/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.DepartmentBO;
import com.original.evaluate.entity.Department;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author kanehe
 */
@Named(value="departmentBean")
@SessionScoped
public class DepartmentBean implements Serializable {
    
    private DepartmentBO departmentBO;
    
    private List<Department> departments;
    private List<Department> selectedDepartments = null;
    private Department editDepartment;

    public List<Department> getSelectedDepartments() {
        return selectedDepartments;
    }

    public void setSelectedDepartments(List<Department> selectedDepartments) {
        this.selectedDepartments = selectedDepartments;
    }    

    public Department getEditDepartment() {
        return editDepartment;
    }

    public void setEditDepartment(Department editDepartment) {
        this.editDepartment = editDepartment;
    }

    public DepartmentBO getDepartmentBO() {
        return departmentBO;
    }

    public void setDepartmentBO(DepartmentBO departmentBO) {
        this.departmentBO = departmentBO;
    }
    
    public DepartmentBean() throws NamingException {
        departmentBO = new DepartmentBO();
        departments = departmentBO.getAllDepartmentList();
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;   
    }
    
    public void update() throws Exception{
        departmentBO.save(editDepartment);        
        departments = departmentBO.getAllDepartmentList();
    }
    
    public void create() throws Exception{
        departmentBO.create(editDepartment);        
        departments = departmentBO.getAllDepartmentList();
    }
    
    public void delete() throws Exception{
        for(Department department:selectedDepartments){
            departmentBO.delete(department);
        }
        departments = departmentBO.getAllDepartmentList();
    }
    
    public void refresh() throws NamingException{
        departments = departmentBO.getAllDepartmentList();
    }
    
    public void prepareCreate() {
        editDepartment = new Department();
    }
}
