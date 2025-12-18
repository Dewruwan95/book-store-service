package com.application.bookstore.service;

import com.application.bookstore.dto.CustomerDto;
import com.application.bookstore.dto.CustomerRequestDto;
import com.application.bookstore.exception.AttributeAlreadyExistsException;
import com.application.bookstore.exception.ValidationException;
import com.application.bookstore.model.Customer;
import com.application.bookstore.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //--------------------------------------------------------------
    //------------------- Get All Customers ------------------------
    //--------------------------------------------------------------
    public List<CustomerDto> getAll() {
        logger.info("Fetching all customers");
        return toDto(customerRepository.findAll());
    }

    //--------------------------------------------------------------
    //------------------- Get Single Customer By Id ----------------
    //--------------------------------------------------------------
    public CustomerDto getById(int id) {
        logger.info("Fetching customer with ID: {}", id);
        return customerRepository.findById(id).map(this::toDto).orElseThrow(() -> {
            logger.warn("Customer not found with ID: {}", id);
            return new EntityNotFoundException("Customer not found with id " + id);
        });
    }

    //--------------------------------------------------------------
    //------------------- Create New Customer ----------------------
    //--------------------------------------------------------------
    public CustomerDto create(CustomerRequestDto customerRequestDto) {

        logger.info("Creating new customer with email: {}", customerRequestDto.getEmail());

        validateCustomerRequestDto(customerRequestDto);

        Customer customer = toEntity(customerRequestDto);
        final Customer savedCustomer = customerRepository.save(customer);

        logger.info("Customer created successfully with ID: {} and email: {}", savedCustomer.getId(), savedCustomer.getEmail());

        return toDto(savedCustomer);
    }

    //--------------------------------------------------------------
    //------------------- Update Customer --------------------------
    //--------------------------------------------------------------
    public CustomerDto update(int id, CustomerRequestDto customerRequestDto) {

        logger.info("Updating customer with ID: {}", id);

        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() -> {
            logger.warn("Cannot update: Customer not found with ID: {}", id);
            return new EntityNotFoundException("Customer not found with id " + id);
        });

        if (customerRequestDto.getFirstName() != null) {
            existingCustomer.setFirstName(customerRequestDto.getFirstName());
        }
        if (customerRequestDto.getLastName() != null) {
            existingCustomer.setLastName(customerRequestDto.getLastName());
        }
        if (customerRequestDto.getEmail() != null) {
            existingCustomer.setEmail(customerRequestDto.getEmail());
        }
        if (customerRequestDto.getPhoneNumber() != null) {
            existingCustomer.setPhoneNumber(customerRequestDto.getPhoneNumber());
        }
        if (customerRequestDto.getAddress() != null) {
            existingCustomer.setAddress(customerRequestDto.getAddress());
        }

        final Customer savedCustomer = customerRepository.save(existingCustomer);

        logger.info("Customer updated successfully with ID: {}", savedCustomer.getId());

        return toDto(savedCustomer);
    }

    //--------------------------------------------------------------
    //------------------- Delete Customer --------------------------
    //--------------------------------------------------------------
    public void delete(int id) {

        logger.info("Deleting customer with ID: {}", id);

        if (!customerRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent customer ID: {}", id);
            throw new EntityNotFoundException("Customer not found with id " + id);
        }
        customerRepository.deleteById(id);
        logger.info("Customer deleted successfully with ID: {}", id);
    }


    //--------------------------------------------------------------
    //------------------- Validate CustomerRequestDto --------------
    //--------------------------------------------------------------
    private void validateCustomerRequestDto(CustomerRequestDto customerRequestDto) {
        if (customerRequestDto.getFirstName() == null) {
            throw new ValidationException("firstName");
        }

        if (customerRequestDto.getLastName() == null) {
            throw new ValidationException("lastName");
        }

        if (customerRequestDto.getEmail() == null) {
            throw new ValidationException("email");
        }

        if (!customerRequestDto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("email", "Invalid email format");
        }

        // Check if email already exists in database
        if (customerRepository.findByEmail(customerRequestDto.getEmail()) != null) {
            throw new AttributeAlreadyExistsException("Customer", "email", customerRequestDto.getEmail());
        }

        if (customerRequestDto.getPhoneNumber() == null) {
            throw new ValidationException("phoneNumber");
        }

        if (customerRequestDto.getAddress() == null) {
            throw new ValidationException("address");
        }


    }

    //--------------------------------------------------------------
    //----------------- Convert Customer to CustomerDto ------------
    //--------------------------------------------------------------
    public List<CustomerDto> toDto(List<Customer> customers) {

        return customers.stream().map(this::toDto).toList();
    }


    private CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDto result = new CustomerDto();
        result.setId(customer.getId());
        result.setFirstName(customer.getFirstName());
        result.setLastName(customer.getLastName());
        result.setFullName(customer.getFirstName() + " " + customer.getLastName());
        result.setEmail(customer.getEmail());
        result.setPhoneNumber(customer.getPhoneNumber());
        result.setAddress(customer.getAddress());

        return result;

    }

    //--------------------------------------------------------------
    // ------------ convert CustomerDto to Customer ----------------
    //--------------------------------------------------------------
    private Customer toEntity(CustomerRequestDto customerRequestDto) {
        if (customerRequestDto == null) {
            return null;
        }
        Customer result = new Customer();
        result.setFirstName(customerRequestDto.getFirstName());
        result.setLastName(customerRequestDto.getLastName());
        result.setEmail(customerRequestDto.getEmail());
        result.setPhoneNumber(customerRequestDto.getPhoneNumber());
        result.setAddress(customerRequestDto.getAddress());

        return result;
    }
}
