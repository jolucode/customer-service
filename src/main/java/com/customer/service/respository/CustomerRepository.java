package com.customer.service.respository;

import com.customer.service.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {
    Flux<Customer> findByNameContainsIgnoreCase(String name);
}
