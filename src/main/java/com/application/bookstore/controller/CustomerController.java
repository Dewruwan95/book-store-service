package com.application.bookstore.controller;


import com.application.bookstore.dto.CustomerDto;
import com.application.bookstore.dto.CustomerRequestDto;
import com.application.bookstore.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-store-service/v1/customers")
@SecurityRequirement(name = "basicAuth")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //------------------- Get All Customers ------------------------
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAll() {
        List<CustomerDto> response = customerService.getAll();

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build(); // Better than returning null
        } else {
            return ResponseEntity.ok(response);
        }
    }

    //------------------- Get Single Customer By Id ------------------------
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDto> getOneById(@PathVariable int id) {
        CustomerDto response = customerService.getById(id);
        return ResponseEntity.ok(response);
    }

    //------------------- Create new Customer ------------------------
    @PostMapping
    public ResponseEntity<CustomerDto> create(@RequestBody CustomerRequestDto customerRequestDto) {
        CustomerDto response = customerService.create(customerRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //------------------- Update Customer By Id ------------------------
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(@PathVariable int id, @RequestBody CustomerRequestDto customerRequestDto) {
        CustomerDto response = customerService.update(id, customerRequestDto);
        return ResponseEntity.ok(response);
    }

    //------------------- Delete Customer By Id ------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
