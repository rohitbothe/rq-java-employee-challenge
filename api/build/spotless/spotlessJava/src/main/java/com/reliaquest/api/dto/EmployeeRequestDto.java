package com.reliaquest.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

/**
 * @author Rohit Bothe
 */
@Data
@Builder
public class EmployeeRequestDto {

    public EmployeeRequestDto() {
        super();
        // TODO Auto-generated constructor stub
    }

    public EmployeeRequestDto(
            @NotBlank String name,
            @Positive @NotNull Integer salary,
            @Min(12) @Max(80) @NotNull Integer age,
            @NotBlank String title,
            String email) {
        super();
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.title = title;
        this.email = email;
    }

    @NotBlank
    private String name;

    @Positive @NotNull private Integer salary;

    @Min(12)
    @Max(80)
    @NotNull private Integer age;

    @NotBlank
    private String title;

    private String email;

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
