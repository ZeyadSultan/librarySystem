package com.example.librarySystem.service.impl;

import com.example.librarySystem.exception.ApiError;
import com.example.librarySystem.model.Customer;
import com.example.librarySystem.repository.CustomerRepository;
import com.example.librarySystem.service.BorrowingRecordService;
import com.example.librarySystem.service.CustomerService;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BorrowingRecordService borrowingRecordService;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, BorrowingRecordService borrowingRecordService, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.borrowingRecordService = borrowingRecordService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (!customer.isPresent()) {
            throw ApiError.notFound("Customer id not found!");
        } else {
            return customer.get();
        }
    }

    @Override
    public Customer save(Customer customer) {
        String encryptedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encryptedPassword);
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        Optional<Customer> savedCustomer = customerRepository.findById(id);
        if (!savedCustomer.isPresent()) {
            throw ApiError.notFound("Customer id not found!");
        } else {
            Customer existingCustomer = savedCustomer.get();
            existingCustomer.setName(customer.getName());
            existingCustomer.setEmail(customer.getEmail());
            return customerRepository.save(existingCustomer);
        }
    }

    @Override
    public void deleteById(Long id) {

        boolean hasActiveBorrowings = borrowingRecordService.hasActiveBorrowings(findById(id));

        if (hasActiveBorrowings) {
            throw ApiError.badRequest("You can't delete this customer he have current borrowings, delete the borrowings first!!");
        }

        customerRepository.deleteById(id);
    }
}