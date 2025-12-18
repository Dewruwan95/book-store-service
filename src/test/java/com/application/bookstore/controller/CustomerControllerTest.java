package com.application.bookstore.controller;

import com.application.bookstore.dto.CustomerDto;
import com.application.bookstore.dto.CustomerRequestDto;
import com.application.bookstore.exception.AttributeAlreadyExistsException;
import com.application.bookstore.exception.ValidationException;
import com.application.bookstore.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.*;
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
    void should_return_no_content_when_no_customers() throws Exception {
        Mockito.when(customerService.getAll()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book-store-service/v1/customers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    void should_return_single_customer() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(1);
        customerDto.setFirstName("John");
        customerDto.setLastName("Doe");
        customerDto.setFullName("John Doe");
        customerDto.setEmail("john.doe@email.com");
        customerDto.setPhoneNumber("0123456789");
        customerDto.setAddress("Nugegoda, Colombo");

        Mockito.when(customerService.getById(1)).thenReturn(customerDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book-store-service/v1/customers/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("0123456789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Nugegoda, Colombo"));
    }


        @Test
    void should_create_new_customer_by_id() throws Exception {

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


    @Test
    void should_update_customer() throws Exception {
        CustomerRequestDto updateCustomerDto = new CustomerRequestDto();
        updateCustomerDto.setFirstName("John");
        updateCustomerDto.setLastName("Smith");
        updateCustomerDto.setEmail("john.smith@email.com");
        updateCustomerDto.setPhoneNumber("9876543210");
        updateCustomerDto.setAddress("Colombo 05, Sri Lanka");

        CustomerDto updatedCustomerDto = new CustomerDto();
        updatedCustomerDto.setId(1);
        updatedCustomerDto.setFirstName("John");
        updatedCustomerDto.setLastName("Smith");
        updatedCustomerDto.setFullName("John Smith");
        updatedCustomerDto.setEmail("john.smith@email.com");
        updatedCustomerDto.setPhoneNumber("9876543210");
        updatedCustomerDto.setAddress("Colombo 05, Sri Lanka");

        Mockito.when(customerService.update(Mockito.eq(1), Mockito.any(CustomerRequestDto.class)))
                .thenReturn(updatedCustomerDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/book-store-service/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateCustomerDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("John Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.smith@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("9876543210"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Colombo 05, Sri Lanka"));
    }


    @Test
    void should_delete_customer() throws Exception {
        Mockito.doNothing().when(customerService).delete(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book-store-service/v1/customers/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


     @Test
    void should_return_bad_request_when_required_fields_are_missing() throws Exception {
        CustomerRequestDto incompleteCustomerDto = new CustomerRequestDto();
        incompleteCustomerDto.setFirstName("John");
        // Missing lastName, email, phoneNumber, address

        Mockito.when(customerService.create(Mockito.any(CustomerRequestDto.class)))
                .thenThrow(new ValidationException("lastName"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book-store-service/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(incompleteCustomerDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void should_return_bad_request_for_invalid_email_format() throws Exception {
        CustomerRequestDto invalidEmailDto = new CustomerRequestDto();
        invalidEmailDto.setFirstName("John");
        invalidEmailDto.setLastName("Doe");
        invalidEmailDto.setEmail("invalid-email"); // Invalid email format
        invalidEmailDto.setPhoneNumber("0123456789");
        invalidEmailDto.setAddress("Nugegoda, Colombo");

        Mockito.when(customerService.create(Mockito.any(CustomerRequestDto.class)))
                .thenThrow(new ValidationException("email", "Invalid email format"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book-store-service/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidEmailDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void should_return_conflict_when_email_already_exists() throws Exception {
        CustomerRequestDto duplicateEmailDto = new CustomerRequestDto();
        duplicateEmailDto.setFirstName("John");
        duplicateEmailDto.setLastName("Doe");
        duplicateEmailDto.setEmail("existing@email.com"); // Email that already exists
        duplicateEmailDto.setPhoneNumber("0123456789");
        duplicateEmailDto.setAddress("Nugegoda, Colombo");

        Mockito.when(customerService.create(Mockito.any(CustomerRequestDto.class)))
                .thenThrow(new AttributeAlreadyExistsException("Customer", "email", "existing@email.com"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book-store-service/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(duplicateEmailDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }


    @Test
    void should_return_not_found_when_updating_non_existing_customer() throws Exception {
        CustomerRequestDto updateDto = new CustomerRequestDto();
        updateDto.setFirstName("John");
        updateDto.setLastName("Doe");
        updateDto.setEmail("john.doe@email.com");
        updateDto.setPhoneNumber("0123456789");
        updateDto.setAddress("Nugegoda, Colombo");

        Mockito.when(customerService.update(Mockito.eq(999), Mockito.any(CustomerRequestDto.class)))
                .thenThrow(new EntityNotFoundException("Customer not found with id 999"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/book-store-service/v1/customers/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(updateDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    void should_return_not_found_when_deleting_non_existing_customer() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Customer not found with id 999"))
                .when(customerService).delete(999);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book-store-service/v1/customers/999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
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