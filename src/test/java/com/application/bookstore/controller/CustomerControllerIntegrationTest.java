//package com.application.bookstore.controller;
//
//import com.application.bookstore.dto.CustomerDto;
//import com.application.bookstore.dto.CustomerRequestDto;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class CustomerControllerIntegrationTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//        @Test
//    void should_return_a_customer(){
//
//        CustomerRequestDto input = new CustomerRequestDto();
//        input.setFirstName("John");
//        input.setLastName("Doe");
//        input.setEmail("john.doe@email.com");
//        input.setPhoneNumber("1234567890");
//        input.setAddress("Nugegoda, Colombo");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<CustomerRequestDto> httpEntity = new HttpEntity<>(input, headers);
//
//
//        ResponseEntity<CustomerDto> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/book-store-service/v1/customers", httpEntity, CustomerDto.class);
//
//        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        Assertions.assertEquals(, responseEntity.getBody());
//    }
//
//}