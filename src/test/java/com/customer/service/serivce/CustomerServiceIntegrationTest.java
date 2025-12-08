package com.customer.service.serivce;

import com.customer.service.client.OrderClient;
import com.customer.service.domain.Customer;
import com.customer.service.dto.CreateOrderRequest;
import com.customer.service.dto.OrderRequest;
import com.customer.service.dto.OrderResponse;
import com.customer.service.exception.CustomerNotFoundException;
import com.customer.service.respository.CustomerRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceIntegrationTest {

    private MockWebServer mockWebServer;
    private CustomerService customerService;
    private CustomerRepository repository;
    private OrderClient orderClient;

    @BeforeAll
    void setupServer() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    void tearDownServer() throws Exception {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setup() {
        // Mock repository
        repository = Mockito.mock(CustomerRepository.class);

        // Configurar WebClient para apuntar al MockWebServer
        String baseUrl = mockWebServer.url("/orders").toString();

        orderClient = new OrderClient(WebClient.builder().baseUrl(baseUrl).build());

        customerService = new CustomerService(repository, orderClient);
    }

    // TEST 1 — CREACIÓN DE ORDEN EXITOSA
    @Test
    @DisplayName("Should create an order when customer exists and OrderService returns 200 OK")
    void should_CreateOrder_When_CustomerExists_Then_ReturnOrderResponse() {

        // ARRANGE
        Long customerId = 10L;
        Customer customerMock = new Customer(customerId, "Jose", "jose@example.com");

        Mockito.when(repository.findById(customerId))
                .thenReturn(Mono.just(customerMock));

        CreateOrderRequest request = new CreateOrderRequest(1200.0, "Laptop MSI");

        // Simular respuesta JSON del OrderService
        String jsonResponse = """
                {
                  "orderId": 1,
                  "customerId": 10,
                  "amount": 1200.0,
                  "description": "Laptop MSI",
                  "status": "CREATED"
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        // ACT
        Mono<OrderResponse> resultMono =
                customerService.createOrderForCustomer(customerId, request);

        OrderResponse result = resultMono.block();

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(1);
        assertThat(result.customerId()).isEqualTo(10L);
        assertThat(result.status()).isEqualTo("CREATED");
    }
}
