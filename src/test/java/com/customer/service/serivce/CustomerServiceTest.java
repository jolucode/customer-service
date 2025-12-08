package com.customer.service.serivce;

import com.customer.service.client.OrderClient;
import com.customer.service.domain.Customer;
import com.customer.service.dto.CreateOrderRequest;
import com.customer.service.dto.OrderRequest;
import com.customer.service.dto.OrderResponse;
import com.customer.service.exception.CustomerNotFoundException;
import com.customer.service.respository.CustomerRepository;
import com.customer.service.serivce.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository repository;

    @Mock
    OrderClient orderClient;

    @InjectMocks
    CustomerService service;


    @Test
    @DisplayName("getCustomer - Should return a customer when ID exists")
    void should_ReturnCustomer_when_IdExists() {

        // ARRANGE
        Customer customer = new Customer(1L, "Jose", "jose@example.com");

        when(repository.findById(1L)).thenReturn(Mono.just(customer));

        // ACT & ASSERT
        StepVerifier.create(service.getCustomer(1L))
                .expectNext(customer)
                .verifyComplete();

        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("getCustomer - Should throw CustomerNotFoundException when ID does not exist")
    void should_ThrowException_when_IdDoesNotExist() {

        // ARRANGE
        when(repository.findById(99L)).thenReturn(Mono.empty());

        // ACT & ASSERT
        StepVerifier.create(service.getCustomer(99L))
                .expectErrorMatches(ex ->
                        ex instanceof CustomerNotFoundException &&
                                ex.getMessage().contains("99")
                )
                .verify();

        verify(repository).findById(99L);
    }

    @Test
    @DisplayName("saveCustomer - Should save and return customer when input is valid")
    void should_SaveCustomer_when_InputIsValid() {

        // ARRANGE
        Customer c = new Customer(1L, "Luis", "luis@test.com");

        when(repository.save(c)).thenReturn(Mono.just(c));

        // ACT & ASSERT
        StepVerifier.create(service.saveCustomer(c))
                .assertNext(saved -> assertThat(saved.name()).isEqualTo("Luis"))
                .verifyComplete();

        verify(repository).save(c);
    }

    @Test
    @DisplayName("createOrderForCustomer - Should create order when customer exists")
    void should_CreateOrder_when_CustomerExists() {

        // ARRANGE
        Long customerId = 1L;
        CreateOrderRequest request = new CreateOrderRequest(120.0, "Laptop");

        OrderResponse response = new OrderResponse(
                customerId, 1L,  120.0,"Laptop", "CREATED"
        );

        when(repository.findById(customerId))
                .thenReturn(Mono.just(new Customer(customerId, "Jose", "j@j.com")));

        when(orderClient.createOrder(any(OrderRequest.class)))
                .thenReturn(Mono.just(response));

        // ACT & ASSERT
        StepVerifier.create(service.createOrderForCustomer(customerId, request))
                .expectNext(response)
                .verifyComplete();

        verify(orderClient).createOrder(any(OrderRequest.class));
    }


    @Test
    @DisplayName("createOrderForCustomer - Should fail when customer does not exist")
    void should_ThrowException_when_CustomerDoesNotExist() {

        when(repository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.createOrderForCustomer(99L, new CreateOrderRequest(10.0, "X")))
                .expectError(CustomerNotFoundException.class)
                .verify();

        verify(orderClient, never()).createOrder(any());
    }

}