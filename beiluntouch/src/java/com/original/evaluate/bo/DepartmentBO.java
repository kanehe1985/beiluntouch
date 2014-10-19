/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.DepartmentJpaController;
import com.original.evaluate.dao.DepartmentJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Department;
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
public class DepartmentBO {

    private DepartmentJpaController departmentJpaController;
    
    public DepartmentBO() throws NamingException{
        if (departmentJpaController == null) {
            UserTransaction utx = FacesUtil.getUserTransaction();
            EntityManagerFactory emf = FacesUtil.getEntityManagerFactory(); 
            departmentJpaController = new DepartmentJpaController(emf);
        }
    }

    public List<Department> getAllDepartmentList() throws NamingException {
        return departmentJpaController.findDepartmentEntities();
    }
    
    public Department getDepartmentById(Integer id) {
        return departmentJpaController.findDepartment(id);
    }
    
    public void create(Department department) throws RollbackFailureException, Exception{
        departmentJpaController.create(department);
    }
    
    public void save(Department department) throws RollbackFailureException, Exception{
        departmentJpaController.edit(department);
    }
    
    public void delete(Department department) throws RollbackFailureException, Exception{
        departmentJpaController.destroy(department.getId());
    }
}
