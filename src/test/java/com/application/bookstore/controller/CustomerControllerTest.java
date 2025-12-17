package com.application.bookstore.controller;

import com.application.bookstore.dto.BookDto;
import com.application.bookstore.dto.CustomerDto;
import com.application.bookstore.dto.CustomerRequestDto;
import com.application.bookstore.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private UserDetailsService userDetailsService; // Mock the UserDetailsService

    @Test
    void should_return_all_customers() throws Exception {
        // Mock the user details
        UserDetails user = User.withUsername("testuser")
                .password("hashedPassword") // Any string here, it won't be checked
                .roles("USER")
                .build();

        Mockito.when(userDetailsService.loadUserByUsername("testuser"))
                .thenReturn(user);

        Mockito.when(customerService.getAll()).thenReturn(dummyCustomerDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book-store-service/v1/customers")
                        .header("Authorization", "Basic " +
                                Base64.getEncoder().encodeToString("testuser:password".getBytes())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
    }

    private static List<CustomerDto> dummyCustomerDto() {

        CustomerDto john = new CustomerDto();
        john.setFirstName("John");
        john.setLastName("Doe");
        john.setFullName("John Doe");
        john.setEmail("john.doe@email.com");
        john.setPhoneNumber("0123456789");
        john.setAddress("Nugegoda, Colombo");

        CustomerDto david = new CustomerDto();
        david.setFirstName("David");
        david.setLastName("Allen");
        david.setFullName("David Allen");
        david.setEmail("david.allen@email.com");
        david.setPhoneNumber("1472583690");
        david.setAddress("Jaffna, Sri Lanka");

        return List.of(john, david);

    }


}