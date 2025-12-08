package com.customer.service.controller;

import com.customer.service.domain.Customer;
import com.customer.service.dto.CreateOrderRequest;
import com.customer.service.dto.OrderResponse;
import com.customer.service.exception.CustomerNotFoundException;
import com.customer.service.serivce.CustomerService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerService customerService;

    @Test
    @DisplayName("Should return a customer when a valid ID is provided")
    void should_ReturnCustomer_When_IdExists() {
        // ARRANGE
        Customer customer = new Customer(1L, "Jose Perez", "jose@example.com");
        Mockito.when(customerService.getCustomer(1L))
                .thenReturn(Mono.just(customer));

        // ACT
        var response = webTestClient.get()
                .uri("/customers/1")
                .exchange();

        // ASSERT
        response.expectStatus().isOk()
                .expectBody(Customer.class)
                .value(result ->
                        assertThat(result.name()).isEqualTo("Jose Perez")
                );
    }

    @Test
    @DisplayName("Should create a customer when valid data is provided")
    void should_CreateCustomer_When_RequestIsValid_Then_ReturnCreatedCustomer() {

        // ARRANGE
        Customer request = new Customer(null, "Juan Lopez", "juan@example.com");
        Customer saved = new Customer(10L, "Juan Lopez", "juan@example.com");

        Mockito.when(customerService.saveCustomer(request))
                .thenReturn(Mono.just(saved));

        // ACT
        var response = webTestClient.post()
                .uri("/customers")
                .bodyValue(request)
                .exchange();

        // ASSERT
        response.expectStatus().isOk()
                .expectBody(Customer.class)
                .value(result ->
                        assertThat(result.id()).isEqualTo(10L)
                );
    }

    @Test
    @DisplayName("Should create an order for a customer when a valid request is provided")
    void should_CreateOrder_When_RequestIsValid_Then_ReturnOrderResponse() {

        // ARRANGE
        Long customerId = 5L;

        // CreateOrderRequest(Double amount, String description)
        CreateOrderRequest request = new CreateOrderRequest(
                1500.0,
                "Laptop MSI"
        );

        OrderResponse responseMock = new OrderResponse(
                1L,
                customerId,
                1500.0,
                "Laptop MSI",
                "CREATED"
        );

        Mockito.when(customerService.createOrderForCustomer(customerId, request))
                .thenReturn(Mono.just(responseMock));

        // ACT
        var response = webTestClient.post()
                .uri("/customers/" + customerId + "/orders")
                .bodyValue(request)
                .exchange();

        // ASSERT
        response.expectStatus().isOk()
                .expectBody(OrderResponse.class)
                .value(result -> {
                    assertThat(result.customerId()).isEqualTo(customerId);
                    assertThat(result.orderId()).isEqualTo(1L);
                    assertThat(result.amount()).isEqualTo(1500.0);
                    assertThat(result.description()).isEqualTo("Laptop MSI");
                    assertThat(result.status()).isEqualTo("CREATED");
                });
    }

    @Test
    @DisplayName("Should return NOT FOUND when customer does not exist (no JSON expected)")
    void should_ReturnNotFound_When_CustomerDoesNotExist_WithoutBody() {

        long customerId = 99L;

        Mockito.when(customerService.getCustomer(customerId))
                .thenReturn(Mono.error(new CustomerNotFoundException(customerId)));

        webTestClient.get()
                .uri("/customers/{id}", customerId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }

}