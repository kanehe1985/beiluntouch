/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bean;

import com.original.evaluate.dao.SettingJpaController;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.Persistence;

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

    private SettingJpaController getSettingJpaController() {
        if (settingJpaController == null) {
            settingJpaController = new SettingJpaController(Persistence.createEntityManagerFactory("beiluntouchPU"));
        }
        return settingJpaController;
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

    public String login() {
        if (getSettingJpaController().findSettingEntities().get(0).getAdminpassword().equals(password)) {
            return "Appraisal.xhtml";  //跳转至页面
        }
        return null;
    }
}
