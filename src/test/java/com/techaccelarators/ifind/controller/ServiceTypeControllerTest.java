package com.techaccelarators.ifind.controller;

import com.techaccelarators.ifind.domain.ServiceType;
import com.techaccelarators.ifind.domain.enums.Status;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeDto;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeRequest;
import com.techaccelarators.ifind.dtos.servicetype.ServiceTypeResponseDto;
import com.techaccelarators.ifind.exception.ForbiddenException;
import com.techaccelarators.ifind.exception.InvalidRequestException;
import com.techaccelarators.ifind.exception.RecordNotFoundException;
import com.techaccelarators.ifind.exception.UnauthorizedException;
import com.techaccelarators.ifind.service.ServiceTypeService;
import com.techaccelarators.ifind.util.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;


class ServiceTypeControllerTest {
    @Mock
    private ServiceTypeService serviceTypeService;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private ServiceTypeController serviceTypeController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        this.serviceTypeController = new ServiceTypeController(serviceTypeService);
    }

    ServiceType RECORD_1 = new ServiceType("test","sajfgdlhagsouydgab",Status.ACTIVE);
    ServiceType RECORD_2 = new ServiceType("test1","sgdlhagsouydgab",Status.ACTIVE);
    ServiceType RECORD_3 = new ServiceType("test2","sajfhagsouydgab",Status.ACTIVE);
    ServiceTypeRequest serviceTypeRequest = new ServiceTypeRequest();

    @Test
    void createServiceTypeSuccess() {
        when(serviceTypeService.createServiceType(serviceTypeRequest)).thenReturn(RECORD_1);

        Response<ServiceTypeDto> response = serviceTypeController.createServiceType(serviceTypeRequest);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals("Service Type Created Successfully",response.getMessage());
        assertEquals(RECORD_1.getName(),response.getResult().getName());
    }
    @Test
    void createServiceTypeThrowsInvalidRequestException() {

        when(serviceTypeService.createServiceType(serviceTypeRequest)).thenThrow(new InvalidRequestException("Invalid Request"));

        assertThrows(InvalidRequestException.class,()-> serviceTypeController.createServiceType(serviceTypeRequest));
        verify(serviceTypeService).createServiceType(serviceTypeRequest);
    }

    @Test
    void updateServiceTypeSuccess() {
        Long id = 1L;
        when(serviceTypeService.updateServiceType(id,serviceTypeRequest)).thenReturn(RECORD_1);
        Response<ServiceTypeDto> response = serviceTypeController.updateServiceType(id,serviceTypeRequest);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(RECORD_1.getName(),response.getResult().getName());
        assertEquals("Service Type Updated Successfully",response.getMessage());
        verify(serviceTypeService).updateServiceType(id,serviceTypeRequest);
    }
    @Test
    void updateServiceTypeThrowsInvalidRequestException() {
        Long id = 1L;
        when(serviceTypeService.updateServiceType(id,serviceTypeRequest)).thenThrow(new InvalidRequestException("Invalid Request"));
        assertThrows(InvalidRequestException.class,()-> serviceTypeController.updateServiceType(id,serviceTypeRequest));
        verify(serviceTypeService).updateServiceType(id,serviceTypeRequest);
    }

    @Test
    void getAllServiceTypesSuccess() {
        Page<ServiceType> serviceTypes = new PageImpl<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3),pageable,3L);
        when(serviceTypeService.getAllServiceTypes(pageable)).thenReturn(serviceTypes);

        Response<Page<ServiceTypeDto>> response = serviceTypeController.getAllServiceTypes(pageable);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(3,response.getResult().getTotalElements());
    }
    @Test
    void getAllServiceTypesReturnEmptyPage() {
        Page<ServiceType> serviceTypes = new PageImpl<>(Collections.emptyList());
        when(serviceTypeService.getAllServiceTypes(pageable)).thenReturn(serviceTypes);

        Response<Page<ServiceTypeDto>> response = serviceTypeController.getAllServiceTypes(pageable);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(0,response.getResult().getTotalElements());
    }

    @Test
    void getServiceTypeByIdSuccess() {
        Long id = 2L;
        ServiceTypeResponseDto serviceTypeResponseDto = new ServiceTypeResponseDto();
        when(serviceTypeService.getServiceTypeById(id)).thenReturn(serviceTypeResponseDto);

        Response<ServiceTypeResponseDto> response = serviceTypeController.getServiceTypeById(id);

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        verify(serviceTypeService).getServiceTypeById(id);
    }
    @Test
    void getServiceTypeByIdThrowsRecordNotFoundException() {
        when(serviceTypeService.getServiceTypeById(anyLong())).thenThrow(new RecordNotFoundException("Service Type Not Found"));

        assertThrows(RecordNotFoundException.class,()-> serviceTypeController.getServiceTypeById(anyLong()));
        verify(serviceTypeService).getServiceTypeById(anyLong());
    }

    @Test
    void getServiceTypeByNameSuccess() {
        String name = "test";
        when(serviceTypeService.getServiceTypeByName(name)).thenReturn(RECORD_2);

        Response<ServiceTypeDto> response = serviceTypeController.getServiceTypeByName(name);

        assertEquals(HttpStatus.FOUND,response.getStatusCode());
        verify(serviceTypeService).getServiceTypeByName(name);
    }
    @Test
    void getServiceTypeByNameThrowsRecordNotFoundException() {
        when(serviceTypeService.getServiceTypeByName(anyString())).thenThrow(new RecordNotFoundException("Not Found"));

        assertThrows(RecordNotFoundException.class,()-> serviceTypeController.getServiceTypeByName(anyString()));
        verify(serviceTypeService).getServiceTypeByName(anyString());
    }

    @Test
    void searchServiceTypeSuccess() {
        String searchParam = "test";
        Page<ServiceType> serviceTypes = new PageImpl<>(Collections.singletonList(RECORD_1),pageable,1L);
        when(serviceTypeService.searchServiceType(searchParam,pageable)).thenReturn(serviceTypes);

        Response<Page<ServiceTypeDto>> response = serviceTypeController.searchServiceType(searchParam,pageable);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(RECORD_1.getName(),response.getResult().getContent().get(0).getName());
        verify(serviceTypeService).searchServiceType(searchParam,pageable);
    }
    @Test
    void searchServiceTypeThrowsInvalidRequestException() {
        String searchParam = "test";
        when(serviceTypeService.searchServiceType(searchParam,pageable)).thenThrow(new InvalidRequestException("Invalid Request"));

        assertThrows(InvalidRequestException.class,()-> serviceTypeController.searchServiceType(searchParam,pageable));
        verify(serviceTypeService).searchServiceType(searchParam,pageable);
    }

    @Test
    void toggleServiceTypeStatus() {
        RECORD_1.setStatus(Status.ACTIVE);

        when(serviceTypeService.toggleServiceTypeStatus(1L)).thenReturn(RECORD_1);

        Response<ServiceTypeDto> response = serviceTypeController.toggleServiceTypeStatus(RECORD_1.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ServiceType Status Updated Successfully", response.getMessage());
        assertNotNull(response.getResult());
        assertEquals(RECORD_1.getStatus(), response.getResult().getStatus());
    }

    @Test
    void deleteServiceTypeSuccess() {
        Long serviceTypeId = 123L;
        doNothing().when(serviceTypeService).deleteServiceType(serviceTypeId);
        Response<?> response = serviceTypeController.deleteServiceType(serviceTypeId);

        verify(serviceTypeService).deleteServiceType(serviceTypeId);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("ServiceType Deleted Successfully",response.getMessage());

    }
    @Test
    void deleteServiceTypeForbidden() {
        doThrow(new ForbiddenException("Forbidden")).when(serviceTypeService).deleteServiceType(anyLong());

        assertThrows(ForbiddenException.class,()-> serviceTypeController.deleteServiceType(anyLong()));
        verify(serviceTypeService).deleteServiceType(anyLong());

    }
    @Test
    void deleteServiceTypeUnauthorized() {
        doThrow(new UnauthorizedException("Unauthorized")).when(serviceTypeService).deleteServiceType(anyLong());

        assertThrows(UnauthorizedException.class,()-> serviceTypeController.deleteServiceType(anyLong()));
        verify(serviceTypeService).deleteServiceType(anyLong());

    }
}