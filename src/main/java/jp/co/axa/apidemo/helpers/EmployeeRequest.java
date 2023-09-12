package jp.co.axa.apidemo.helpers;


import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class EmployeeRequest {
    public EmployeeRequest(String name, Integer salary, String department) {
        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    public EmployeeRequest() {}


    @NotNull
    @Length(min = 1, max = 100)
    private String name;

    private Integer salary;

    @NotNull
    @Length(min = 1, max = 100)
    private String department;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
