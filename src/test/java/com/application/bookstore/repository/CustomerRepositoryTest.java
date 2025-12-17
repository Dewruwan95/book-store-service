package com.application.bookstore.repository;

import com.application.bookstore.model.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import javax.sql.DataSource;
import java.util.List;

@DataJpaTest
@AutoConfigureMockMvc(addFilters = false)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DataSource dataSource;


    @BeforeEach
    void setUp() {
        Customer john = new Customer();
        john.setFirstName("John");
        john.setLastName("Doe");
        john.setEmail("john.doe@email.com");
        john.setPhoneNumber("0123456789");
        john.setAddress("Nugegoda, Colombo");

        customerRepository.save(john);

        Customer david = new Customer();
        david.setFirstName("David");
        david.setLastName("Allen");
        david.setEmail("david.allen@email.com");
        david.setPhoneNumber("1472583690");
        david.setAddress("Jaffna, Sri Lanka");

        customerRepository.save(david);

    }

    @Test
    void context_loads(){
        Assertions.assertNotNull(customerRepository);
        Assertions.assertNotNull(dataSource);
    }

    @Test
    void should_return_all_persons(){

        List<Customer> result = customerRepository.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
    }


}