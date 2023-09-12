package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface EmployeeService {
    public List<Employee> retrieveEmployees();

    public Employee getEmployee(Long employeeId);

    public Employee saveEmployee(Employee employee);

    public boolean deleteEmployee(Long employeeId);

    public boolean updateEmployee(Employee employee);
}