package com.ecommerce.order.services;

import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.models.Order;
import com.ecommerce.order.models.OrderItem;
import com.ecommerce.order.models.OrderStatus;
import com.ecommerce.order.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public Optional<OrderResponse> createOrder(String userId) {
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty()) return Optional.empty();
//        User user = userOpt.get();

        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item-> new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        //Update the stock quantity.
//        cartItems.forEach(item-> {
//            Product product = item.getProduct();
//            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
//            productRepository.save(product);
//        });

        // Publish Order Event
        OrderCreatedEvent event = new  OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getStatus(),
                mapToOrderDTO(savedOrder.getItems(), savedOrder.getTotalAmount()),
                savedOrder.getTotalAmount(),
                savedOrder.getCreatedAt()
        );

        rabbitTemplate.convertAndSend(exchangeName, routingKey, event);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private List<OrderItemDTO> mapToOrderDTO(List<OrderItem> items, BigDecimal totalAmount) {
        return items.stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        totalAmount
                )).toList();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems()
                        .stream().map(item-> new OrderItemDTO(
                                item.getId(),
                                String.valueOf(item.getProductId()),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                        ))
                        .toList(),
                order.getCreatedAt()
        );
    }
}
