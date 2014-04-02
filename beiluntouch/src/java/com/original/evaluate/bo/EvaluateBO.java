/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.EmployeeJpaController;
import com.original.evaluate.entity.Employee;
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
public class EvaluateBO {

    private EmployeeJpaController employeeJpaController;

    public EmployeeJpaController getJpaController() throws NamingException {
        if (employeeJpaController == null) {
            UserTransaction utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("openjpa"); 
            employeeJpaController = new EmployeeJpaController(utx, emf);
        }
        return employeeJpaController;
    }

    public List<Employee> getAllEmployeeList() throws NamingException {
        List<Employee> employeeList = new ArrayList<>();
        getJpaController().findEmployeeEntities();
        return employeeList;
    }
}
