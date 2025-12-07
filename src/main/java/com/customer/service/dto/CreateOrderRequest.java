package com.customer.service.dto;

public record CreateOrderRequest(
        Double amount,
        String description
) {}

