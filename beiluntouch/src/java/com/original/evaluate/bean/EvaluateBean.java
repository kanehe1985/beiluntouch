/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.EvaluateBO;
import com.original.evaluate.entity.Employee;
import com.original.util.MessageUtil;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.NamingException;

/**
 *
 * @author Administrator
 */
@ManagedBean
@SessionScoped
public class EvaluateBean implements Serializable {

    private List<Employee> employeeList;
    private EvaluateBO evaluateBO;
    private Employee selectedEmployee;
    /**
     * Creates a new instance of EvaluateBean
     */
    public EvaluateBean() {
        try {
            evaluateBO = new EvaluateBO();
            employeeList = evaluateBO.getAllEmployeeList();
        } catch (NamingException ex) {
            MessageUtil.createFacesMessage("", "");
        }
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Employee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }
    
    public void startEvaluate(Employee employee){
        selectedEmployee = employee;
    }
    
}
