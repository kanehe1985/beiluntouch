/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.EmployeeJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Employee;
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
public class EmployeeBO {

    private EmployeeJpaController employeeJpaController;
    
    public EmployeeBO() throws NamingException{
        if (employeeJpaController == null) {
            UserTransaction utx = FacesUtil.getUserTransaction();
            EntityManagerFactory emf = FacesUtil.getEntityManagerFactory(); 
            employeeJpaController = new EmployeeJpaController(utx, emf);
        }
    }

    public List<Employee> getAllEmployeeList() throws NamingException {
        return employeeJpaController.findEmployeeEntities();
    }
    
    public Employee getEmployeeById(Integer id) {
        return employeeJpaController.findEmployee(id);
    }
    
    public void save(Employee employee) throws RollbackFailureException, Exception{
        employeeJpaController.edit(employee);
    }

    public List<Employee> getAllAppraisallevelList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
