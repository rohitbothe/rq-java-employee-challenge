package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.reliaquest.api.client.EmployeeRestApi;
import com.reliaquest.api.dto.EmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeRestClientDto.DeleteEmployeeRequest;
import com.reliaquest.api.dto.EmployeeRestClientDto.EmployeeListResponse;
import com.reliaquest.api.dto.EmployeeRestClientDto.EmployeeResponse;
import com.reliaquest.api.employee.service.EmployeeServiceImpl;
import com.reliaquest.api.model.Employee;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;

/**
 * @author Rohit Bothe
 */
public class EmployeeServiceImplTest {

    private static final String SUCCESS_MESSAGE = "Request proceeded successfully";

    private EmployeeRestApi employeeRestApiService;
    private CircuitBreakerRegistry circuitBreakerRegistry;
    private CircuitBreaker circuitBreaker;
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        employeeRestApiService = mock(EmployeeRestApi.class);
        circuitBreakerRegistry = mock(CircuitBreakerRegistry.class);
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowSize(2)
                .minimumNumberOfCalls(2)
                .failureRateThreshold(100)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .build();
        circuitBreaker = CircuitBreaker.of("employeeApiBreaker", config);
        when(circuitBreakerRegistry.circuitBreaker("employeeApiBreaker")).thenReturn(circuitBreaker);
        employeeService = new EmployeeServiceImpl(employeeRestApiService, circuitBreakerRegistry);
    }

    @Test
    public void testCircuitBreakerTrips() {
        when(employeeRestApiService.findAll())
                .thenThrow(new HttpClientErrorException(
                        HttpStatus.TOO_MANY_REQUESTS, HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase()));
        Assertions.assertThrows(HttpClientErrorException.class, () -> employeeService.getAllEmployees());
        Assertions.assertThrows(HttpClientErrorException.class, () -> employeeService.getAllEmployees());
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
    }
    
    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = EmployeeTestUtil.getEmployeesList();
        when(employeeRestApiService.findAll()).thenReturn(new EmployeeListResponse(employees, SUCCESS_MESSAGE));
        List<Employee> result = employeeService.getAllEmployees();
        assertEquals(6, result.size());
        Mockito.verify(employeeRestApiService, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetAllEmployeesByName() {
        List<Employee> employees = EmployeeTestUtil.getEmployeesList();
        when(employeeRestApiService.findAll()).thenReturn(new EmployeeListResponse(employees, SUCCESS_MESSAGE));
        List<Employee> result = employeeService.getAllEmployeesByName("Rohit");
        assertEquals(1, result.size());
        assertEquals("Rohit", result.get(0).getName());
    }

    @Test
    public void testGetEmployeeById() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee(id, "Tom", 5000, 24, "Lead Eng", "test@test.com");
        when(employeeRestApiService.findById(id.toString())).thenReturn(new EmployeeResponse(emp, SUCCESS_MESSAGE));
        Employee result = employeeService.getEmployeeById(id.toString());
        Assertions.assertNotNull(result);
        assertEquals("Tom", result.getName());
    }

    @Test
    public void testGetHighestSalaryOfEmployees() {
        List<Employee> employees = EmployeeTestUtil.getEmployeesList(7000000);
        when(employeeRestApiService.findAll()).thenReturn(new EmployeeListResponse(employees, SUCCESS_MESSAGE));
        int highestSalary = employeeService.getHighestSalaryOfEmployees();
        assertEquals(7000000, highestSalary);
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = EmployeeTestUtil.getEmployeesList(6000000);
        when(employeeRestApiService.findAll()).thenReturn(new EmployeeListResponse(employees, SUCCESS_MESSAGE));
        List<String> topEarning = employeeService.getTopTenHighestEarningEmployeeNames();
        assertNotNull(topEarning);
    }

    @Test
    public void testCreateEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto("Tiger Nixon", 320800, 25, "Manager", "rbothe1@visa.com");
        Employee employee = new Employee(UUID.randomUUID(), "Tiger Nixon", 320800, 25, "Manager", "rbothe1@visa.com");
        when(employeeRestApiService.create(request)).thenReturn(new EmployeeResponse(employee, SUCCESS_MESSAGE));
        Employee result = employeeService.createEmployee(request);
        Assertions.assertNotNull(result);
        assertEquals("Tiger Nixon", result.getName());
    }

    @Test
    public void testDeleteEmployeeById() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee(id, "Thomas", 786110, 25, "Sr Product Manager", "test@test132.com");
        when(employeeRestApiService.findById(id.toString())).thenReturn(new EmployeeResponse(emp, SUCCESS_MESSAGE));
        String deletedName = employeeService.deleteEmployeeById(id.toString());
        assertEquals("Thomas", deletedName);
        ArgumentCaptor<DeleteEmployeeRequest> captor = ArgumentCaptor.forClass(DeleteEmployeeRequest.class);
        Mockito.verify(employeeRestApiService).delete(captor.capture());
        assertEquals("Thomas", captor.getValue().name());
    }

    @Test
    public void testGetAllEmployees_whenApiThrowsException() {
        when(employeeRestApiService.findAll())
                .thenThrow(new RestClientResponseException(
                        "HTTP error: " + HttpStatus.BAD_GATEWAY,
                        HttpStatus.BAD_GATEWAY.value(),
                        "Bad gateway",
                        null,
                        null,
                        null));
        RestClientResponseException ex =
                Assertions.assertThrows(RestClientResponseException.class, () -> employeeService.getAllEmployees());
        assertEquals("HTTP error: " + HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    @Test
    public void testGetEmployeeById_whenApiThrowsException() {
        UUID id = UUID.randomUUID();
        when(employeeRestApiService.findById(id.toString()))
                .thenThrow(new RestClientResponseException(
                        "HTTP error: " + HttpStatus.NOT_FOUND,
                        HttpStatus.NOT_FOUND.value(),
                        "No record found",
                        null,
                        null,
                        null));
        RestClientResponseException ex = Assertions.assertThrows(
                RestClientResponseException.class, () -> employeeService.getEmployeeById(id.toString()));
        assertEquals("HTTP error: " + HttpStatus.NOT_FOUND, ex.getMessage());
    }

}
