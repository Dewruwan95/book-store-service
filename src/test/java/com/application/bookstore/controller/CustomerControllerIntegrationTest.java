//package com.application.bookstore.controller;
//
//import com.application.bookstore.dto.CustomerDto;
//import com.application.bookstore.dto.CustomerRequestDto;
//import com.application.bookstore.model.Customer;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.*;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
////@AutoConfigureMockMvc(addFilters = false) not needed for TestRestTemplate
////@ActiveProfiles("test")
//@Transactional  // This ensures each test runs in its own transaction
//class CustomerControllerIntegrationTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    // Mock the SecurityFilterChain bean - this prevents the real security from loading
//    @MockitoBean
//    private SecurityFilterChain securityFilterChain;
//
//    @Test
//    void should_create_new_customer() {
//
//        final CustomerRequestDto john = new CustomerRequestDto();
//        john.setFirstName("John");
//        john.setLastName("Doe");
//        john.setEmail("john.doe@email.com");
//        john.setPhoneNumber("0123456789");
//        john.setAddress("Nugegoda, Colombo");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<CustomerRequestDto> request = new HttpEntity<>(john, headers);
//
//        ResponseEntity<CustomerDto> response = restTemplate.postForEntity(
//                "http://localhost:" + port + "/api/book-store-service/v1/customers",
//                request,
//                CustomerDto.class
//        );
//
//        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
//
//        CustomerDto responseBody = response.getBody();
//
//        Assertions.assertNotNull(responseBody);
//        Assertions.assertNotNull(responseBody.getId());
//        Assertions.assertEquals("John", responseBody.getFirstName());
//        Assertions.assertEquals("Doe", responseBody.getLastName());
//        Assertions.assertEquals("John Doe", responseBody.getFullName());
//        Assertions.assertEquals("john.doe@email.com", responseBody.getEmail());
//        Assertions.assertEquals("0123456789", responseBody.getPhoneNumber());
//        Assertions.assertEquals("Nugegoda, Colombo", responseBody.getAddress());
//
//    }
//
////        @Test
////    void should_return_a_customer(){
////
////        CustomerRequestDto input = new CustomerRequestDto();
////        input.setFirstName("John");
////        input.setLastName("Doe");
////        input.setEmail("john.doe@email.com");
////        input.setPhoneNumber("1234567890");
////        input.setAddress("Nugegoda, Colombo");
////
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
////
////        HttpEntity<CustomerRequestDto> httpEntity = new HttpEntity<>(input, headers);
////
////
////        ResponseEntity<CustomerDto> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/book-store-service/v1/customers", httpEntity, CustomerDto.class);
////
////        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
////        Assertions.assertEquals(, responseEntity.getBody());
////    }
////
////
//
//}