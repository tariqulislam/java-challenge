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

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    CacheServiceCheckService cacheServiceCheckService;

    @GetMapping("/employees/cache/{name}")
    public Cache getEmployeeCacheDetails(@PathVariable("name")String name) {
        return cacheServiceCheckService.getCacheByName(name);
    }

    @GetMapping("/employees")
    @ApiImplicitParam(name = "Authorization",
            value = "Access Token",
            required = true,
            allowEmptyValue = false,
            paramType = "header",
            dataTypeClass = String.class,
            example = "Bearer access_token")
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

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

    @PostMapping("/employees")
    @ApiImplicitParam(name = "Authorization",
            value = "Access Token",
            required = true,
            allowEmptyValue = false,
            paramType = "header",
            dataTypeClass = String.class,
            example = "Bearer access_token")
    public ResponseEntity<?> saveEmployee(@RequestBody @Valid EmployeeRequest employeeRequest){
        HashMap<String, Object> map = new HashMap<>();
        Employee updateEmp = null;
        EmployeeResponse employeeResponse = new EmployeeResponse();
        try {
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

    @DeleteMapping("/employees/{employeeId}")
    @ApiImplicitParam(name = "Authorization",
            value = "Access Token",
            required = true,
            allowEmptyValue = false,
            paramType = "header",
            dataTypeClass = String.class,
            example = "Bearer access_token")
    public ResponseEntity<?> deleteEmployee(@PathVariable(name="employeeId") Long employeeId){
        Employee findEmp = employeeService.getEmployee(employeeId);
        HashMap<String, String> map = new HashMap<>();
        EmployeeResponse employeeResponse = new EmployeeResponse();
        if(findEmp != null) {
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
        Employee emp = employeeService.getEmployee(employeeId);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        if(emp != null){
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
