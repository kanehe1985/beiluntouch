/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.AdminuserJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Adminuser;
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
public class AdminuserBO {

    private AdminuserJpaController adminUserJpaController;
    
    public AdminuserBO() throws NamingException{
        if (adminUserJpaController == null) {
            UserTransaction utx = FacesUtil.getUserTransaction();
            EntityManagerFactory emf = FacesUtil.getEntityManagerFactory(); 
            adminUserJpaController = new AdminuserJpaController(emf);
        }
    }

    public List<Adminuser> getAllAdminuserList() throws NamingException {
        return adminUserJpaController.findAdminuserEntities();
    }
    
    public Adminuser getAdminuserById(Integer id) {
        return adminUserJpaController.findAdminuser(id);
    }
    
    public void create(Adminuser adminUser) throws RollbackFailureException, Exception{
        adminUserJpaController.create(adminUser);
    }
    
    public void save(Adminuser adminUser) throws RollbackFailureException, Exception{
        adminUserJpaController.edit(adminUser);
    }
    
    public void delete(Adminuser adminUser) throws RollbackFailureException, Exception{
        adminUserJpaController.destroy(adminUser.getId());
    }
}
