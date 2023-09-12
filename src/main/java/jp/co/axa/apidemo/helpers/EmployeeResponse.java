package jp.co.axa.apidemo.helpers;

import io.swagger.models.auth.In;
import jp.co.axa.apidemo.entities.Employee;

public class EmployeeResponse {
    private String status;
    private  String msg;

    public EmployeeResponse() {
    }

    public EmployeeResponse(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public EmployeeResponse(String status, String msg, Employee employee) {
        this.status = status;
        this.msg = msg;
        this.employee = employee;
    }

    private Employee employee;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
