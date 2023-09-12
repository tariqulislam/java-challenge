package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
* Employee Related Business logic for repository
* Cached Database to this service
* Handle most of the repository logic for Employee
 */
@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    /* Method for return the list of employee and cached the employees into employee list cached
    * Hash based Native Cached applied*/
    @Cacheable(value = "employee-list")
    public List<Employee> retrieveEmployees() {
       System.out.println("employee list action is calling.... ");
       return  employeeRepository.findAll();
    }

    /* Hash based Native Cached applied
    * Method for return single employee and cached with employee id */
    @Cacheable(value = "employee-info" , key = "#id")
    public Employee getEmployee(Long id) {
        System.out.println("employee find action is calling.... ");
        Optional<Employee> optEmp = employeeRepository.findById(id);
        return optEmp.orElse(null);

    }

    /* Service for Saved the employee into database
    * Remove all employee list cached and add new the cached employee info and employee list for Hashed native cache by
    */
    @CachePut(value = "employee-info", key = "#employee.id")
    @CacheEvict(value = "employee-list", allEntries = true)
    public Employee saveEmployee(Employee employee){
        System.out.println("employee save action is calling.... ");
        return employeeRepository.save(employee);
    }

   /* Service for delete the employee from database  and evict all the cached employee */
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

    /* Service for update the employee information
    Update the cached employee by id during update and evict the all employee list cache*/
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