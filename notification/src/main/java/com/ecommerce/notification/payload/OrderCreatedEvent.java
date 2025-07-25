package com.ecommerce.notification.payload;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private String userId;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    protected BigDecimal totalAmount;
    protected LocalDateTime createdTime;

    public OrderCreatedEvent() {}

}
