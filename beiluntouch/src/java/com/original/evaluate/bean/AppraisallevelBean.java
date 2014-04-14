/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.AppraisallevelBO;
import com.original.evaluate.entity.Appraisallevel;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author kanehe
 */
@Named(value="appraisallevelBean")
@SessionScoped
public class AppraisallevelBean implements Serializable {
    
    private AppraisallevelBO appraisallevelBO;
    
    private List<Appraisallevel> appraisallevels;
    private List<Appraisallevel> selectedAppraisallevels = null;
    private Appraisallevel editAppraisallevel;

    public List<Appraisallevel> getSelectedAppraisallevels() {
        return selectedAppraisallevels;
    }

    public void setSelectedAppraisallevels(List<Appraisallevel> selectedAppraisallevels) {
        this.selectedAppraisallevels = selectedAppraisallevels;
    }    

    public Appraisallevel getEditAppraisallevel() {
        return editAppraisallevel;
    }

    public void setEditAppraisallevel(Appraisallevel editAppraisallevel) {
        this.editAppraisallevel = editAppraisallevel;
    }

    public AppraisallevelBO getAppraisallevelBO() {
        return appraisallevelBO;
    }

    public void setAppraisallevelBO(AppraisallevelBO appraisallevelBO) {
        this.appraisallevelBO = appraisallevelBO;
    }
    
    public AppraisallevelBean() throws NamingException {
        appraisallevelBO = new AppraisallevelBO();
        appraisallevels = appraisallevelBO.getAllAppraisallevelList();
    }

    public List<Appraisallevel> getAppraisallevels() {
        return appraisallevels;
    }

    public void setAppraisallevels(List<Appraisallevel> appraisallevels) {
        this.appraisallevels = appraisallevels;   
    }
    
    public void update() throws Exception{
        appraisallevelBO.save(editAppraisallevel);        
        appraisallevels = appraisallevelBO.getAllAppraisallevelList();
    }
    
    public void create() throws Exception{
        appraisallevelBO.create(editAppraisallevel);        
        appraisallevels = appraisallevelBO.getAllAppraisallevelList();
    }
    
    public void delete() throws Exception{
        for(Appraisallevel appraisallevel:selectedAppraisallevels){
            appraisallevelBO.delete(appraisallevel);
        }
        appraisallevels = appraisallevelBO.getAllAppraisallevelList();
    }
    
    public void refresh() throws NamingException{
        appraisallevels = appraisallevelBO.getAllAppraisallevelList();
    }
    
    public void prepareCreate() {
        editAppraisallevel = new Appraisallevel();
    }
}
