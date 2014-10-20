/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bean;

import com.original.evaluate.dao.AdminuserJpaController;
import com.original.evaluate.dao.SettingJpaController;
import com.original.evaluate.entity.Adminuser;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;

/**
 *
 * @author dxx
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    private SettingJpaController settingJpaController = null;
    private AdminuserJpaController adminuserJpaController = null;
    private String role="";
    private String adminRole="false";

    private SettingJpaController getSettingJpaController() {
        if (settingJpaController == null) {
            settingJpaController = new SettingJpaController(Persistence.createEntityManagerFactory("beiluntouchPU"));
        }
        return settingJpaController;
    }
    
    private AdminuserJpaController getAdminuserJpaController() {
        if (adminuserJpaController == null) {
            adminuserJpaController = new AdminuserJpaController(Persistence.createEntityManagerFactory("beiluntouchPU"));
        }
        return adminuserJpaController;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
//        this.role = role;
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute("Role", role);
    }

    public String getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(String adminRole) {
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute("AdminRole", adminRole);
    }


    public String login() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session1 = (HttpSession) fc.getExternalContext().getSession(false);
        session1.invalidate();
        if (username.equals("admin") && getSettingJpaController().findSettingEntities().get(0).getAdminpassword().equals(password)) {
            setAdminRole("true");
            setRole("");
            return "Statistics.xhtml";  //跳转至页面
        }else{
            List<Adminuser> findAdminuserEntities = getAdminuserJpaController().findAdminuserEntities();
            for(Adminuser adminuser : findAdminuserEntities){
                if(adminuser.getName().equals(username) && adminuser.getPassword().equals(password)){
                    setAdminRole("false");
                    if(adminuser.getDepartment()==null){
                        setRole("");
                    }else{
                        setRole(adminuser.getDepartment().getId().toString());
                    }
//                    Integer roleValue=adminuser.getDepartment().getId();
//                    setRole(roleValue==null?"":roleValue.toString());
                    return "Statistics.xhtml";
                }
            }
        }
        return null;
    }
    
//    private void setRole(String role){
//        this.role = role;
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ExternalContext extContext =facesContext.getExternalContext();
//        this.session =(HttpSession)extContext.getSession(true);
//        this.session.setAttribute("Role", role);
//    }
}
