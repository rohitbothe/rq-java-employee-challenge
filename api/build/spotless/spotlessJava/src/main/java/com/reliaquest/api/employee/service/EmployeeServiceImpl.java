package com.reliaquest.api.employee.service;

import com.reliaquest.api.client.EmployeeRestApi;
import com.reliaquest.api.dto.EmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeRestClientDto.*;
import com.reliaquest.api.dto.EmployeeRestClientDto.EmployeeListResponse;
import com.reliaquest.api.model.Employee;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Rohit Bothe
 */
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRestApi employeeRestApiService;
    private CircuitBreaker circuitBreaker;

    public EmployeeServiceImpl(EmployeeRestApi employeeRestApiService, CircuitBreakerRegistry registry) {
        this.circuitBreaker = registry.circuitBreaker("employeeApiBreaker");
        this.employeeRestApiService = employeeRestApiService;
    }

    public List<Employee> getAllEmployees() {
        Supplier<List<Employee>> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
            EmployeeListResponse employeeDTO = employeeRestApiService.findAll();
            return employeeDTO.data();
        });

        return decoratedSupplier.get();
    }

    public List<Employee> getAllEmployeesByName(String employeeNameSearch) {
        List<Employee> employeeDTO = getAllEmployees();
        return employeeDTO.stream()
                .filter(emp -> emp.getName().toLowerCase().contains(employeeNameSearch.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Employee getEmployeeById(String id) {
        Supplier<Employee> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
            EmployeeResponse employeeDTO = employeeRestApiService.findById(id);
            return employeeDTO.data();
        });
        return decoratedSupplier.get();
    }

    public Integer getHighestSalaryOfEmployees() {
        List<Employee> employeeDTO = getAllEmployees();
        return employeeDTO.stream()
                .map(Employee::getSalary)
                .max(Comparator.naturalOrder())
                .get();
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
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
        return decoratedSupplier.get();
    }

    public String deleteEmployeeById(String id) {
        Employee emp = getEmployeeById(id);
        String empName = emp.getName();
        employeeRestApiService.delete(new DeleteEmployeeRequest(empName));
        return empName;
    }
}
