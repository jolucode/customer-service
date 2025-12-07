package com.customer.service.serivce;

import com.customer.service.client.OrderClient;
import com.customer.service.domain.Customer;
import com.customer.service.dto.CreateOrderRequest;
import com.customer.service.dto.OrderRequest;
import com.customer.service.dto.OrderResponse;
import com.customer.service.exception.CustomerNotFoundException;
import com.customer.service.respository.CustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final OrderClient orderClient;

    public CustomerService(CustomerRepository repository, OrderClient orderClient) {
        this.repository = repository;
        this.orderClient = orderClient;
    }

    public Mono<Customer> getCustomer(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(id)));
    }

    public Mono<Customer> saveCustomer(Customer c) {
        return Optional.ofNullable(c)
                .map(Mono::just)
                .orElseGet(() -> Mono.error(new IllegalArgumentException("Invalid customer")))
                .flatMap(repository::save);
    }

    public Flux<Customer> searchByName(String q) {
        return Optional.ofNullable(q)
                .filter(s -> !s.isBlank())
                .map(repository::findByNameContainsIgnoreCase)
                .orElseGet(repository::findAll);
    }

    public Mono<OrderResponse> createOrderForCustomer(Long id, CreateOrderRequest request) {
        return getCustomer(id)
                .flatMap(c -> orderClient.createOrder(
                        new OrderRequest(id, request.amount(), request.description())
                ));
    }
}

