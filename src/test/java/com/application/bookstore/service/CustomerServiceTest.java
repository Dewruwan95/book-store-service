package com.application.bookstore.service;


import com.application.bookstore.dto.CustomerDto;
import com.application.bookstore.dto.CustomerRequestDto;
import com.application.bookstore.model.Customer;
import com.application.bookstore.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;


    @BeforeEach
    void beforeEachTest() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void context_loads() {
        Assertions.assertNotNull(customerService);
        Assertions.assertNotNull(customerRepository);
    }

    @Test
    void should_return_all_customers() {

        Mockito.when(customerRepository.findAll()).thenReturn(dummyCustomer());

        List<CustomerDto> result = customerService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals("John", result.getFirst().getFirstName());
        Assertions.assertEquals("Doe", result.getFirst().getLastName());
        Assertions.assertEquals("john.doe@email.com", result.getFirst().getEmail());
        Assertions.assertEquals("0123456789", result.getFirst().getPhoneNumber());
        Assertions.assertEquals("Nugegoda, Colombo", result.getFirst().getAddress());

        Assertions.assertEquals("David", result.get(1).getFirstName());
        Assertions.assertEquals("Allen", result.get(1).getLastName());
        Assertions.assertEquals("david.allen@email.com", result.get(1).getEmail());
        Assertions.assertEquals("1472583690", result.get(1).getPhoneNumber());
        Assertions.assertEquals("Jaffna, Sri Lanka", result.get(1).getAddress());

    }

    @Test
    void should_create_new_customer() {

        CustomerRequestDto request = new CustomerRequestDto();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@email.com");
        request.setPhoneNumber("0123456789");
        request.setAddress("Nugegoda, Colombo");


        Customer savedCustomer = new Customer();
        savedCustomer.setId(1);
        savedCustomer.setFirstName("John");
        savedCustomer.setLastName("Doe");
        savedCustomer.setEmail("john.doe@email.com");
        savedCustomer.setPhoneNumber("0123456789");
        savedCustomer.setAddress("Nugegoda, Colombo");


        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(savedCustomer);

        CustomerDto result = customerService.create(request);


        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("John", result.getFirstName());
        Assertions.assertEquals("Doe", result.getLastName());
        Assertions.assertEquals("john.doe@email.com", result.getEmail());
        Assertions.assertEquals("0123456789", result.getPhoneNumber());
        Assertions.assertEquals("Nugegoda, Colombo", result.getAddress());
    }

    private static List<Customer> dummyCustomer() {

        Customer john = new Customer();
        john.setId(1);
        john.setFirstName("John");
        john.setLastName("Doe");
        john.setEmail("john.doe@email.com");
        john.setPhoneNumber("0123456789");
        john.setAddress("Nugegoda, Colombo");

        Customer david = new Customer();
        david.setId(2);
        david.setFirstName("David");
        david.setLastName("Allen");
        david.setEmail("david.allen@email.com");
        david.setPhoneNumber("1472583690");
        david.setAddress("Jaffna, Sri Lanka");

        return List.of(john, david);

    }


}