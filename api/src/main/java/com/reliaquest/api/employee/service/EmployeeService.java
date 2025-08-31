package com.reliaquest.api.employee.service;

import com.reliaquest.api.dto.EmployeeRequestDto;
import com.reliaquest.api.model.Employee;
import java.util.List;

public interface EmployeeService {

    public List<Employee> getAllEmployees();

    public List<Employee> getAllEmployeesByName(String employeeNameSearch);

    public Employee getEmployeeById(String id);

    public Integer getHighestSalaryOfEmployees();

    public List<String> getTopTenHighestEarningEmployeeNames();

    public Employee createEmployee(EmployeeRequestDto employeeRequest);

    public String deleteEmployeeById(String id);
}
