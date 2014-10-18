/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.AppraisallevelBO;
import com.original.evaluate.bo.ReasonBO;
import com.original.evaluate.entity.Appraisallevel;
import com.original.evaluate.entity.Reason;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author kanehe
 */
@Named(value="reasonBean")
@SessionScoped
public class ReasonBean implements Serializable {
    
    private ReasonBO reasonBO;
    private AppraisallevelBO appraisallevelBO;
    
    private List<Reason> reasons;
    private List<Reason> selectedReasons = null;
    private Reason editReason;
    private int editAppraisallevelID;
    private List<Appraisallevel> appraisallevels;

    public List<Reason> getSelectedReasons() {
        return selectedReasons;
    }

    public void setSelectedReasons(List<Reason> selectedReasons) {
        this.selectedReasons = selectedReasons;
    }    

    public Reason getEditReason() {
        return editReason;
    }

    public void setEditReason(Reason editReason) {
        this.editReason = editReason;
    }

    public int getEditAppraisallevelID() {
        return editAppraisallevelID;
    }

    public void setEditAppraisallevelID(int editAppraisallevelID) {
        this.editAppraisallevelID = editAppraisallevelID;
    }
    
    public ReasonBO getReasonBO() {
        return reasonBO;
    }

    public void setReasonBO(ReasonBO reasonBO) {
        this.reasonBO = reasonBO;
    }

    public List<Appraisallevel> getAppraisallevels() {
        return appraisallevels;
    }

    public void setAppraisallevels(List<Appraisallevel> appraisallevels) {
        this.appraisallevels = appraisallevels;
    }
    
    public ReasonBean() throws NamingException {
        reasonBO = new ReasonBO();
        appraisallevelBO = new AppraisallevelBO();
        
        editReason = new Reason();
        reasons = reasonBO.getAllReasonList();
        appraisallevels = appraisallevelBO.getAllAppraisallevelList();
    }

    public List<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;   
    }
    
    public void update() throws Exception{
        editReason.setAppraisallevel(appraisallevelBO.getAppraisallevelById(editAppraisallevelID));
        reasonBO.save(editReason);        
        reasons = reasonBO.getAllReasonList();
    }
    
    public void create() throws Exception{
        editReason.setAppraisallevel(appraisallevelBO.getAppraisallevelById(editAppraisallevelID));
        reasonBO.create(editReason);        
        reasons = reasonBO.getAllReasonList();
    }
    
    public void delete() throws Exception{
        for(Reason reason:selectedReasons){
            reasonBO.delete(reason);
        }
        reasons = reasonBO.getAllReasonList();
    }
    
    public void prepareCreate() {
        editReason = new Reason();
        editAppraisallevelID = -1;
    }
}
