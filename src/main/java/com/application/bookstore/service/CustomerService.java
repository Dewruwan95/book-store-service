package com.application.bookstore.service;

import com.application.bookstore.dto.CustomerDto;
import com.application.bookstore.dto.CustomerRequestDto;
import com.application.bookstore.exception.EmailAlreadyExistsException;
import com.application.bookstore.exception.ValidationException;
import com.application.bookstore.model.Customer;
import com.application.bookstore.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //--------------------------------------------------------------
    //------------------- Get All Customers ------------------------
    //--------------------------------------------------------------
    public List<CustomerDto> getAll() {
        return toDto(customerRepository.findAll());
    }

    //--------------------------------------------------------------
    //------------------- Get Single Customer By Id ----------------
    //--------------------------------------------------------------
    public CustomerDto getById(int id) {
        return customerRepository.findById(id).map(customer -> toDto(customer)).orElseThrow(() -> new EntityNotFoundException("Customer not found with id " + id));
    }

    //--------------------------------------------------------------
    //------------------- Create New Customer ----------------------
    //--------------------------------------------------------------
    public CustomerDto create(CustomerRequestDto customerRequestDto) {

        validateCustomerRequestDto(customerRequestDto);

        Customer customer = toEntity(customerRequestDto);
        final Customer savedCustomer = customerRepository.save(customer);
        return toDto(savedCustomer);
    }

    //--------------------------------------------------------------
    //------------------- Update Customer --------------------------
    //--------------------------------------------------------------
    public CustomerDto update(int id, CustomerRequestDto customerRequestDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id " + id));

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
        return toDto(savedCustomer);
    }

    //--------------------------------------------------------------
    //------------------- Delete Customer --------------------------
    //--------------------------------------------------------------
    public void delete(int id) {
        customerRepository.deleteById(id);
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
            throw new EmailAlreadyExistsException("customer",customerRequestDto.getEmail());
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
        List<CustomerDto> result = customers.stream().map(customer -> toDto(customer)).toList();

        return result;
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
