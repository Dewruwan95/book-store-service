package com.application.bookstore.controller;

import com.application.bookstore.dto.CustomerDto;
import com.application.bookstore.dto.CustomerRequestDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional  // This ensures each test runs in its own transaction
class CustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // Mock the SecurityFilterChain bean - this prevents the real security from loading
    @MockitoBean
    private SecurityFilterChain securityFilterChain;

    @Test
    void should_create_new_customer() {

        final CustomerRequestDto john = new CustomerRequestDto();
        john.setFirstName("John");
        john.setLastName("Doe");
        john.setEmail("john.doe@email.com");
        john.setPhoneNumber("0123456789");
        john.setAddress("Nugegoda, Colombo");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CustomerRequestDto> request = new HttpEntity<>(john, headers);

        ResponseEntity<CustomerDto> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/book-store-service/v1/customers",
                request,
                CustomerDto.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        CustomerDto responseBody = response.getBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals("John", responseBody.getFirstName());
        Assertions.assertEquals("Doe", responseBody.getLastName());
        Assertions.assertEquals("John Doe", responseBody.getFullName());
        Assertions.assertEquals("john.doe@email.com", responseBody.getEmail());
        Assertions.assertEquals("0123456789", responseBody.getPhoneNumber());
        Assertions.assertEquals("Nugegoda, Colombo", responseBody.getAddress());

    }

}