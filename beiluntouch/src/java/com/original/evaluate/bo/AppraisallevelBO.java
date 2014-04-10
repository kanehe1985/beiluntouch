/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.AppraisallevelJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Appraisallevel;
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
public class AppraisallevelBO {

    private AppraisallevelJpaController appraisallevelJpaController;
    
    public AppraisallevelBO() throws NamingException{
        if (appraisallevelJpaController == null) {
            UserTransaction utx = FacesUtil.getUserTransaction();
            EntityManagerFactory emf = FacesUtil.getEntityManagerFactory(); 
            appraisallevelJpaController = new AppraisallevelJpaController(utx, emf);
        }
    }

    public List<Appraisallevel> getAllAppraisallevelList() throws NamingException {
        return appraisallevelJpaController.findAppraisallevelEntities();
    }
    
    public Appraisallevel getAppraisallevelById(Integer id) {
        return appraisallevelJpaController.findAppraisallevel(id);
    }
    
    public void create(Appraisallevel appraisallevel) throws RollbackFailureException, Exception{
        appraisallevelJpaController.create(appraisallevel);
    }
    
    public void save(Appraisallevel appraisallevel) throws RollbackFailureException, Exception{
        appraisallevelJpaController.edit(appraisallevel);
    }
    
    public void delete(Appraisallevel appraisallevel) throws RollbackFailureException, Exception{
        appraisallevelJpaController.destroy(appraisallevel.getId());
    }
}
