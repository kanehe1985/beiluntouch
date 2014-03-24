/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author dxx
 */
@Named(value = "demoBean")
@SessionScoped
public class DemoBean implements Serializable {

    private List<Employee> employeeList;

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public DemoBean() {
        employeeList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            Employee employee = new Employee();
            employee.setName("员工" + i);
            employee.setPhotoFileName("images:" + employee.getName() + ".jpg");
            employeeList.add(employee);
        }
    }
}
