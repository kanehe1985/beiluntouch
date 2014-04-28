/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bean;

import com.original.evaluate.dao.AppraisalJpaController;
import com.original.evaluate.dao.AppraisallevelJpaController;
import com.original.evaluate.dao.DepartmentJpaController;
import com.original.evaluate.dao.EmployeeJpaController;
import com.original.evaluate.dao.ReasonJpaController;
import com.original.evaluate.entity.Appraisal;
import com.original.evaluate.entity.Appraisallevel;
import com.original.evaluate.entity.Department;
import com.original.evaluate.entity.Employee;
import com.original.evaluate.entity.Reason;
import com.original.util.FacesUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

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
    private DepartmentJpaController departmentJpaController;
    private LinkedHashMap<String, Integer> appraisalItems;
    private LinkedHashMap<String, Integer> reasonItems;
    private LinkedHashMap<String, Integer> departmentItems;
    private List<Department> departments;
    private List<Employee> employeeList = null;
    private Employee selectedEmployee;
    private String groupCondition;
    private String roomCondition;
    private Integer appraisalValue;
    private Integer reasonValue;
    private Integer departmentValue;
    private String appraiser;
    private String reason;
    private String contact;
    private Date createDate;

    public EvaluateBean() {

    }

    public List<Employee> getEmployeeList() {
        List<Employee> tempList = getEmployeeJpaController().findEmployeeEntities();
        int depID = Integer.parseInt(this.getRequestParameter("depid"));
        employeeList = new ArrayList<>();
        for (Employee employee : tempList) {
            if (employee.getDepartment().getId().equals(depID)) {
                employeeList.add(employee);
            }
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

    private ReasonJpaController getReasonJpaController() {
        if (reasonJpaController == null) {
            reasonJpaController = new ReasonJpaController(FacesUtil.getEntityManagerFactory());
        }
        return reasonJpaController;
    }

    private DepartmentJpaController getDepartmentJpaController() {
        if (departmentJpaController == null) {
            departmentJpaController = new DepartmentJpaController(FacesUtil.getEntityManagerFactory());
        }
        return departmentJpaController;
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

    public LinkedHashMap<String, Integer> getDepartmentItems() {
        if (departmentItems == null) {
            departmentItems = new LinkedHashMap<>();
            List<Department> departmentList = getDepartmentJpaController().findDepartmentEntities();
            for (Department department : departmentList) {
                departmentItems.put(department.getName(), department.getId());
            }
        }
        return departmentItems;
    }

    public List<Department> getDepartments() {
        String tag = this.getRequestParameter("tag");
        return getDepartmentJpaController().getDeparmentListByTag(tag);
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
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

    public Integer getDepartmentValue() {
        return departmentValue;
    }

    public void setDepartmentValue(Integer departmentValue) {
        this.departmentValue = departmentValue;
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
            appraisal.setCreatedate(new Date());
            appraisal.setContent(reason);
            appraisal.setContact(contact);
            appraisal.setEmployee(selectedEmployee);
            getAppraisalJpaController().create(appraisal);
        } catch (Exception ex) {
            Logger.getLogger(EvaluateBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void evaluate(Employee employee) {
        this.selectedEmployee = employee;
        reason = null;
        appraiser = null;
        contact = null;
        createDate = null;
    }

    public void onSelectReason(AjaxBehaviorEvent e) {
        reason = getReasonJpaController().findReason(reasonValue).getContent();
    }

    private String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public String start() {
        return "evaluate.xhtml?faces-redirect=true&tag=" + getRequestParameter("tag");
    }
}