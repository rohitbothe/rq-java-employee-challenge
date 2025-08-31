package com.reliaquest.api.controller;

import com.reliaquest.api.constant.EmployeeConstants;
import com.reliaquest.api.dto.EmployeeRequestDto;
import com.reliaquest.api.employee.service.EmployeeService;
import com.reliaquest.api.model.Employee;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/employee")
public class EmployeeControllerImpl implements IEmployeeController<Employee, EmployeeRequestDto> {

    private static final Logger log = LoggerFactory.getLogger(EmployeeControllerImpl.class);

    @Autowired
    EmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> listEmployees = employeeService.getAllEmployees();
        ResponseEntity<List<Employee>> response = null;
        log.info(EmployeeConstants.INFO_EMPLOYEE_LIST_SIZE, listEmployees.size());
        if (listEmployees.size() > 0) {
            response = new ResponseEntity<>(listEmployees, HttpStatus.FOUND);
        } else {
            log.error(EmployeeConstants.ERROR_NO_LIST);
            response = new ResponseEntity<>(listEmployees, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable("searchString") String searchString) {
        List<Employee> listEmployees = employeeService.getAllEmployeesByName(searchString);
        ResponseEntity<List<Employee>> response = null;
        log.info(EmployeeConstants.INFO_EMPLOYEE_LIST_SIZE, listEmployees.size());
        if (listEmployees.size() > 0) {
            response = new ResponseEntity<>(listEmployees, HttpStatus.FOUND);
        } else {
            log.error(EmployeeConstants.ERROR_NO_LIST);
            response = new ResponseEntity<>(listEmployees, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") String id) {
        Employee employee = employeeService.getEmployeeById(id);
        ResponseEntity<Employee> response = null;
        if (employee != null) {
            response = new ResponseEntity<>(employee, HttpStatus.FOUND);
        } else {
            log.error(EmployeeConstants.ERROR_NO_LIST);
            response = new ResponseEntity<>(employee, HttpStatus.NOT_FOUND);
        }

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
        if (listEmployees.size() > 0) {
            response = new ResponseEntity<>(listEmployees, HttpStatus.FOUND);
        } else {
            log.error(EmployeeConstants.ERROR_NO_LIST);
            response = new ResponseEntity<>(listEmployees, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@RequestBody() EmployeeRequestDto employeeInput) {
        Employee employee = employeeService.createEmployee(employeeInput);

        ResponseEntity<Employee> response = null;
        if (null != employee) {
            log.info(EmployeeConstants.INFO_CREATED);
            response = new ResponseEntity<>(employee, HttpStatus.CREATED);
        } else {
            log.error(EmployeeConstants.ERROR_CREATE_FAILED);
            response = new ResponseEntity<>(employee, HttpStatus.EXPECTATION_FAILED);
        }
        return response;
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(@PathVariable("id") String id) {
        String status = employeeService.deleteEmployeeById(id);
        ResponseEntity<String> response = null;
        if (status != null) {
            log.info(EmployeeConstants.INFO_DELETED);
            response = new ResponseEntity<>(status, HttpStatus.OK);
        } else {
            log.error(EmployeeConstants.ERROR_DELETE_FAILED);
            response = new ResponseEntity<>(EmployeeConstants.ERROR_DELETE_FAILED, HttpStatus.EXPECTATION_FAILED);
        }
        return response;
    }
}
