package com.reliaquest.api.client;

import com.reliaquest.api.dto.EmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeRestClientDto.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author Rohit Bothe
 */
@HttpExchange(url = "api/v1/employee", accept = "application/json", contentType = "application/json")
public interface EmployeeRestApi {

    @GetExchange()
    EmployeeListResponse findAll();

    @GetExchange("/{id}")
    EmployeeResponse findById(@PathVariable("id") String id);

    @PostExchange()
    EmployeeResponse create(@RequestBody EmployeeRequestDto employeeRequest);

    @DeleteExchange()
    DeleteEmployeeResponse delete(@RequestBody DeleteEmployeeRequest deleteEmployeeRequest);
}
