package com.reliaquest.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.reliaquest.api.dto.EmployeeRequestDto;
import com.reliaquest.api.employee.service.EmployeeService;
import com.reliaquest.api.model.Employee;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author Rohit Bothe
 */
@WebMvcTest(EmployeeControllerImpl.class)
public class EmployeeControllerImplTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @Test
    public void getAllEmployeesFound() throws Exception {
        List<Employee> list = new ArrayList<>();

        Employee emp = new Employee(UUID.randomUUID(), "Rohit", 6543, 35, "Manager", "rbothe1@visa.com");
        Employee emp1 = new Employee(UUID.randomUUID(), "Tiger Nixon", 320800, 25, "Manager", "rbothe1@visa.com");
        Employee emp2 = new Employee(UUID.randomUUID(), "Garrett Winters", 170750, 35, "Manager", "rbothe1@visa.com");
        Employee emp3 = new Employee(UUID.randomUUID(), "Ashton Cox", 86000, 45, "Manager", "rbothe1@visa.com");
        Employee emp4 = new Employee(UUID.randomUUID(), "Cedric Kelly", 433060, 55, "Manager", "rbothe1@visa.com");
        Employee emp5 = new Employee(UUID.randomUUID(), "Airi Satou", 162700, 30, "Manager", "rbothe1@visa.com");

        list.add(emp);
        list.add(emp1);
        list.add(emp2);
        list.add(emp3);
        list.add(emp4);
        list.add(emp5);

        when(service.getAllEmployees()).thenReturn(list);
        this.mockMvc.perform(get("/employee")).andExpect(status().isOk());
    }

    @Test
    public void getAllEmployeesNotFound() throws Exception {
        List<Employee> list = new ArrayList<>();

        when(service.getAllEmployees()).thenReturn(list);
        this.mockMvc.perform(get("/employee")).andExpect(status().isOk());
    }

    @Test
    public void getEmployeesByNameSearchFound() throws Exception {
        List<Employee> list = new ArrayList<>();

        Employee emp = new Employee(UUID.randomUUID(), "Rohit", 6543, 35, "Manager", "rbothe1@visa.com");
        Employee emp1 = new Employee(UUID.randomUUID(), "Tiger Nixon", 320800, 25, "Manager", "rbothe1@visa.com");
        Employee emp2 = new Employee(UUID.randomUUID(), "Garrett Winters", 170750, 35, "Manager", "rbothe1@visa.com");
        Employee emp3 = new Employee(UUID.randomUUID(), "Ashton Cox", 86000, 45, "Manager", "rbothe1@visa.com");
        Employee emp4 = new Employee(UUID.randomUUID(), "Cedric Kelly", 433060, 55, "Manager", "rbothe1@visa.com");
        Employee emp5 = new Employee(UUID.randomUUID(), "Airi Satou", 162700, 30, "Manager", "rbothe1@visa.com");

        list.add(emp);
        list.add(emp1);
        list.add(emp2);
        list.add(emp3);
        list.add(emp4);
        list.add(emp5);

        when(service.getAllEmployeesByName("Ashton")).thenReturn(list);
        this.mockMvc.perform(get("/employee/search/Ashton")).andExpect(status().isOk());
    }

    @Test
    public void getEmployeesByNameSearchNotFound() throws Exception {
        List<Employee> list = new ArrayList<>();

        when(service.getAllEmployeesByName("Berry")).thenReturn(list);
        this.mockMvc.perform(get("/employee/search/Berry")).andExpect(status().isOk());
    }

    @Test
    public void getEmployeeByIdFound() throws Exception {
        Employee emp5 = new Employee(UUID.randomUUID(), "Airi Satou", 162700, 30, "Manager", "rbothe1@visa.com");

        when(service.getEmployeeById("5")).thenReturn(emp5);
        this.mockMvc.perform(get("/employee/5")).andExpect(status().isOk());
    }

    @Test
    public void getEmployeeByIdNotFound() throws Exception {

        when(service.getEmployeeById("23")).thenReturn(null);
        this.mockMvc.perform(get("/employee/23")).andExpect(status().isOk());
    }

    @Test
    public void getHighestSalaryOfEmployees() throws Exception {

        when(service.getHighestSalaryOfEmployees()).thenReturn(433060);
        this.mockMvc.perform(get("/employee/highestSalary")).andExpect(status().isOk());
    }

    @Test
    public void getTopTenHighestEarningEmployeeNamesFound() throws Exception {
        List<String> list = new ArrayList<>();
        list.add("Tiger Nixon");
        list.add("Garrett Winters");
        list.add("Ashton Cox");
        list.add("Cedric Kelly");
        list.add("Airi Satou");
        list.add("Tiger Nixon2");
        list.add("Garrett Winters2");
        list.add("Ashton Cox2");
        list.add("Cedric Kelly2");
        list.add("Airi Satou2");

        when(service.getTopTenHighestEarningEmployeeNames()).thenReturn(list);
        this.mockMvc.perform(get("/employee/topTenHighestEarningEmployeeNames")).andExpect(status().isOk());
    }

    @Test
    public void getTopTenHighestEarningEmployeeNamesNone() throws Exception {
        List<String> list = new ArrayList<>();

        when(service.getTopTenHighestEarningEmployeeNames()).thenReturn(list);
        this.mockMvc.perform(get("/employee/topTenHighestEarningEmployeeNames")).andExpect(status().isOk());
    }

    @Test
    public void createEmployeeFailed() throws Exception {
        Employee employee = new Employee(UUID.randomUUID(), "Tiger Nixon", 320800, 25, "Manager", "rbothe1@visa.com");
        EmployeeRequestDto request = new EmployeeRequestDto("Tiger Nixon", 320800, 25, "Manager", "rbothe1@visa.com");
        when(service.createEmployee(null)).thenReturn(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteEmployeeById() throws Exception {
        when(service.deleteEmployeeById("5")).thenReturn("Airi Satou");
        this.mockMvc.perform(delete("/employee/5")).andExpect(status().isNoContent());
    }

    @Test
    public void deleteEmployeeByIdFailed() throws Exception {
        when(service.deleteEmployeeById("51")).thenReturn(null);
        this.mockMvc.perform(delete("/employee/51")).andExpect(status().isNoContent());
    }
}
