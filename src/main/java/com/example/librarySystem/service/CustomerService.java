package com.example.librarySystem.service;

import com.example.librarySystem.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    Customer findById(Long id);
    Customer save(Customer customer);
    Customer updateCustomer(Long id, Customer customer);
    void deleteById(Long id);
}