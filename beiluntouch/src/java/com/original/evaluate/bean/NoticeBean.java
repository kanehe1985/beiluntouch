/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.CategoryBO;
import com.original.evaluate.bo.DepartmentBO;
import com.original.evaluate.bo.EmployeeBO;
import com.original.evaluate.bo.NoticeBO;
import com.original.evaluate.entity.Category;
import com.original.evaluate.entity.Department;
import com.original.evaluate.entity.Employee;
import com.original.evaluate.entity.Notice;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author kanehe
 */
@Named(value="noticeBean")
@SessionScoped
public class NoticeBean implements Serializable {
    
    private NoticeBO noticeBO;
    private DepartmentBO departmentBO;
    private CategoryBO categoryBO;
    private EmployeeBO employeeBO;
    
    private List<Notice> notices;
    private List<Notice> selectedNotices = null;
    private List<Department> departments;
    private List<Category> categorys;
    private List<Employee> employees;
    private Notice editNotice;
    
    private int editDepartmentID;
    private int editCategoryID;
    private int editEmployeeID;

    public List<Notice> getSelectedNotices() {
        return selectedNotices;
    }

    public int getEditDepartmentID() {
        return editDepartmentID;
    }

    public void setEditDepartmentID(int editDepartmentID) {
        this.editDepartmentID = editDepartmentID;
    }

    public int getEditCategoryID() {
        return editCategoryID;
    }

    public void setEditCategoryID(int editCategoryID) {
        this.editCategoryID = editCategoryID;
    }

    public int getEditEmployeeID() {
        return editEmployeeID;
    }

    public void setEditEmployeeID(int editEmployeeID) {
        this.editEmployeeID = editEmployeeID;
    }
    
    public void setSelectedNotices(List<Notice> selectedNotices) {
        this.selectedNotices = selectedNotices;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Category> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<Category> categorys) {
        this.categorys = categorys;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }   
    
    public Notice getEditNotice() {
        return editNotice;
    }

    public void setEditNotice(Notice editNotice) {
        this.editNotice = editNotice;
    }

    public NoticeBO getNoticeBO() {
        return noticeBO;
    }

    public void setNoticeBO(NoticeBO noticeBO) {
        this.noticeBO = noticeBO;
    }
    
    public NoticeBean() throws NamingException {
        noticeBO = new NoticeBO();
        departmentBO = new DepartmentBO();
        categoryBO = new CategoryBO();
        employeeBO = new EmployeeBO();
        
        editNotice = new Notice();
        
        notices = noticeBO.getAllNoticeList();
        departments = departmentBO.getAllDepartmentList();
        categorys = categoryBO.getAllCategoryList();
        employees = employeeBO.getAllEmployeeList();
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public void setNotices(List<Notice> notices) {
        this.notices = notices;   
    }
    
    public void update() throws Exception{
        editNotice.setDepartment(departmentBO.getDepartmentById(editDepartmentID));
        editNotice.setCategory(categoryBO.getCategoryById(editCategoryID));
        editNotice.setEmployee(employeeBO.getEmployeeById(editEmployeeID));
        
        noticeBO.save(editNotice);
        notices = noticeBO.getAllNoticeList();
    }
    
    public void create() throws Exception{
        editNotice.setDepartment(departmentBO.getDepartmentById(editDepartmentID));
        editNotice.setCategory(categoryBO.getCategoryById(editCategoryID));
        editNotice.setEmployee(employeeBO.getEmployeeById(editEmployeeID));
        
        noticeBO.create(editNotice);
        notices = noticeBO.getAllNoticeList();
    }
    
    public void delete() throws Exception{
        for(Notice notice:selectedNotices){
            noticeBO.delete(notice);
        }
        notices = noticeBO.getAllNoticeList();
    }
    
    public void refresh() throws NamingException{
        notices = noticeBO.getAllNoticeList();
    }
    
    public void prepareCreate() {
        editNotice = new Notice();
        editDepartmentID=-1;
        editCategoryID=-1;
        editEmployeeID=-1;
    }
}
