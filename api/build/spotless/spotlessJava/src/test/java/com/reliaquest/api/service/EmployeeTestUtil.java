package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Rohit Bothe
 */
public class EmployeeTestUtil {

    public static List<Employee> getEmployeesList() {
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

        return list;
    }

    public static List<Employee> getEmployeesList(Integer highestSalary) {
        List<Employee> list = new ArrayList<>();

        Employee emp = new Employee(UUID.randomUUID(), "Rohit", 6543, 35, "Manager", "rbothe1@visa.com");
        Employee emp1 = new Employee(UUID.randomUUID(), "Tiger Nixon", 320800, 25, "Manager", "rbothe1@visa.com");
        Employee emp2 = new Employee(UUID.randomUUID(), "Garrett Winters", 170750, 35, "Manager", "rbothe1@visa.com");
        Employee emp3 = new Employee(UUID.randomUUID(), "Ashton Cox", 86000, 45, "Manager", "rbothe1@visa.com");
        Employee emp4 = new Employee(UUID.randomUUID(), "Cedric Kelly", 433060, 55, "Manager", "rbothe1@visa.com");
        Employee emp5 = new Employee(UUID.randomUUID(), "Airi Satou", highestSalary, 30, "Manager", "rbothe1@visa.com");

        list.add(emp);
        list.add(emp1);
        list.add(emp2);
        list.add(emp3);
        list.add(emp4);
        list.add(emp5);

        return list;
    }
}
