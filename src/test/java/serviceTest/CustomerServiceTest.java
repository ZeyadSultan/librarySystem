package serviceTest;

import com.example.librarySystem.exception.ApiError;
import com.example.librarySystem.exception.CustomException;
import com.example.librarySystem.model.Customer;
import com.example.librarySystem.repository.CustomerRepository;
import com.example.librarySystem.service.BorrowingRecordService;
import com.example.librarySystem.service.CustomerService;
import com.example.librarySystem.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    private CustomerRepository customerRepository;

    private BorrowingRecordService borrowingRecordService;

    private PasswordEncoder passwordEncoder;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        borrowingRecordService = mock(BorrowingRecordService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        customerService = new CustomerServiceImpl(customerRepository,borrowingRecordService, passwordEncoder);
    }

    @Test
    void findAll_returnsAllCustomers() {
        List<Customer> customers = Arrays.asList(
                new Customer(1L, "John Doe", "john@example.com", "password", "address", "1234567890"),
                new Customer(2L, "Jane Doe", "jane@example.com", "password", "address", "0987654321")
        );

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerService.findAll();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void findById_existingId_returnsCustomer() {
        Customer customer = new Customer(1L, "John Doe", "john@example.com", "password", "address", "1234567890");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.findById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> customerService.findById(1L));

        assertEquals("Customer id not found!", exception.getMessage());

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void save_encryptsPasswordAndSavesCustomer() {
        Customer customer = new Customer(1L, "John Doe", "john@example.com", "password", "address", "1234567890");
        when(passwordEncoder.encode("password")).thenReturn("encryptedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.save(customer);

        assertNotNull(result);
        assertEquals("encryptedPassword", result.getPassword());

        verify(passwordEncoder, times(1)).encode("password");
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void updateCustomer_existingId_updatesAndReturnsCustomer() {
        Customer existingCustomer = new Customer(1L, "John Doe", "john@example.com", "password", "address", "1234567890");
        Customer updatedCustomer = new Customer(1L, "John Updated", "johnupdated@example.com", "newpassword", "newaddress", "0987654321");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(passwordEncoder.encode("newpassword")).thenReturn("encryptedNewPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

        Customer result = customerService.updateCustomer(1L, updatedCustomer);

        System.out.println("Result password: " + result.getPassword());
        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        assertEquals("johnupdated@example.com", result.getEmail());
        assertEquals("encryptedNewPassword", result.getPassword());

        // Additional verification
        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    void updateCustomer_nonExistingId_throwsException() {
        Customer updatedCustomer = new Customer(1L, "John Updated", "johnupdated@example.com", "newpassword", "newaddress", "0987654321");

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> customerService.updateCustomer(1L, updatedCustomer));

        assertEquals("Customer id not found!", exception.getMessage());

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteById_customerWithActiveBorrowings_throwsException() {
        Customer customer = new Customer(1L, "John Doe", "john@example.com", "password", "address", "1234567890");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(borrowingRecordService.hasActiveBorrowings(customer)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> customerService.deleteById(1L));

        assertEquals("You can't delete this customer he has current borrowings, delete the borrowings first!!", exception.getMessage());

        verify(customerRepository, times(1)).findById(1L);
        verify(borrowingRecordService, times(1)).hasActiveBorrowings(customer);
        verify(customerRepository, never()).deleteById(1L);
    }

    @Test
    void deleteById_customerWithoutActiveBorrowings_deletesCustomer() {
        Customer customer = new Customer(1L, "John Doe", "john@example.com", "password", "address", "1234567890");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(borrowingRecordService.hasActiveBorrowings(customer)).thenReturn(false);

        customerService.deleteById(1L);

        verify(customerRepository, times(1)).findById(1L);
        verify(borrowingRecordService, times(1)).hasActiveBorrowings(customer);
        verify(customerRepository, times(1)).deleteById(1L);
    }
}
