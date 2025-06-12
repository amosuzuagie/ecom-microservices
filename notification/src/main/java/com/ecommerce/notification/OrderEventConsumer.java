package com.ecommerce.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderEventConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleOrderEvent(Map<String, Object> orderEvent) {
        System.out.println("Receive Order Event: " + orderEvent);

        Long orderId = Long.valueOf(orderEvent.get("orderId").toString());
        String status = orderEvent.get("status").toString();

        // todo: Send Notification
        // todo: Send Emails
        // todo: Generate Invoice
        // todo: Update Database
        // todo: Send Seller Notification
    }

}
