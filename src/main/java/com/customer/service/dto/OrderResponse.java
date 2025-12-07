package com.customer.service.dto;

public record OrderResponse(
        Long orderId,
        Long customerId,
        Double amount,
        String description,
        String status
) {}

