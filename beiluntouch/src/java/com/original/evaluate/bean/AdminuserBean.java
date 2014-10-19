/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.CategoryBO;
import com.original.evaluate.bo.DepartmentBO;
import com.original.evaluate.bo.EmployeeBO;
import com.original.evaluate.bo.AdminuserBO;
import com.original.evaluate.entity.Category;
import com.original.evaluate.entity.Department;
import com.original.evaluate.entity.Employee;
import com.original.evaluate.entity.Adminuser;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author kanehe
 */
@Named(value="adminuserBean")
@SessionScoped
public class AdminuserBean implements Serializable {
    
    private AdminuserBO adminUserBO;
    private DepartmentBO departmentBO;
    
    private List<Adminuser> adminUsers;
    private List<Adminuser> selectedAdminusers = null;
    private List<Department> departments;
    private Adminuser editAdminuser;
    
    private int editDepartmentID;
    private String editName;
    private String editPassword;
    
    private Employee editEmployee;

    public List<Adminuser> getSelectedAdminusers() {
        return selectedAdminusers;
    }

    public int getEditDepartmentID() {
        return editDepartmentID;
    }

    public void setEditDepartmentID(int editDepartmentID) {
        this.editDepartmentID = editDepartmentID;
    }

    public String getEditName() {
        return editName;
    }

    public void setEditName(String editName) {
        this.editName = editName;
    }

    public String getEditPassword() {
        return editPassword;
    }

    public void setEditPassword(String editPassword) {
        this.editPassword = editPassword;
    }

    public Employee getEditEmployee() {
        return editEmployee;
    }

    public void setEditEmployee(Employee editEmployee) {
        this.editEmployee = editEmployee;
    }
    
    public void setSelectedAdminusers(List<Adminuser> selectedAdminusers) {
        this.selectedAdminusers = selectedAdminusers;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
    
    public Adminuser getEditAdminuser() {
        return editAdminuser;
    }

    public void setEditAdminuser(Adminuser editAdminuser) {
        this.editAdminuser = editAdminuser;
    }

    public AdminuserBO getAdminuserBO() {
        return adminUserBO;
    }

    public void setAdminuserBO(AdminuserBO adminUserBO) {
        this.adminUserBO = adminUserBO;
    }
    
    public AdminuserBean() throws NamingException {
        adminUserBO = new AdminuserBO();
        departmentBO = new DepartmentBO();
        
        editAdminuser = new Adminuser();
        
        adminUsers = adminUserBO.getAllAdminuserList();
        departments = departmentBO.getAllDepartmentList();
    }

    public List<Adminuser> getAdminusers() {
        return adminUsers;
    }

    public void setAdminusers(List<Adminuser> adminUsers) {
        this.adminUsers = adminUsers;   
    }
    
    public void update() throws Exception{
        editAdminuser.setDepartment(departmentBO.getDepartmentById(editDepartmentID));
        
        adminUserBO.save(editAdminuser);
        adminUsers = adminUserBO.getAllAdminuserList();
    }
    
    public void create() throws Exception{
        editAdminuser.setDepartment(departmentBO.getDepartmentById(editDepartmentID));
        
        adminUserBO.create(editAdminuser);
        adminUsers = adminUserBO.getAllAdminuserList();
    }
    
    public void delete() throws Exception{
        for(Adminuser adminUser:selectedAdminusers){
            adminUserBO.delete(adminUser);
        }
        adminUsers = adminUserBO.getAllAdminuserList();
    }
    
    public void refresh() throws NamingException{
        adminUsers = adminUserBO.getAllAdminuserList();
    }
    
    public void prepareCreate() {
        editAdminuser = new Adminuser();
        editDepartmentID=-1;
        editName="";
        editPassword="";
    }
}
