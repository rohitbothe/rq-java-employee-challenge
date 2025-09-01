package com.reliaquest.api.employee.service;

import com.reliaquest.api.client.EmployeeRestApi;
import com.reliaquest.api.constant.EmployeeConstants;
import com.reliaquest.api.controller.EmployeeControllerImpl;
import com.reliaquest.api.dto.EmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeRestClientDto.DeleteEmployeeRequest;
import com.reliaquest.api.dto.EmployeeRestClientDto.EmployeeListResponse;
import com.reliaquest.api.dto.EmployeeRestClientDto.EmployeeResponse;
import com.reliaquest.api.model.Employee;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Rohit Bothe
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeControllerImpl.class);
    private EmployeeRestApi employeeRestApiService;
    private CircuitBreaker circuitBreaker;

    public EmployeeServiceImpl(EmployeeRestApi employeeRestApiService, CircuitBreakerRegistry registry) {
        this.circuitBreaker = registry.circuitBreaker("employeeApiBreaker");
        this.employeeRestApiService = employeeRestApiService;
    }

    public List<Employee> getAllEmployees() {
        log.info("GetAllEmployees");
        Supplier<List<Employee>> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
            EmployeeListResponse employeeDTO = employeeRestApiService.findAll();
            return employeeDTO.data();
        });

        return decoratedSupplier.get();
    }

    public List<Employee> getAllEmployeesByName(String employeeNameSearch) {
        log.info("GetAllEmployeesByName", employeeNameSearch);
        List<Employee> employeeDTO = getAllEmployees();
        return employeeDTO.stream()
                .filter(emp -> emp.getName().toLowerCase().contains(employeeNameSearch.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Employee getEmployeeById(String id) {
        log.info("GetEmployeeById", id);
        Supplier<Employee> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
            EmployeeResponse employeeDTO = employeeRestApiService.findById(id);
            return employeeDTO.data();
        });
        return decoratedSupplier.get();
    }

    public Integer getHighestSalaryOfEmployees() {
        log.info(EmployeeConstants.INFO_HIGEST_SALARY);
        List<Employee> employeeDTO = getAllEmployees();
        return employeeDTO.stream()
                .map(Employee::getSalary)
                .max(Comparator.naturalOrder())
                .get();
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        log.info(EmployeeConstants.INFO_HIGEST_SALARY);
        List<Employee> employeeDTO = getAllEmployees();
        return employeeDTO.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .limit(10)
                .map(Employee::getName)
                .toList();
    }

    public Employee createEmployee(EmployeeRequestDto employeeRequest) {
        Supplier<Employee> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
            EmployeeResponse employeeDTO = employeeRestApiService.create(employeeRequest);
            return employeeDTO.data();
        });
        log.info(EmployeeConstants.INFO_CREATED, employeeRequest.getName());
        return decoratedSupplier.get();
    }

    public String deleteEmployeeById(String id) {
        Employee emp = getEmployeeById(id);
        String empName = emp.getName();
        employeeRestApiService.delete(new DeleteEmployeeRequest(empName));
        log.info(EmployeeConstants.INFO_DELETED, id);
        return empName;
    }
}
