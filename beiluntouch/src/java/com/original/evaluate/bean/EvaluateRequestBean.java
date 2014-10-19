/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bean;

import com.original.evaluate.bo.SettingBO;
import com.original.evaluate.dao.AppraisalJpaController;
import com.original.evaluate.dao.AppraisallevelJpaController;
import com.original.evaluate.dao.CategoryJpaController;
import com.original.evaluate.dao.DepartmentJpaController;
import com.original.evaluate.dao.EmployeeJpaController;
import com.original.evaluate.dao.NoticeJpaController;
import com.original.evaluate.dao.ReasonJpaController;
import com.original.evaluate.dao.RoomJpaController;
import com.original.evaluate.entity.Appraisal;
import com.original.evaluate.entity.Appraisallevel;
import com.original.evaluate.entity.Category;
import com.original.evaluate.entity.Department;
import com.original.evaluate.entity.Employee;
import com.original.evaluate.entity.Notice;
import com.original.evaluate.entity.Reason;
import com.original.evaluate.entity.Room;
import com.original.evaluate.entity.Setting;
import com.original.util.FacesUtil;
import com.original.util.MessageUtil;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author dxx
 */
@Named(value = "evaluateRequestBean")
@RequestScoped
public class EvaluateRequestBean implements Serializable{

    private EmployeeJpaController employeeJpaController;
    private AppraisallevelJpaController appraisallevelJpaController;
    private AppraisalJpaController appraisalJpaController;
    private ReasonJpaController reasonJpaController;
    private DepartmentJpaController departmentJpaController;
    private CategoryJpaController categoryJpaController;
    private NoticeJpaController noticeJpaController;
    private RoomJpaController roomJpaController;
    private LinkedHashMap<String, Integer> appraisalItems;
    private LinkedHashMap<String, Integer> reasonItems;
    private LinkedHashMap<String, Integer> departmentItems;
    private LinkedHashMap<String, Integer> categoryItems;
    private LinkedHashMap<String, String> roomItems;
    private List<Department> departments;
    private List<Employee> employeeList;    
    private Employee selectedEmployee;
    private String categoryCondition;
    private String roomCondition;
    private Integer appraisalValue;
    private Integer reasonValue;
    private Integer departmentValue;
    private String appraiser;
    private String reason;
    private String contact;
    private String depID;
    private Date createDate;
    private Boolean merge;
    private String tag;
    private Boolean reasonRequired=false;
    

    public EvaluateRequestBean(){
        
    }
    
    public String getBackUrl() {
        String param="index.xhtml";
        if(tag !=null){
            param+="?tag="+tag;
        }
        if(merge==true)
            param+="&m=true";
        return param;
    }

//    public List<Employee> getEmployeeList() {
//        List<Employee> tempList = getEmployeeJpaController().findEmployeeEntities();
//        int depID = Integer.parseInt(this.getRequestParameter("depid"));
//        employeeList = new ArrayList<>();
//        for (Employee employee : tempList) {
//            if (employee.getDepartment().getId().equals(depID)) {
//                employeeList.add(employee);
//            }
//        }
//        return employeeList;
//        return tempList;
//    }
    
    public List<Employee> getEmployeeList(){
//        int depID = Integer.parseInt(this.getRequestParameter("depid"));
//        this.employeeList = getEmployeeJpaController().findEmployeeEntities(17,groupCondition,roomCondition);
        search();
//        this.employeeList = getEmployeeJpaController().findEmployeeEntities();
        return this.employeeList;
    }

    public String getTag() {
        String t = this.getRequestParameter("tag");
        if(t!=null){
            tag = t;
        }
        return tag;
    }

