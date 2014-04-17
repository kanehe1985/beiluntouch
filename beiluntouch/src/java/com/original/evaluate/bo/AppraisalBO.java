/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.AppraisalJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Appraisal;
import com.original.util.FacesUtil;
import java.util.ArrayList;
import java.util.Date;
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
public class AppraisalBO {

    private AppraisalJpaController appraisalJpaController;
    
    public AppraisalBO() throws NamingException{
        if (appraisalJpaController == null) {
            UserTransaction utx = FacesUtil.getUserTransaction();
            EntityManagerFactory emf = FacesUtil.getEntityManagerFactory(); 
            appraisalJpaController = new AppraisalJpaController(emf);
        }
    }

    public List<Appraisal> getAllAppraisalList() throws NamingException {
        return appraisalJpaController.findAppraisalEntities();
    }
    
    public List<Appraisal> getAppraisalListByDuration(Date beginDate,Date endDate){
        return appraisalJpaController.findAppraisalEntitiesByDuration(beginDate, endDate);
    }
    
    public Appraisal getAppraisalById(Integer id) {
        return appraisalJpaController.findAppraisal(id);
    }
    
    public void create(Appraisal appraisal) throws RollbackFailureException, Exception{
        appraisalJpaController.create(appraisal);
    }
    
    public void save(Appraisal appraisal) throws RollbackFailureException, Exception{
        appraisalJpaController.edit(appraisal);
    }
    
    public void delete(Appraisal appraisal) throws RollbackFailureException, Exception{
        appraisalJpaController.destroy(appraisal.getId());
    }
}
