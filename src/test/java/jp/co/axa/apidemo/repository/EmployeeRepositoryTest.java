package jp.co.axa.apidemo.repository;


import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureBefore(CacheAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test

   public void shouldHaveEmployees() {
        employeeRepository.deleteAll();
        Employee emp = new Employee();
        emp.setSalary(1000);
        emp.setName("Test employee 3");
        emp.setDepartment("DDDD");
        employeeRepository.save(emp);

        Employee emp1 = new Employee();
        emp1.setSalary(1000);
        emp1.setName("Test employee 3");
        emp1.setDepartment("DDDD");
        employeeRepository.save(emp1);

        Employee emp3 = new Employee();
        emp3.setSalary(1000);
        emp3.setName("Test employee 3");
        emp3.setDepartment("DDDD");
        employeeRepository.save(emp3);

        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees.size()).isEqualTo(3);
    }

    @Test

    public  void shouldEmployeeUpdate() {
        employeeRepository.deleteAll();
        Employee emp = new Employee();
        emp.setSalary(1000);
        emp.setName("Test employee 3");
        emp.setDepartment("DDDD");
        Employee saveEmp = employeeRepository.save(emp);

        Employee findEmployee = employeeRepository.findById(saveEmp.getId()).orElse(null);
        assert findEmployee != null;
        Employee updateEmp = new Employee();
        updateEmp.setId(findEmployee.getId());
        updateEmp.setDepartment("MMK");
        updateEmp.setName("Test Update Employee 9");
        updateEmp.setSalary(5000);
        Employee updateEmpInfo = employeeRepository.save(updateEmp);
        assertThat(updateEmpInfo.getName()).isEqualTo("Test Update Employee 9");
        assertThat(updateEmpInfo.getDepartment()).isEqualTo("MMK");
        assertThat(updateEmpInfo.getSalary()).isEqualTo(5000);
    }

    @Test
    public void shouldDeleteEmployee() {
        employeeRepository.deleteAll();
        Employee emp = new Employee();
        emp.setSalary(1000);
        emp.setName("Test employee 3");
        emp.setDepartment("DDDD");
        Employee saveEmp = employeeRepository.save(emp);

        Employee findEmployee = employeeRepository.findById(saveEmp.getId()).orElse(null);
        assert findEmployee != null;
        employeeRepository.delete(findEmployee);

        Employee deletedEmp = employeeRepository.findById(findEmployee.getId()).orElse(null);
        assertThat(deletedEmp).isEqualTo(null);

    }

}
