/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.EmployeeBO;
import com.original.evaluate.entity.Employee;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author kanehe
 */
@Named(value="employeeBean")
@SessionScoped
public class EmployeeBean implements Serializable {
    
    private EmployeeBO employeeBO;
    
    private List<Employee> employees;
    private List<Employee> selectedEmployees = null;
    private Employee editEmployee;

    public List<Employee> getSelectedEmployees() {
        return selectedEmployees;
    }

    public void setSelectedEmployees(List<Employee> selectedEmployees) {
        this.selectedEmployees = selectedEmployees;
    }    

    public Employee getEditEmployee() {
        return editEmployee;
    }

    public void setEditEmployee(Employee editEmployee) {
        this.editEmployee = editEmployee;
    }

    public EmployeeBO getEmployeeBO() {
        return employeeBO;
    }

    public void setEmployeeBO(EmployeeBO employeeBO) {
        this.employeeBO = employeeBO;
    }
    
    public EmployeeBean() throws NamingException {
        employeeBO = new EmployeeBO();
        employees = employeeBO.getAllEmployeeList();
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;   
    }
    
    public void update(Employee employee) throws Exception{
        employeeBO.save(employee);        
//        employees = employeeBO.getAllEmployeeList();
    }
}
