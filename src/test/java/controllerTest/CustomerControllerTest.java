package controllerTest;

import com.example.librarySystem.model.Customer;
import com.example.librarySystem.service.CustomerService;
import com.example.librarySystem.controller.CustomerController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    private CustomerService customerService;
    private CustomerController customerController;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    public void setUp() {
        customerService = Mockito.mock(CustomerService.class);
        customerController = new CustomerController(customerService);

        customer1 = new Customer(1L, "John Doe", "john@example.com", "password", "address", "1234567890");
        customer2 = new Customer(2L, "Jane Doe", "jane@example.com", "password", "address", "0987654321");
    }

    @Test
    public void testGetAllCustomers() {
        when(customerService.findAll()).thenReturn(Arrays.asList(customer1, customer2));
        ResponseEntity<List<Customer>> response = customerController.getAllCustomers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetCustomerById() {
        when(customerService.findById(1L)).thenReturn(customer1);
        ResponseEntity<Customer> response = customerController.getCustomerById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer1, response.getBody());
    }

    @Test
    public void testCreateCustomer() {
        when(customerService.save(customer1)).thenReturn(customer1);
        ResponseEntity<Customer> response = customerController.createCustomer(customer1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(customer1, response.getBody());
    }

    @Test
    public void testUpdateCustomer() {
        when(customerService.updateCustomer(1L, customer1)).thenReturn(customer1);
        ResponseEntity<Customer> response = customerController.updateCustomer(1L, customer1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer1, response.getBody());
    }

    @Test
    public void testDeleteCustomer() {
        doNothing().when(customerService).deleteById(1L);
        ResponseEntity<String> response = customerController.deleteCustomer(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer deleted Successfully!", response.getBody());
    }
}
