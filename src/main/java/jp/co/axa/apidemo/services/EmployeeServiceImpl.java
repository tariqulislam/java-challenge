package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Cacheable(value = "employee-list")
    public List<Employee> retrieveEmployees() {
       System.out.println("employee list action is calling.... ");
       return  employeeRepository.findAll();
    }

    @Cacheable(value = "employee-info" , key = "#id")
    public Employee getEmployee(Long id) {
        System.out.println("employee find action is calling.... ");
        Optional<Employee> optEmp = employeeRepository.findById(id);
        return optEmp.orElse(null);

    }

    @CachePut(value = "employee-info", key = "#employee.id")
    @CacheEvict(value = "employee-list", allEntries = true)
    public Employee saveEmployee(Employee employee){
        System.out.println("employee save action is calling.... ");
        return employeeRepository.save(employee);
    }


   @Caching(evict = {
           @CacheEvict(value = "employee-info", key = "#id"),
           @CacheEvict(value = "employee-list", allEntries = true)
   })
    public boolean deleteEmployee(Long id){
       System.out.println("employee delete action is calling.... ");
        try  {
            employeeRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        }

    }

    @Caching(evict = {
            @CacheEvict(value = "employee-info", key = "#employee.id"),
            @CacheEvict(value = "employee-list", allEntries = true)
    })
    public boolean updateEmployee(Employee employee) {
        System.out.println("employee update action is calling.... ");
        try {
            Employee emp = employeeRepository.findById(employee.getId()).orElse(null);
            if (emp != null) {
                emp.setName(employee.getName());
                emp.setSalary(employee.getSalary());
                emp.setDepartment(employee.getDepartment());
                employeeRepository.save(emp);
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        }

    }
}