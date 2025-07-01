package com.ecommerce.notification;

import com.ecommerce.notification.payload.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class OrderEventConsumer {

    @Bean
    public Consumer<OrderCreatedEvent> createdOrder() {
        return event -> {
            log.info("Received order event for order ID: {}, & user ID: {}", event.getOrderId(), event.getUserId());
        };
    }

//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void handleOrderEvent(OrderCreatedEvent orderEvent) {
//        System.out.println("Receive Order Event: " + orderEvent);
//
//        Long orderId = orderEvent.getOrderId();
//        OrderStatus status = orderEvent.getStatus();
//
//        System.out.println("Order ID: " + orderId);
//        System.out.println("Order Status: " + status);
//
//        // todo: Send Notification
//        // todo: Send Emails
//        // todo: Generate Invoice
//        // todo: Update Database
//        // todo: Send Seller Notification
//    }

}
