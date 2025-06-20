package com.ecommerce.notification;

import com.ecommerce.notification.payload.OrderCreatedEvent;
import com.ecommerce.notification.payload.OrderStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(OrderCreatedEvent orderEvent) {
        System.out.println("Receive Order Event: " + orderEvent);

        Long orderId = orderEvent.getOrderId();
        OrderStatus status = orderEvent.getStatus();

        System.out.println("Order ID: " + orderId);
        System.out.println("Order Status: " + status);

        // todo: Send Notification
        // todo: Send Emails
        // todo: Generate Invoice
        // todo: Update Database
        // todo: Send Seller Notification
    }

}
