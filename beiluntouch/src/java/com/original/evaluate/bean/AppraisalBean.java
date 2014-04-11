/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.AppraisalBO;
import com.original.evaluate.entity.Appraisal;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
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
    
    private List<Appraisal> appraisals;
    private List<Appraisal> selectedAppraisals = null;
    private Appraisal editAppraisal;

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
        appraisals = appraisalBO.getAllAppraisalList();
    }

    public List<Appraisal> getAppraisals() {
        return appraisals;
    }

    public void setAppraisals(List<Appraisal> appraisals) {
        this.appraisals = appraisals;   
    }
}
