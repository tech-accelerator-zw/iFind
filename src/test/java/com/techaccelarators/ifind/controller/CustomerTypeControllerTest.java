package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.CustomerType;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.customer.CustomerTypeDto;
import com.techaccelarators.ifind.dtos.customer.CustomerTypeRequest;
import com.techaccelarators.ifind.exception.ForbiddenException;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.exception.UnauthorizedException;
import com.techaccelarators.ifind.service.CustomerTypeService;
import com.techaccelarators.ifind.util.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerTypeControllerTest {
    @Mock
    private CustomerTypeService customerTypeService;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private CustomerTypeController customerTypeController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        this.customerTypeController = new CustomerTypeController(customerTypeService);
    }

    CustomerType RECORD_1 = new CustomerType("Consultancy");
    CustomerType RECORD_2 = new CustomerType("Pharmacy");
    CustomerType RECORD_3 = new CustomerType("Gardner");

    @Test
    void createCustomerTypeSuccess() {
        CustomerTypeRequest customerTypeRequest = new CustomerTypeRequest();
        CustomerType customerType = new CustomerType("test");

        when(customerTypeService.createCustomerType(customerTypeRequest)).thenReturn(customerType);

        Response<CustomerTypeDto> response = customerTypeController.createCustomerType(customerTypeRequest);

        assertEquals("CustomerType Created Successfully", response.getMessage());
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        verify(customerTypeService).createCustomerType(customerTypeRequest);

    }
    @Test
    void createCustomerTypeThrowsInvalidRequest() {
        CustomerTypeRequest customerTypeRequest = new CustomerTypeRequest();

        when(customerTypeService.createCustomerType(customerTypeRequest)).thenThrow(new InvalidRequestException("Invalid Request"));

        assertThrows(InvalidRequestException.class,()-> customerTypeController.createCustomerType(customerTypeRequest));
        verify(customerTypeService).createCustomerType(customerTypeRequest);

    }

    @Test
    void updateCustomerTypeSuccess() {
        Long id = 3L;
        CustomerTypeRequest customerTypeRequest = new CustomerTypeRequest();

        CustomerType customerType = new CustomerType("123");

        when(customerTypeService.updateCustomerType(id,customerTypeRequest)).thenReturn(customerType);

        Response<CustomerTypeDto> response = customerTypeController.updateCustomerType(id,customerTypeRequest);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(customerType.getName(),response.getResult().getName());
        verify(customerTypeService).updateCustomerType(id,customerTypeRequest);

    }
    @Test
    void updateCustomerTypeThrowsRecordNotFoundException() {
        Long id = 3L;
        CustomerTypeRequest customerTypeRequest = new CustomerTypeRequest();

        when(customerTypeService.updateCustomerType(id,customerTypeRequest)).thenThrow(new RecordNotFoundException("Customer Not Found"));

        assertThrows(RecordNotFoundException.class,()-> customerTypeController.updateCustomerType(id,customerTypeRequest));
        verify(customerTypeService).updateCustomerType(id,customerTypeRequest);

    }
    @Test
    void updateCustomerTypeThrowsInvalidRequestException() {
        Long id = 3L;
        CustomerTypeRequest customerTypeRequest = new CustomerTypeRequest();

        when(customerTypeService.updateCustomerType(id,customerTypeRequest)).thenThrow(new InvalidRequestException("Invalid Request"));

        assertThrows(InvalidRequestException.class,()-> customerTypeController.updateCustomerType(id,customerTypeRequest));
        verify(customerTypeService).updateCustomerType(id,customerTypeRequest);

    }

    @Test
    void getAllCustomerTypesSuccess() {
        Page<CustomerType> customerTypes = new PageImpl<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3),pageable,3L);
        when(customerTypeService.getAllCustomerTypes(pageable)).thenReturn(customerTypes);

        Response<Page<CustomerTypeDto>> response = customerTypeController.getAllCustomerTypes(pageable);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(3,response.getResult().getTotalElements());
    }
    @Test
    void getAllCustomerTypesReturnsEmptyList() {
        Page<CustomerType> customerTypes = new PageImpl<>(Collections.emptyList());
        when(customerTypeService.getAllCustomerTypes(pageable)).thenReturn(customerTypes);

        Response<Page<CustomerTypeDto>> response = customerTypeController.getAllCustomerTypes(pageable);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("SUCCESS",response.getMessage());
        assertEquals(0,response.getResult().getTotalElements());
    }

    @Test
    void getCustomerTypeByIdSuccess() {
        RECORD_1.setId(1L);
        when(customerTypeService.getCustomerTypeById(RECORD_1.getId())).thenReturn(RECORD_1);

        Response<CustomerTypeDto> response = customerTypeController.getCustomerTypeById(RECORD_1.getId());

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertEquals(RECORD_1.getName(),response.getResult().getName());
        verify(customerTypeService).getCustomerTypeById(RECORD_1.getId());
    }
    @Test
    void getCustomerTypeByIdThrowsRecordNotFoundException() {
        when(customerTypeService.getCustomerTypeById(anyLong())).thenThrow(new RecordNotFoundException("Customer Type Not Found"));

        assertThrows(RecordNotFoundException.class,()-> customerTypeController.getCustomerTypeById(anyLong()));
        verify(customerTypeService).getCustomerTypeById(anyLong());
    }

    @Test
    void getCustomerTypeByName() {
        RECORD_1.setName("test");
        when(customerTypeService.getCustomerTypeByName(RECORD_1.getName())).thenReturn(RECORD_1);

        Response<CustomerTypeDto> response = customerTypeController.getCustomerTypeByName(RECORD_1.getName());

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        assertEquals(RECORD_1.getName(),response.getResult().getName());
        verify(customerTypeService).getCustomerTypeByName(RECORD_1.getName());
    }
    @Test
    void getCustomerTypeByNameThrowsRecordNotFoundException() {
        when(customerTypeService.getCustomerTypeByName(anyString())).thenThrow(new RecordNotFoundException("Record Not Found"));

        assertThrows(RecordNotFoundException.class,()-> customerTypeController.getCustomerTypeByName(anyString()));
        verify(customerTypeService).getCustomerTypeByName(anyString());
    }

    @Test
    void searchCustomerTypeSuccess() {
        String searchParam = "test";
        Page<CustomerType> customerTypes = new PageImpl<>(Collections.singletonList(RECORD_1),pageable,1L);
        when(customerTypeService.searchCustomerType(searchParam,pageable)).thenReturn(customerTypes);

        Response<Page<CustomerTypeDto>> response = customerTypeController.searchCustomerType(searchParam,pageable);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(RECORD_1.getName(),response.getResult().getContent().get(0).getName());
        verify(customerTypeService).searchCustomerType(searchParam,pageable);
    }
    @Test
    void searchCustomerTypeThrowsInvalidRequest() {
        String searchParam = "test";

        when(customerTypeService.searchCustomerType(searchParam,pageable)).thenThrow(new InvalidRequestException("Invalid Request"));

        assertThrows(InvalidRequestException.class,()-> customerTypeController.searchCustomerType(searchParam,pageable));
        verify(customerTypeService).searchCustomerType(searchParam,pageable);
    }

    @Test
    void toggleCustomerTypeStatus() {
        RECORD_1.setId(1L);
        RECORD_1.setStatus(Status.ACTIVE);

        when(customerTypeService.toggleCustomerTypeStatus(1L)).thenReturn(RECORD_1);

        Response<CustomerTypeDto> response = customerTypeController.toggleCustomerTypeStatus(RECORD_1.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer Type Status Updated Successfully", response.getMessage());
        assertNotNull(response.getResult());
        assertEquals(RECORD_1.getId(), response.getResult().getId());
        assertEquals(RECORD_1.getStatus(), response.getResult().getStatus());
    }

    @Test
    void deleteCustomerTypeSuccess() {
        Long customerTypeId = 123L;
        doNothing().when(customerTypeService).deleteCustomerType(customerTypeId);
        Response<?> response = customerTypeController.deleteCustomerType(customerTypeId);

        verify(customerTypeService).deleteCustomerType(customerTypeId);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Customer Type Deleted Successfully",response.getMessage());

    }
    @Test
    void deleteCustomerTypeForbidden() {
        doThrow(new ForbiddenException("Forbidden")).when(customerTypeService).deleteCustomerType(anyLong());

        assertThrows(ForbiddenException.class,()-> customerTypeController.deleteCustomerType(anyLong()));
        verify(customerTypeService).deleteCustomerType(anyLong());

    }
    @Test
    void deleteCustomerTypeUnauthorized() {
        doThrow(new UnauthorizedException("Unauthorized")).when(customerTypeService).deleteCustomerType(anyLong());

        assertThrows(UnauthorizedException.class,()-> customerTypeController.deleteCustomerType(anyLong()));
        verify(customerTypeService).deleteCustomerType(anyLong());

    }
}