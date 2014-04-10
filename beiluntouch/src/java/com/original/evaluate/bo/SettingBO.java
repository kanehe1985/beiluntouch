/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.ReasonJpaController;
import com.original.evaluate.dao.SettingJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Reason;
import com.original.evaluate.entity.Setting;
import com.original.util.FacesUtil;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
 */
public class SettingBO {

    private SettingJpaController settingJpaController;
    
    private Setting setting = null;
 
    public SettingBO() throws NamingException{
        if (settingJpaController == null) {
            UserTransaction utx = FacesUtil.getUserTransaction();
            EntityManagerFactory emf = FacesUtil.getEntityManagerFactory(); 
            settingJpaController = new SettingJpaController(utx, emf);
        }
    }
    
    public Setting getSetting(){        
        List<Setting> settings = settingJpaController.findSettingEntities();
        if(settings.size()>0){
            setting = settings.get(0);
        } else {
            setting = new Setting();
        }
        return setting;
    }
}