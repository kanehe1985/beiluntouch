/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.util;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author dxx
 */
public class FacesUtil {

    /**
     * 获取当前FacesContext的实例对象
     *
     * @return FacesContext FacesContext的实例对象
     */
    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static UserTransaction getUserTransaction() throws NamingException {
        Context c = new InitialContext();
        return (UserTransaction) c.lookup("java:comp/UserTransaction");
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("beiluntouchPU");
    }

    public static void createFacesMessage(Severity severity, String summary, String detail) {
        getFacesContext().getExternalContext().getFlash().setKeepMessages(true);
        getFacesContext().addMessage(null, new FacesMessage(severity, summary, detail));
    }
    
    public static void createFacesMessage(FacesMessage message){
        getFacesContext().getExternalContext().getFlash().setKeepMessages(true);
        getFacesContext().addMessage(null,message);
    }
}
