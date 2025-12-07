package com.customer.service.controller;

import com.customer.service.domain.Customer;

import com.customer.service.dto.CreateOrderRequest;
import com.customer.service.dto.OrderResponse;
import com.customer.service.serivce.CustomerService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    Mono<Customer> getById(@PathVariable Long id) {
        return service.getCustomer(id);
    }

    @PostMapping
    Mono<Customer> create(@RequestBody Customer customer) {
        return service.saveCustomer(customer);
    }

    @GetMapping
    Flux<Customer> search(@RequestParam(required = false) String q) {
        return service.searchByName(q);
    }

    @PostMapping("/{id}/orders")
    Mono<OrderResponse> createOrder(
            @PathVariable Long id,
            @RequestBody CreateOrderRequest request
    ) {
        return service.createOrderForCustomer(id, request);
    }
}
