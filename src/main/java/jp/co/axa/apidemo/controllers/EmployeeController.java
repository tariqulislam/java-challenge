package jp.co.axa.apidemo.controllers;

import io.swagger.annotations.ApiImplicitParam;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.helpers.EmployeeRequest;
import jp.co.axa.apidemo.helpers.EmployeeResponse;
import jp.co.axa.apidemo.services.CacheServiceCheckService;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* Rest api Controller for Employee */
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    CacheServiceCheckService cacheServiceCheckService;

    /* Api end point for check the employee cache by name
    using cache manager from CacheServiceCheckService
     Need Access token  to access this api endpoint */
    @GetMapping("/employees/cache/{name}")
    @ApiImplicitParam(name = "Authorization",
            value = "Access Token",
            required = true,
            allowEmptyValue = false,
            paramType = "header",
            dataTypeClass = String.class,
            example = "Bearer access_token")
    public Cache getEmployeeCacheDetails(@PathVariable("name")String name) {
        return cacheServiceCheckService.getCacheByName(name);
    }

    /* Api Endpoint for access the employee list information
    * Need Access token for access api endpoint */
    @GetMapping("/employees")
    @ApiImplicitParam(name = "Authorization",
            value = "Access Token",
            required = true,
            allowEmptyValue = false,
            paramType = "header",
            dataTypeClass = String.class,
            example = "Bearer access_token")
    public List<Employee> getEmployees() {
        return employeeService.retrieveEmployees();
    }
    /* API endpoint for access the single employee information
    * Need Access token to access */
    @GetMapping("/employees/{employeeId}")
    @ApiImplicitParam(name = "Authorization",
            value = "Access Token",
            required = true,
            allowEmptyValue = false,
            paramType = "header",
            dataTypeClass = String.class,
            example = "Bearer access_token")
    public Employee getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    /* API endpoint for save the employee information
    * Access Token is Required */
    @PostMapping("/employees")
    @ApiImplicitParam(name = "Authorization",
            value = "Access Token",
            required = true,
            allowEmptyValue = false,
            paramType = "header",
            dataTypeClass = String.class,
            example = "Bearer access_token")
    public ResponseEntity<?> saveEmployee(@RequestBody @Valid EmployeeRequest employeeRequest){
        Employee updateEmp = null;
        /* Using for create the entity response for api */
        EmployeeResponse employeeResponse = new EmployeeResponse();
        try {
            /* create the employee object and employeeService to save the employee into database by service */
           Employee  savedEmployee = new Employee();
           savedEmployee.setName(employeeRequest.getName());
           savedEmployee.setDepartment(employeeRequest.getDepartment());
           savedEmployee.setSalary(employeeRequest.getSalary());
           updateEmp = employeeService.saveEmployee(savedEmployee);

           employeeResponse.setStatus("success");
           employeeResponse.setMsg("Employee Saved Successfully");
           employeeResponse.setEmployee(updateEmp);

        } catch (Exception ex) {
            employeeResponse.setStatus("error");
            employeeResponse.setMsg(ex.getLocalizedMessage());
            employeeResponse.setEmployee(updateEmp);
        }
        return ResponseEntity.ok().body(employeeResponse);

    }

    /* API endpoint for delete the employee  and need access token */
    @DeleteMapping("/employees/{employeeId}")
    @ApiImplicitParam(name = "Authorization",
            value = "Access Token",
            required = true,
            allowEmptyValue = false,
            paramType = "header",
            dataTypeClass = String.class,
            example = "Bearer access_token")
    public ResponseEntity<?> deleteEmployee(@PathVariable(name="employeeId") Long employeeId){
        /* Find the existing employee from database by employee service */
        Employee findEmp = employeeService.getEmployee(employeeId);
        HashMap<String, String> map = new HashMap<>();
        EmployeeResponse employeeResponse = new EmployeeResponse();
        if(findEmp != null) {
            /* Run the delete operation by employee service delete method */
            boolean isDeleteEmp =  employeeService.deleteEmployee(employeeId);
            if (isDeleteEmp) {
                employeeResponse.setStatus("success");
                employeeResponse.setMsg("Employee Delete successfully");
                employeeResponse.setEmployee(null);
            } else {
                employeeResponse.setStatus("error");
                employeeResponse.setMsg("Something Went Wrong During Deleting the Employee. Employee Id:" + employeeId);
                employeeResponse.setEmployee(null);
            }

        } else {
            employeeResponse.setStatus("error");
            employeeResponse.setMsg("There is on employee related to give id" + employeeId);
            employeeResponse.setEmployee(null);
        }
        return ResponseEntity.ok().body(employeeResponse);
    }
    /* API endpoint for  update existing employee. Access token is required */
    @PutMapping("/employees/{employeeId}")
    @ApiImplicitParam(name = "Authorization",
            value = "Access Token",
            required = true,
            allowEmptyValue = false,
            paramType = "header",
            dataTypeClass = String.class,
            example = "Bearer access_token")
    public ResponseEntity<?> updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        /* Find the existing employee by employee service */
        Employee emp = employeeService.getEmployee(employeeId);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        if(emp != null){
            /* update the employee by employee service update method */
           boolean isUpdateEmp = employeeService.updateEmployee(employee);
           if(isUpdateEmp) {
               employeeResponse.setStatus("success");
               employeeResponse.setMsg("Employee Update Successfully");
               employeeResponse.setEmployee(null);

           } else {
               employeeResponse.setStatus("error");
               employeeResponse.setMsg("Something Went Wrong During Updating the Employee");
               employeeResponse.setEmployee(null);
           }
        } else {
            employeeResponse.setStatus("error");
            employeeResponse.setMsg("Employee not found.");
            employeeResponse.setEmployee(null);
        }

        return ResponseEntity.ok().body(employeeResponse);

    }

}
