/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.AppraisalBO;
import com.original.evaluate.bo.AppraisallevelBO;
import com.original.evaluate.entity.Appraisal;
import com.original.evaluate.entity.Appraisallevel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author kanehe
 */
@Named(value="appraisalBean")
@SessionScoped
public class AppraisalBean implements Serializable {
    
    private AppraisalBO appraisalBO;
    private AppraisallevelBO appraisallevelBO;
    
    private List<Appraisal> appraisals;
    private List<Appraisal> selectedAppraisals = null;
    private List<Appraisal> filteredAppraisals;
    private Appraisal editAppraisal;    
    private Date beginDate;
    private Date endDate;
    private SelectItem[] appraisallevelOptions;

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
        appraisals = appraisalBO.getAllAppraisalList();
        
        appraisallevelOptions = this.createFilterOptions(appraisallevelBO.getAllAppraisallevelList());
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
}
