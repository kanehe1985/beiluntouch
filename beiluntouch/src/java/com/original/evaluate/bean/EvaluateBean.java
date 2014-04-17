/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bean;

import com.original.evaluate.dao.AppraisalJpaController;
import com.original.evaluate.dao.AppraisallevelJpaController;
import com.original.evaluate.dao.EmployeeJpaController;
import com.original.evaluate.dao.ReasonJpaController;
import com.original.evaluate.entity.Appraisal;
import com.original.evaluate.entity.Appraisallevel;
import com.original.evaluate.entity.Employee;
import com.original.evaluate.entity.Reason;
import com.original.util.FacesUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author dxx
 */
@Named(value = "evaluateBean")
@SessionScoped
public class EvaluateBean implements Serializable {

    private EmployeeJpaController employeeJpaController;
    private AppraisallevelJpaController appraisallevelJpaController;
    private AppraisalJpaController appraisalJpaController;
    private ReasonJpaController reasonJpaController;
    private LinkedHashMap<String, Integer> appraisalItems;
    private LinkedHashMap<String, Integer> reasonItems;
    private List<Employee> employeeList = null;
    private Employee selectedEmployee;
    private String groupCondition;
    private String roomCondition;
    private Integer appraisalValue;
    private Integer reasonValue;
    private String appraiser;
    private String reason;
    private String contact;    
    private Date createDate;

    public EvaluateBean() {

    }

    public List<Employee> getEmployeeList() {
        if (employeeList == null) {
            employeeList = getEmployeeJpaController().findEmployeeEntities();
        }
        return employeeList;
    }

    private EmployeeJpaController getEmployeeJpaController() {
        if (employeeJpaController == null) {
            employeeJpaController = new EmployeeJpaController(FacesUtil.getEntityManagerFactory());
        }
        return employeeJpaController;
    }

    private AppraisallevelJpaController getAppraisallevelJpaController() {
        if (appraisallevelJpaController == null) {
            appraisallevelJpaController = new AppraisallevelJpaController(FacesUtil.getEntityManagerFactory());
        }
        return appraisallevelJpaController;
    }

    private AppraisalJpaController getAppraisalJpaController() {
        if (appraisalJpaController == null) {
            appraisalJpaController = new AppraisalJpaController(FacesUtil.getEntityManagerFactory());
        }
        return appraisalJpaController;
    }

    public ReasonJpaController getReasonJpaController() {
        if (reasonJpaController == null) {
            reasonJpaController = new ReasonJpaController(FacesUtil.getEntityManagerFactory());
        }
        return reasonJpaController;
    }

    public LinkedHashMap<String, Integer> getAppraisalItems() {
        if (appraisalItems == null) {
            appraisalItems = new LinkedHashMap<>();
            List<Appraisallevel> levelList = getAppraisallevelJpaController().findAppraisallevelEntities();
            for (Appraisallevel level : levelList) {
                appraisalItems.put(level.getName(), level.getId());
            }
        }
        return appraisalItems;
    }

    public LinkedHashMap<String, Integer> getReasonItems() {
        if (reasonItems == null) {
            reasonItems = new LinkedHashMap<>();
            List<Reason> reasonList = getReasonJpaController().findReasonEntities();
            for (Reason reason : reasonList) {
                reasonItems.put(reason.getContent(), reason.getId());
            }    
        }
        return reasonItems;
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Employee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public String getGroupCondition() {
        return groupCondition;
    }

    public void setGroupCondition(String groupCondition) {
        this.groupCondition = groupCondition;
    }

    public String getRoomCondition() {
        return roomCondition;
    }

    public void setRoomCondition(String roomCondition) {
        this.roomCondition = roomCondition;
    }

    public Integer getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(Integer appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public Integer getReasonValue() {
        return reasonValue;
    }

    public void setReasonValue(Integer reasonValue) {
        this.reasonValue = reasonValue;
    }

    public String getAppraiser() {
        return appraiser;
    }

    public void setAppraiser(String appraiser) {
        this.appraiser = appraiser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createFate) {
        this.createDate = createFate;
    }

    public void search() {
        employeeList = employeeJpaController.getEmployeeListByCondition(groupCondition, roomCondition);
    }

    public void save() {
        try {
            Appraisal appraisal = new Appraisal();
            Appraisallevel level = new Appraisallevel();

            level.setId(appraisalValue);
            appraisal.setAppraisallevel(level);
            appraisal.setAppraiser(appraiser);
            appraisal.setCreatedate(createDate);
            appraisal.setContent(reason);
            appraisal.setContact(contact);
            getAppraisalJpaController().create(appraisal);
        } catch (Exception ex) {
            Logger.getLogger(EvaluateBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void evaluate(Employee employee) {
        this.selectedEmployee = employee;
        reason = null;
        appraiser = null;
        contact=null;
        createDate = null;
    }
}
