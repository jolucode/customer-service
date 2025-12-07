package com.customer.service.client;

import com.customer.service.dto.OrderRequest;
import com.customer.service.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OrderClient {

    private final WebClient webClient;

    public OrderClient(@Qualifier("orderWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<OrderResponse> createOrder(OrderRequest request) {
        return webClient.post()
                .uri("/orders")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OrderResponse.class);
    }
}

