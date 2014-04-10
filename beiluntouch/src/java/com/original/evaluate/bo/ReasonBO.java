/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.ReasonJpaController;
import com.original.evaluate.dao.ReasonJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Reason;
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
public class ReasonBO {

    private ReasonJpaController reasonJpaController;
    
    public ReasonBO() throws NamingException{
        if (reasonJpaController == null) {
            UserTransaction utx = FacesUtil.getUserTransaction();
            EntityManagerFactory emf = FacesUtil.getEntityManagerFactory(); 
            reasonJpaController = new ReasonJpaController(utx, emf);
        }
    }

    public List<Reason> getAllReasonList() throws NamingException {
        return reasonJpaController.findReasonEntities();
    }
    
    public Reason getReasonById(Integer id) {
        return reasonJpaController.findReason(id);
    }
    
    public void create(Reason reason) throws RollbackFailureException, Exception{
        reasonJpaController.create(reason);
    }
    
    public void save(Reason reason) throws RollbackFailureException, Exception{
        reasonJpaController.edit(reason);
    }
    
    public void delete(Reason reason) throws RollbackFailureException, Exception{
        reasonJpaController.destroy(reason.getId());
    }
}
