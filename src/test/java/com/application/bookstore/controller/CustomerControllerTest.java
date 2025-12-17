package com.application.bookstore.controller;

import com.application.bookstore.dto.CustomerDto;
import com.application.bookstore.dto.CustomerRequestDto;
import com.application.bookstore.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@AutoConfigureMockMvc(addFilters = false)  // This disables all filters including security
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;


    @Test
    void should_return_all_customers() throws Exception {

        Mockito.when(customerService.getAll()).thenReturn(dummyCustomerDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book-store-service/v1/customers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fullName").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].email").value("john.doe@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].phoneNumber").value("0123456789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].address").value("Nugegoda, Colombo"));

    }


    @Test
    void should_create_new_customer() throws Exception {

        CustomerRequestDto inputCustomerDto = new CustomerRequestDto();
        inputCustomerDto.setFirstName("John");
        inputCustomerDto.setLastName("Doe");
        inputCustomerDto.setEmail("john.doe@email.com");
        inputCustomerDto.setPhoneNumber("0123456789");
        inputCustomerDto.setAddress("Nugegoda, Colombo");

        Mockito.when(customerService.create(Mockito.any(CustomerRequestDto.class)))
                .thenReturn(dummyCustomerDto().getFirst());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book-store-service/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(inputCustomerDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("0123456789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Nugegoda, Colombo"));

    }


    private static List<CustomerDto> dummyCustomerDto() {

        CustomerDto john = new CustomerDto();
        john.setId(1);
        john.setFirstName("John");
        john.setLastName("Doe");
        john.setFullName("John Doe");
        john.setEmail("john.doe@email.com");
        john.setPhoneNumber("0123456789");
        john.setAddress("Nugegoda, Colombo");

        CustomerDto david = new CustomerDto();
        david.setId(2);
        david.setFirstName("David");
        david.setLastName("Allen");
        david.setFullName("David Allen");
        david.setEmail("david.allen@email.com");
        david.setPhoneNumber("1472583690");
        david.setAddress("Jaffna, Sri Lanka");

        return List.of(john, david);

    }


    private static String toJson(CustomerRequestDto customerRequestDto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(customerRequestDto);
    }


}