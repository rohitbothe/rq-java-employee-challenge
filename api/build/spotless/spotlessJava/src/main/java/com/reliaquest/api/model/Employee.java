package com.reliaquest.api.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rohit Bothe
 */
@Data
@Builder
@EqualsAndHashCode
// @JsonInclude(JsonInclude.Include.NON_NULL)
// @JsonNaming(PrefixStrategy.class)
public class Employee {

    private UUID id;
    private String name;
    private Integer salary;
    private Integer age;
    private String title;
    private String email;

    public Employee() {
        super();
    }

    public Employee(UUID id, String name, Integer salary, Integer age, String title, String email) {
        super();
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.title = title;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
