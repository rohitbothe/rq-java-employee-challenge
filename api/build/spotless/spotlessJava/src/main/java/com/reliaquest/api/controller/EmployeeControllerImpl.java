package com.reliaquest.api.controller;

import com.reliaquest.api.constant.EmployeeConstants;
import com.reliaquest.api.dto.EmployeeRequestDto;
import com.reliaquest.api.employee.service.EmployeeService;
import com.reliaquest.api.model.Employee;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeControllerImpl implements IEmployeeController<Employee, EmployeeRequestDto> {

    private static final Logger log = LoggerFactory.getLogger(EmployeeControllerImpl.class);
    private EmployeeService employeeService;

    public EmployeeControllerImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> listEmployees = employeeService.getAllEmployees();
        ResponseEntity<List<Employee>> response = null;
        log.info(EmployeeConstants.INFO_EMPLOYEE_LIST_SIZE, listEmployees.size());
        response = new ResponseEntity<>(listEmployees, HttpStatus.OK);
        return response;
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable("searchString") String searchString) {
        List<Employee> listEmployees = employeeService.getAllEmployeesByName(searchString);
        ResponseEntity<List<Employee>> response = null;
        log.info(EmployeeConstants.INFO_EMPLOYEE_LIST_SIZE, listEmployees.size());
        response = new ResponseEntity<>(listEmployees, HttpStatus.OK);
        return response;
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") String id) {
        Employee employee = employeeService.getEmployeeById(id);
        ResponseEntity<Employee> response = null;
        log.info("GetEmployeeById", id);
        response = new ResponseEntity<>(employee, HttpStatus.OK);
        return response;
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        Integer higestSalary = employeeService.getHighestSalaryOfEmployees();
        log.info(EmployeeConstants.INFO_HIGEST_SALARY, higestSalary);
        ResponseEntity<Integer> response = new ResponseEntity<>(higestSalary, HttpStatus.OK);
        return response;
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> listEmployees = employeeService.getTopTenHighestEarningEmployeeNames();
        ResponseEntity<List<String>> response = null;
        log.info("GetTopTenHighestEarningEmployeeNames");
        response = new ResponseEntity<>(listEmployees, HttpStatus.OK);
        return response;
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@RequestBody() EmployeeRequestDto employeeInput) {
        Employee employee = employeeService.createEmployee(employeeInput);
        log.info(EmployeeConstants.INFO_CREATED);
        ResponseEntity<Employee> response = null;
        response = new ResponseEntity<>(employee, HttpStatus.OK);
        return response;
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(@PathVariable("id") String id) {
        String status = employeeService.deleteEmployeeById(id);
        ResponseEntity<String> response = null;
        log.info(EmployeeConstants.INFO_DELETED);
        response = new ResponseEntity<>(status, HttpStatus.NO_CONTENT);
        return response;
    }
}
