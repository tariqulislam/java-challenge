package jp.co.axa.apidemo.service;

import static org.assertj.core.api.Assertions.assertThat;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class EmployeeServiceTest {

    @Autowired
    public EmployeeRepository employeeRepository;
    @Autowired
    public EmployeeService employeeService;
    @Test
    public void getEmployeeByTesting() {
        System.out.println("Should employee Save");
        Employee emp = new Employee();
        emp.setSalary(1000);
        emp.setName("Test employee 1");
        emp.setDepartment("DDSK");
        Employee saveEmp = employeeRepository.save(emp);
        // Find Employee
        Employee searchEmp = employeeService.getEmployee(saveEmp.getId());
        assertThat(searchEmp).isNotNull();
        assertThat(searchEmp.getName()).isEqualTo("Test employee 1");
        assertThat(searchEmp.getId()).isNotNull();
        assertThat(searchEmp.getDepartment()).isEqualTo("DDSK");

    }

    @Test
    public void  shouldEmployeeSavedIntoDatabaseByService() {
        System.out.println("Employee Should Saved by Service");
        Employee emp = new Employee();
        emp.setSalary(1000);
        emp.setName("Test employee 2");
        emp.setDepartment("MMK");

        Employee savedEmp = employeeService.saveEmployee(emp);
        assertThat(savedEmp).isNotNull();
        assertThat(savedEmp.getName()).isEqualTo("Test employee 2");
        assertThat(savedEmp.getId()).isNotNull();
        assertThat(savedEmp.getDepartment()).isEqualTo("MMK");
    }

    @Test
    public void  shouldEmployeeDelete() {
        System.out.println("Employee Should Saved by Service");
        Employee emp = new Employee();
        emp.setSalary(1000);
        emp.setName("Test employee 3");
        emp.setDepartment("DDDD");
        Employee saveEmp = employeeRepository.save(emp);

        // Employee Delete
        boolean empDelete = employeeService.deleteEmployee(saveEmp.getId());
        assertThat(empDelete).isTrue();

    }

    @Test
    public void  shouldEmployeeModified() {
        System.out.println("Employee Should Saved by Service");
        Employee emp = new Employee();
        emp.setSalary(1000);
        emp.setName("Test employee 4");
        emp.setDepartment("DDDD");
        Employee saveEmp = employeeRepository.save(emp);

        saveEmp.setDepartment("MMMK");
        saveEmp.setName("Test Employee 5");
        saveEmp.setSalary(90000);
        // Employee Delete
        boolean empUpdate = employeeService.updateEmployee(saveEmp);
        assertThat(empUpdate).isTrue();

        Employee searchEmp = employeeService.getEmployee(saveEmp.getId());
        assertThat(searchEmp).isNotNull();
        assertThat(searchEmp.getName()).isEqualTo("Test Employee 5");
        assertThat(searchEmp.getId()).isNotNull();
        assertThat(searchEmp.getDepartment()).isEqualTo("MMMK");
        assertThat(searchEmp.getSalary()).isEqualTo(90000);
    }
}
