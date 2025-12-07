package com.customer.service.dto;

public record OrderRequest(
        Long customerId,
        Double amount,
        String description
) {}

