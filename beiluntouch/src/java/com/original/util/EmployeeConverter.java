/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
 
import com.original.evaluate.entity.Employee;
import com.original.evaluate.bean.EmployeeBean;
 
@FacesConverter("employeeConverter")
public class EmployeeConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            EmployeeBean service = (EmployeeBean) fc.getExternalContext().getApplicationMap().get("employeeBean");
            return service.getEmployees().get(Integer.parseInt(value));
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(object);
        }
        else {
            return null;
        }
    }   
}