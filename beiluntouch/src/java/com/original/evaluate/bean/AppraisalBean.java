/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.AppraisalBO;
import com.original.evaluate.bo.AppraisallevelBO;
import com.original.evaluate.bo.CategoryBO;
import com.original.evaluate.bo.DepartmentBO;
import com.original.evaluate.entity.Appraisal;
import com.original.evaluate.entity.Appraisallevel;
import com.original.evaluate.entity.Category;
import com.original.evaluate.entity.Department;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author kanehe
 */
@Named(value="appraisalBean")
@SessionScoped
public class AppraisalBean implements Serializable {
    
    private AppraisalBO appraisalBO;
    private AppraisallevelBO appraisallevelBO;
    private DepartmentBO departmentBO;
    private CategoryBO categoryBO;
    
    private List<Appraisal> appraisals;
    private List summary;
    private List<Appraisal> selectedAppraisals = null;
    private List<Appraisal> filteredAppraisals;
    List<Appraisallevel> appraisallevels;
    private Appraisal editAppraisal;    
    private Date beginDate;
    private Date endDate;
    private Date beginSummaryDate;
    private Date endSummaryDate;
    private SelectItem[] appraisallevelOptions;
    private SelectItem[] departmentOptions;
    private SelectItem[] categoryOptions;
    private String role;

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getBeginSummaryDate() {
        return beginSummaryDate;
    }

    public void setBeginSummaryDate(Date beginSummaryDate) {
        this.beginSummaryDate = beginSummaryDate;
    }

    public Date getEndSummaryDate() {
        return endSummaryDate;
    }

    public void setEndSummaryDate(Date endSummaryDate) {
        this.endSummaryDate = endSummaryDate;
    }

    public List<Appraisal> getFilteredAppraisals() {
        return filteredAppraisals;
    }

    public void setFilteredAppraisals(List<Appraisal> filteredAppraisals) {
        this.filteredAppraisals = filteredAppraisals;
    }

    public List<Appraisal> getSelectedAppraisals() {
        return selectedAppraisals;
    }

    public void setSelectedAppraisals(List<Appraisal> selectedAppraisals) {
        this.selectedAppraisals = selectedAppraisals;
    }    

    public Appraisal getEditAppraisal() {
        return editAppraisal;
    }

    public void setEditAppraisal(Appraisal editAppraisal) {
        this.editAppraisal = editAppraisal;
    }

    public AppraisalBO getAppraisalBO() {
        return appraisalBO;
    }

    public void setAppraisalBO(AppraisalBO appraisalBO) {
        this.appraisalBO = appraisalBO;
    }
    
    public AppraisalBean() throws NamingException {
        appraisalBO = new AppraisalBO();
        appraisallevelBO = new AppraisallevelBO();
        departmentBO = new DepartmentBO();
        categoryBO = new CategoryBO();
        
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        this.role = session.getAttribute("Role").toString();
        
        appraisals = appraisalBO.getAllAppraisalList(role);
        
        
        appraisallevels = appraisallevelBO.getAllAppraisallevelList();
        appraisallevelOptions = this.createFilterOptions(appraisallevels);
        departmentOptions = this.createDepartmentFilterOptions(departmentBO.getAllDepartmentList());
        categoryOptions = this.createCategoryFilterOptions(categoryBO.getAllCategoryList());
        
        summary = appraisalBO.getResultList(appraisallevels,role);
        
    }

    public List<Appraisal> getAppraisals() {
        return appraisals;
    }

    public void setAppraisals(List<Appraisal> appraisals) {
        this.appraisals = appraisals;   
    }
    
    public void search(){
        
        this.appraisals = appraisalBO.getAppraisalListByDuration(beginDate, endDate);
    }

    public SelectItem[] getAppraisallevelOptions() {
        return appraisallevelOptions;
    }

    public SelectItem[] getDepartmentOptions() {
        return departmentOptions;
    }

    public SelectItem[] getCategoryOptions() {
        return categoryOptions;
    }
    
    private SelectItem[] createFilterOptions(List<Appraisallevel> appraisallevels)  { 
        int i = 0;
        SelectItem[] options = new SelectItem[appraisallevels.size() + 1];
        options[0] = new SelectItem("", "请选择");
        
        for(Appraisallevel appraisallevel:appraisallevels){
            appraisallevel.getName();
            options[i + 1] = new SelectItem(appraisallevel.getName(), appraisallevel.getName());
            i++;
        }  
        return options;  
    }  
    
    private SelectItem[] createDepartmentFilterOptions(List<Department> departments)  { 
        int i = 0;
        SelectItem[] options = new SelectItem[departments.size() + 1];
        options[0] = new SelectItem("", "请选择");
        
        for(Department department:departments){
            department.getName();
            options[i + 1] = new SelectItem(department.getName(), department.getName());
            i++;
        }  
        return options;  
    }
    
    private SelectItem[] createCategoryFilterOptions(List<Category> categorys)  { 
        int i = 0;
        SelectItem[] options = new SelectItem[categorys.size() + 1];
        options[0] = new SelectItem("", "请选择");
        
        for(Category category:categorys){
            category.getName();
            options[i + 1] = new SelectItem(category.getName(), category.getName());
            i++;
        }  
        return options;  
    }
    
    public void searchSummary(){
        this.summary=appraisalBO.getResultList(appraisallevels,beginSummaryDate,endSummaryDate,role);
    }

    public List getSummary() {
        return summary;
    }

    public void setSummary(List summary) {
        this.summary = summary;
    }
    
}
