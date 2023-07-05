package com.techaccelarators.ifind.controller;


import com.techaccelarators.ifind.domain.Customer;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.customer.CustomerDto;
import com.techaccelarators.ifind.dtos.customer.CustomerRequest;
import com.techaccelarators.ifind.dtos.customer.CustomerResponseDto;
import com.techaccelarators.ifind.exception.ForbiddenException;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.exception.UnauthorizedException;
import com.techaccelarators.ifind.service.CustomerService;
import com.techaccelarators.ifind.util.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;


import java.util.Arrays;
import java.util.Collections;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerTest {
    @Mock
    private CustomerService customerService;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        this.customerController = new CustomerController(customerService);
    }

    Customer RECORD_1 = Customer.builder().name("Logikmind").description("IT Consultancy Company").build();
    Customer RECORD_2 = Customer.builder().name("Cocacola").description("International Beverages Company").build();
    Customer RECORD_3 = Customer.builder().name("Bola").description("Hardware Company").build();

    @Test
    public void createCustomer_success() {

        CustomerRequest customerRequest = new CustomerRequest();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("tyfah");
        when(customerService.createCustomer(customerRequest)).thenReturn(customer);

        Response<CustomerDto> response = customerController.createCustomer(customerRequest);

        assertEquals("Customer Created Successfully", response.getMessage());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(customer.getId(), response.getResult().getId());
        assertEquals(customer.getName(), response.getResult().getName());
    }

    @Test
    public void createCustomer_recordNotFound() {

        CustomerRequest customerRequest = new CustomerRequest();
        when(customerService.createCustomer(customerRequest)).thenThrow(new RecordNotFoundException("Record not found"));

        assertThrows(RecordNotFoundException.class,()-> customerController.createCustomer(customerRequest));
        verify(customerService).createCustomer(customerRequest);
    }

    @Test
    public void createCustomer_invalidRequest() {

        CustomerRequest customerRequest = new CustomerRequest();
        when(customerService.createCustomer(customerRequest)).thenThrow(new InvalidRequestException("Invalid Request"));

        assertThrows(InvalidRequestException.class,()-> customerController.createCustomer(customerRequest));
        verify(customerService).createCustomer(customerRequest);

    }

    @Test
    void updateCustomerSuccess() {
        Long id = 1L;
        CustomerRequest request = new CustomerRequest();
        request.setName("Customer");
        request.setDescription("Test Customer");

        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(request.getName());
        customer.setDescription(request.getDescription());

        when(customerService.updateCustomer(id, request)).thenReturn(customer);

        Response<CustomerDto> response = customerController.updateCustomer(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer Updated Successfully", response.getMessage());
        assertEquals(customer.getId(), response.getResult().getId());
        assertEquals(customer.getName(), response.getResult().getName());
        assertEquals(customer.getDescription(), response.getResult().getDescription());
    }
    @Test
    public void testUpdateCustomerInvalidRequest() {
        long id = 2L;
        CustomerRequest request = new CustomerRequest();
        when(customerService.updateCustomer(id,request)).thenThrow(new InvalidRequestException("Name should not be null or empty"));

        assertThrows(InvalidRequestException.class,()-> customerController.updateCustomer(id,request));
        verify(customerService).updateCustomer(id,request);
    }

    @Test
    void getCustomerByIdSuccess() {
        Long id = 1L;
        CustomerResponseDto expectedResponse = new CustomerResponseDto();
        expectedResponse.setId(id);
        expectedResponse.setName("Tafadzwa Pundo");
        when(customerService.getCustomerById(id)).thenReturn(expectedResponse);

        Response<CustomerResponseDto> response = customerController.getCustomerById(id);

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals("FOUND", response.getMessage());
        assertEquals(expectedResponse, response.getResult());
    }
    @Test
    public void getCustomerByIdThrowsNotFoundException() {
        when(customerService.getCustomerById(anyLong())).thenThrow(new RecordNotFoundException("Customer Not Found"));

        assertThrows(RecordNotFoundException.class,()-> customerController.getCustomerById(anyLong()));
        verify(customerService).getCustomerById(anyLong());
    }
    @Test
    public void getCustomerByIdThrowsInternalServerError() {
        when(customerService.getCustomerById(anyLong())).thenThrow(new RuntimeException("Something went wrong"));
        assertThrows(RuntimeException.class,()-> customerController.getCustomerById(anyLong()));
        verify(customerService).getCustomerById(anyLong());
    }

    @Test
    void getAllCustomersSuccess() {
        Page<Customer> customers = new PageImpl<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3),pageable,3L);
        when(customerService.getAllCustomers(pageable)).thenReturn(customers);

        Response<Page<CustomerDto>> response = customerController.getAllCustomers(pageable);

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertEquals("FOUND",response.getMessage());
        assertEquals(3,response.getResult().getTotalElements());
    }
    @Test
    void getAllCustomersReturnsEmptyList() {
        Page<Customer> customers = new PageImpl<>(Collections.emptyList());
        when(customerService.getAllCustomers(pageable)).thenReturn(customers);

        Response<Page<CustomerDto>> response = customerController.getAllCustomers(pageable);

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertEquals("FOUND",response.getMessage());
        assertEquals(0,response.getResult().getTotalElements());
    }

    @Test
    void getActiveCustomersSuccess() {
        RECORD_1.setStatus(Status.ACTIVE);
        RECORD_3.setStatus(Status.ACTIVE);

        Page<Customer> activeCustomers = new PageImpl<>(Arrays.asList(RECORD_3,RECORD_1));
        when(customerService.getAllActiveCustomers(pageable)).thenReturn(activeCustomers);

        Response<Page<CustomerDto>> response = customerController.getActiveCustomers(pageable);

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertEquals("FOUND",response.getMessage());
        assertEquals(2,response.getResult().getTotalElements());
        assertEquals(RECORD_3.getName(),response.getResult().getContent().get(0).getName());
    }
    @Test
    void getActiveCustomersReturnsEmptyList() {

        Page<Customer> activeCustomers = new PageImpl<>(Collections.emptyList());
        when(customerService.getAllActiveCustomers(pageable)).thenReturn(activeCustomers);

        Response<Page<CustomerDto>> response = customerController.getActiveCustomers(pageable);

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertEquals("FOUND",response.getMessage());
        assertEquals(0,response.getResult().getTotalElements());
    }

    @Test
    void searchCustomerSuccess() {
        String searchParam = "Logikmind";

        Page<Customer> customers = new PageImpl<>(Collections.singletonList(RECORD_1),pageable,1L);
        when(customerService.searchCustomer(searchParam,pageable)).thenReturn(customers);

        Response<Page<CustomerDto>> response = customerController.searchCustomer(searchParam,pageable);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("SUCCESSFUL",response.getMessage());
        assertEquals(searchParam, response.getResult().getContent().get(0).getName());
    }
    @Test
    void searchCustomerThrowsInvalidRequest(){
        String searchParam = "Logikmind";
        when(customerService.searchCustomer(searchParam,pageable)).thenThrow(new InvalidRequestException("Invalid Request"));

        assertThrows(InvalidRequestException.class,()-> customerController.searchCustomer(searchParam,pageable));
        verify(customerService).searchCustomer(searchParam,pageable);
    }
    @Test
    void searchCustomerThrowsInternalServerError(){
        String searchParam = "Harare";
        when(customerService.searchCustomer(searchParam,pageable)).thenThrow(new RuntimeException("Something went wrong"));

        assertThrows(RuntimeException.class,()-> customerController.searchCustomer(searchParam,pageable));
        verify(customerService).searchCustomer(searchParam,pageable);
    }


    @Test
    void toggleCustomerStatus() {
        RECORD_1.setId(1L);
        RECORD_1.setStatus(Status.ACTIVE);

        when(customerService.toggleCustomerStatus(1L)).thenReturn(RECORD_1);

        Response<CustomerDto> response = customerController.toggleCustomerStatus(RECORD_1.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer Status Updated Successfully", response.getMessage());
        assertNotNull(response.getResult());
        assertEquals(RECORD_1.getId(), response.getResult().getId());
        assertEquals(RECORD_1.getStatus(), response.getResult().getStatus());
    }

    @Test
    void assignCustomerToServiceTypeSuccess() {
        Long customerId = 1L;
        Long serviceId = 2L;

        Response<?> response = customerController.assignCustomerToServiceType(customerId, serviceId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Assigned Customer to ServiceType", response.getMessage());

        verify(customerService).assignCustomerToServiceType(customerId, serviceId);
    }
    @Test
    void assignCustomerToServiceType_ShouldReturnErrorResponse_WhenRecordNotFoundExceptionIsThrown() {
        String errorMessage = "Customer record not found";

        doThrow(new RecordNotFoundException(errorMessage)).when(customerService).assignCustomerToServiceType(anyLong(), anyLong());

        assertThrows(RecordNotFoundException.class,()-> customerController.assignCustomerToServiceType(anyLong(),anyLong()));
        verify(customerService).assignCustomerToServiceType(anyLong(),anyLong());
    }

    @Test
    void deleteCustomerSuccess() {
        doNothing().when(customerService).deleteCustomer(anyLong());
        Response<?> response = customerController.deleteCustomer(anyLong());

        verify(customerService).deleteCustomer(anyLong());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Customer Deleted Successfully",response.getMessage());

    }
    @Test
    void deleteCustomerForbidden() {
        doThrow(new ForbiddenException("Forbidden")).when(customerService).deleteCustomer(anyLong());
        assertThrows(ForbiddenException.class,()-> customerController.deleteCustomer(anyLong()));
        verify(customerService).deleteCustomer(anyLong());
    }
    @Test
    void deleteCustomerUnauthorized() {
        doThrow(new UnauthorizedException("Unauthorized")).when(customerService).deleteCustomer(anyLong());
        assertThrows(UnauthorizedException.class,()-> customerController.deleteCustomer(anyLong()));
        verify(customerService).deleteCustomer(anyLong());
    }
}