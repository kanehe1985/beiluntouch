/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.SettingBO;
import com.original.evaluate.entity.Setting;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author kanehe
 */
@Named(value="settingBean")
@SessionScoped
public class SettingBean implements Serializable {
    
    private SettingBO settingBO;
    
    private Setting setting;
  
    public SettingBean() throws NamingException {
        settingBO = new SettingBO();
        setting = settingBO.getSetting();
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;   
    }
}
