package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.CustomerBranch;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.domain.util.Address;
import com.techaccelarators.ifind.domain.util.ContactDetails;
import com.techaccelarators.ifind.dtos.branch.CustomerBranchDto;
import com.techaccelarators.ifind.dtos.branch.CustomerBranchRequest;
import com.techaccelarators.ifind.dtos.city.CityDto;
import com.techaccelarators.ifind.exception.ForbiddenException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.service.CustomerBranchService;
import com.techaccelarators.ifind.util.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerBranchControllerTest {
    @Mock
    private CustomerBranchService customerBranchService;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private CustomerBranchController customerBranchController;

    Customer customer = Customer.builder()
            .name("Test Customer")
            .build();
    Address address = new Address("21 Skymaster Belvedere","","Harare","Zimbabwe");
    ContactDetails contactDetails = new ContactDetails("+2637890654","0789654789","test@junit.com");

    CustomerBranch RECORD_1 = new CustomerBranch("Msasa",address,contactDetails);
    CustomerBranch RECORD_2 = new CustomerBranch("Willovale",address,contactDetails);
    CustomerBranch RECORD_3 = new CustomerBranch("Mazowe",address,contactDetails);

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @BeforeEach
    public void injectMocks(){
        this.customerBranchController = new CustomerBranchController(customerBranchService);
    }

    @Test
    @DisplayName("createCustomerBranchSuccess")
    void createCustomerBranch() {
        Long id = 3L;
        CustomerBranch customerBranch = CustomerBranch.builder()
                .name("Westlea")
                .build();
        CustomerBranchRequest customerBranchRequest = new CustomerBranchRequest();

        when(customerBranchService.createCustomerBranch(id,customerBranchRequest)).thenReturn(customerBranch);

        Response<CustomerBranchDto> response = customerBranchController.createCustomerBranch(id,customerBranchRequest);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals("Customer Branch Created Successfully",response.getMessage());
        assertEquals(customerBranch.getName(),response.getResult().getName());
    }

    @Test
    @DisplayName("updateCustomerBranchSuccess")
    void updateCustomerBranch() {
        Customer customer = new Customer();
        customer.setId(3L);
        CustomerBranchRequest customerBranchRequest = new CustomerBranchRequest();
        CustomerBranch customerBranch = new CustomerBranch();
        customerBranch.setId(1L);

        when(customerBranchService.updateCustomerBranch(customer.getId(),customerBranch.getId(),customerBranchRequest))
                .thenReturn(customerBranch);

        Response<CustomerBranchDto> response = customerBranchController.updateCustomerBranch(customer.getId(),customerBranch.getId(),customerBranchRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNull(customerBranch.getName());
    }

    @Test
    @DisplayName("getCustomerBranchByIdSuccess")
    void getCustomerBranchById() {
        RECORD_3.setId(5L);

        when(customerBranchService.getCustomerBranchById(RECORD_3.getId())).thenReturn(RECORD_3);
        Response<CustomerBranchDto> response = customerBranchController.getCustomerBranchById(RECORD_3.getId());

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertEquals("FOUND",response.getMessage());
        assertEquals(RECORD_3.getName(),response.getResult().getName());
    }
    @Test
    @DisplayName("getCustomerBranchByIdThrowsRecordNotFoundException")
    void getCustomerBranchByIdThrowsRecordNotFoundException() {
        Long id = 7L;

        when(customerBranchService.getCustomerBranchById(id)).thenThrow(new RecordNotFoundException("Customer Branch Not Found"));
        assertThrows(RecordNotFoundException.class,()->customerBranchController.getCustomerBranchById(id));
    }
    @Test
    @DisplayName("getCustomerBranchByIdThrowsInternalServerError")
    void getCustomerBranchByIdThrowsInternalServerError() {
        Long id = 7L;

        when(customerBranchService.getCustomerBranchById(id)).thenThrow(new RuntimeException("Something went wrong"));
        assertThrows(RuntimeException.class,()->customerBranchController.getCustomerBranchById(id));
    }

    @Test
    @DisplayName("getActiveCustomerBranchesSuccess")
    void getActiveCustomerBranches() {
        RECORD_1.setStatus(Status.ACTIVE);
        RECORD_2.setStatus(Status.ACTIVE);
        Set<CustomerBranch> customerBranches = Set.of(RECORD_1,RECORD_2);
        customer.setId(1L);
        customer.setCustomerBranches(customerBranches);

        Page<CustomerBranch> branches = new PageImpl<>(Arrays.asList(RECORD_1,RECORD_2),pageable,2L);
        when(customerBranchService.getAllActiveCustomerBranches(customer.getId(), pageable)).thenReturn(branches);

        Response<Page<CustomerBranchDto>> response = customerBranchController.getActiveCustomerBranches(customer.getId(), pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(2,response.getResult().getTotalElements());
    }
    @Test
    @DisplayName("getActiveCustomerBranchesThrowsRecordNotFoundException")
    void getActiveCustomerBranchesWithWrongCustomerId() {
        customer.setId(1L);

        when(customerBranchService.getAllActiveCustomerBranches(customer.getId(), pageable)).thenThrow(new RecordNotFoundException("Customer with given id not found"));

        assertThrows(RecordNotFoundException.class,()-> customerBranchController.getActiveCustomerBranches(customer.getId(), pageable));
        verify(customerBranchService).getAllActiveCustomerBranches(customer.getId(), pageable);
    }
    @Test
    @DisplayName("getActiveCustomerBranchesEmptyList")
    void getActiveCustomerBranchesEmptyList() {
        customer.setId(1L);

        Page<CustomerBranch> branches = new PageImpl<>(Collections.emptyList());
        when(customerBranchService.getAllActiveCustomerBranches(customer.getId(), pageable)).thenReturn(branches);

        Response<Page<CustomerBranchDto>> response = customerBranchController.getActiveCustomerBranches(customer.getId(), pageable);

        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(0,response.getResult().getTotalElements());
    }

    @Test
    void toggleCustomerStatus() {
        RECORD_1.setId(1L);
        RECORD_1.setStatus(Status.ACTIVE);

        when(customerBranchService.toggleCustomerBranchStatus(1L)).thenReturn(RECORD_1);

        Response<CustomerBranchDto> response = customerBranchController.toggleCustomerStatus(RECORD_1.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer Branch Status Updated Successfully", response.getMessage());
        assertNotNull(response.getResult());
        assertEquals(RECORD_1.getId(), response.getResult().getId());
        assertEquals(RECORD_1.getStatus(), response.getResult().getStatus());
    }

    @Test
    @DisplayName("deleteCustomerBranchSuccess")
    void deleteCustomerBranch() {
        Long id = 3L;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("username","password",authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Response<?> response = customerBranchController.deleteCustomerBranch(id);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Customer Branch Deleted Successfully",response.getMessage());
    }
    @Test
    @DisplayName("deleteCustomerBranchForbidden")
    void deleteCustomerBranchForbidden() {
        Long id = 3L;

        doThrow(ForbiddenException.class).when(customerBranchService).deleteCustomerBranch(id);
        assertThrows(ForbiddenException.class,()-> customerBranchController.deleteCustomerBranch(id));
    }
    @Test
    @DisplayName("deleteCustomerBranchUnauthorized")
    void deleteCustomerBranchUnauthorized() {
        Long id = 3L;
        doThrow(ForbiddenException.class).when(customerBranchService).deleteCustomerBranch(id);
        assertThrows(ForbiddenException.class,()-> customerBranchController.deleteCustomerBranch(id));
    }

}