    public Boolean getMerge() {
        String m = this.getRequestParameter("m");
        merge = m!=null && "true".equals(m);        
        return merge;
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
    
    private CategoryJpaController getCategoryJpaController() {
        if (categoryJpaController == null) {
            categoryJpaController = new CategoryJpaController(FacesUtil.getEntityManagerFactory());
        }
        return categoryJpaController;
    }
    
    private RoomJpaController getRoomJpaController() {
         if (roomJpaController == null) {
            roomJpaController = new RoomJpaController(FacesUtil.getEntityManagerFactory());
        }
        return roomJpaController;
    }

    private NoticeJpaController getNoticeJpaController() {
        if (noticeJpaController == null) {
            noticeJpaController = new NoticeJpaController(FacesUtil.getEntityManagerFactory());
        }
        return noticeJpaController;
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
    
    public LinkedHashMap<String, Integer> getCategoryItems() {
        if (categoryItems == null) {
            categoryItems = new LinkedHashMap<>();
//            List<Category> categoryList = getCategoryJpaController().findCategoryEntities();
//            for (Category category : categoryList) {
//                categoryItems.put(category.getName(), category.getId());
//            }
            
            List<Employee> employees = null;
            if(getRequestParameter("depid")!=null){
                depID = getRequestParameter("depid");            
            }else if(getRequestParameter("tag")!=null){
                tag = getRequestParameter("tag");
            }
            if(depID != null){
                employees = getEmployeeJpaController().findEmployeeEntities(depID,null,null);
            } else if(tag != null){
                employees = getEmployeeJpaController().findEmployeeEntitiesByTag(tag, null, null);            
            }
            
            for(Employee employee : employees){
                Category employeeCategory = employee.getCategory();
                if(employeeCategory != null && !categoryItems.containsValue(employeeCategory.getId())){
                    categoryItems.put(employeeCategory.getName(), employeeCategory.getId());
                }
            }
            
        }
        return categoryItems;
    }

    public LinkedHashMap<String, String> getRoomItems() {
        roomItems = new LinkedHashMap<>();
//        List<Room> roomList = getRoomJpaController().findRoomEntities();
        
        List<Employee> employees = null;
        if(getRequestParameter("depid")!=null){
            depID = getRequestParameter("depid");            
        }else if(getRequestParameter("tag")!=null){
            tag = getRequestParameter("tag");
        }
        if(depID != null){
            employees = getEmployeeJpaController().findEmployeeEntities(depID,null,null);
        } else if(tag != null){
            employees = getEmployeeJpaController().findEmployeeEntitiesByTag(tag, null, null);            
        }
        
        for(Employee employee : employees){
            String employeeRoomNo = employee.getRomno();
            if(employeeRoomNo != null && employeeRoomNo.length()>0 && !roomItems.containsValue(employeeRoomNo)){
                roomItems.put(employeeRoomNo, employeeRoomNo);
            }
        }
        
//        for (Room room : roomList) {
//            if(merge==true){
//                if(room.getDepartment().getTag().equals(tag)){
//                    if(categoryCondition == null || categoryCondition.isEmpty() || room.getCategory().getId().toString().equals(this.categoryCondition)){
//                        roomItems.put(room.getNo(), room.getNo());
//                    }                        
//                }
//            }else{
//                if(room.getDepartment().getId().toString().equals(depID)){
//                    if(categoryCondition == null || categoryCondition.isEmpty() || room.getCategory().getId().toString().equals(this.categoryCondition)){
//                        roomItems.put(room.getNo(), room.getNo());
//                    }                        
//                }
//            }
//        }
        
        return roomItems;
    }

    public List<Department> getDepartments() {
        return getDepartmentJpaController().getDeparmentListByTag(this.getRequestParameter("tag"));
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Employee employee) {
        appraisalValue=null;
        reason=null;
        reasonValue=null;
        appraiser=null;
        contact=null;
        this.selectedEmployee = employee;
    }

    public String getCategoryCondition() {
        return categoryCondition;
    }

    public void setCategoryCondition(String categoryCondition) {
        this.categoryCondition = categoryCondition;
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

    public Boolean getReasonRequired() {
        return reasonRequired;
    }

    public void setReasonRequired(Boolean reasonRequired) {
        this.reasonRequired = reasonRequired;
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
//        this.employeeList = getEmployeeJpaController().findEmployeeEntities();
        
        if(getRequestParameter("depid")!=null){
            depID = getRequestParameter("depid");
            
        }else if(getRequestParameter("tag")!=null){
            tag = getRequestParameter("tag");
        }
        if(depID != null){
            this.employeeList = getEmployeeJpaController().findEmployeeEntities(depID,categoryCondition,roomCondition);
        } else if(tag != null){
            this.employeeList = getEmployeeJpaController().findEmployeeEntitiesByTag(tag, categoryCondition, roomCondition);            
        }        
    }

    public void save() throws IOException {
        try {
            Appraisal appraisal = new Appraisal();
//            Appraisallevel level = new Appraisallevel();            
//            level.setId(appraisalValue);
            
            Appraisallevel level = getAppraisallevelJpaController().findAppraisallevel(appraisalValue);
            appraisal.setAppraisallevel(level);
            appraisal.setAppraiser(appraiser);
            appraisal.setCreatedate(new Date());
            appraisal.setContent(reason);
            appraisal.setContact(contact);
            appraisal.setEmployee(selectedEmployee);
            getAppraisalJpaController().create(appraisal);
            
            if(level.getIsalert()){
                List<Notice> findNoticeEntities = getNoticeJpaController().findNoticeEntities();
                Employee leader;
                
                String messageText;
                        messageText = selectedEmployee.getName()+"于"+
                                DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(appraisal.getCreatedate())+"被评价为"+
                                level.getName()+","+
                                appraisal.getContent();
                
                List<String> tels= new ArrayList<String>();
                for(Notice notice : findNoticeEntities){
                    if((notice.getDepartment().getId()==selectedEmployee.getDepartment().getId() && notice.getCategory() == null)||
                        (notice.getDepartment().getId()==selectedEmployee.getDepartment().getId() && notice.getCategory().getId() == selectedEmployee.getCategory().getId())
                        ){                        
                        leader = notice.getEmployee();
                        tels.add(leader.getTelephone());
//                        notifyLeader(leader.getTelephone(),messageText);
                    }
                }
                if(tels.size()>0){
                    String[] telnos = tels.toArray(new String[tels.size()]);
                    notifyLeader(telnos,messageText);
                }                
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect("thanks.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(EvaluateBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {           
            categoryCondition=null;
            roomCondition=null;
        }
    }
    
    private void notifyLeader(String[] telephone,String messageText) throws NamingException{
        SettingBO settingBO = new SettingBO();
        Setting setting = settingBO.getSetting();
        MessageUtil.Send(setting.getMessageserver(), setting.getMessageappid(),setting.getMessagepwd(),telephone, messageText);
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
    
    public void onSelectLevel(AjaxBehaviorEvent e){
        reasonRequired = this.getAppraisallevelJpaController().findAppraisallevel(appraisalValue).getIsalert();
    }

    private String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public String start() {
        return "evaluate.xhtml?faces-redirect=true&tag=" + getRequestParameter("tag");
    }    
}
