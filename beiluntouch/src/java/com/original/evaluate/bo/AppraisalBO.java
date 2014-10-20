/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.AppraisalJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Appraisal;
import com.original.evaluate.entity.Appraisallevel;
import com.original.util.FacesUtil;
import java.text.DateFormat;
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
    
    public List getResultList(List<Appraisallevel> appraisallevels,String role){
        return getResultList(appraisallevels,null,null,role);
    }
    
    public List getResultList(List<Appraisallevel> appraisallevels,Date beginDate,Date endDate,String role){
        String template1="select employee.name%1$s from employee ";
        String template2="left join(" +
                        "SELECT employee,count(appraisallevel) as %2$scount FROM appraisal left join employee on appraisal.employee = employee.id " +
                        "where appraisallevel=%1$s %3$s ";
        String template3="GROUP BY employee,appraisallevel" +
                        ") as %1$s on employee.id = %1$s.employee ";
        String template4="not isnull(%1$scount)";
        int tabIndex=0;
        String sql1="";
        String sql2="";
        String sql3="Where ";
        String tableCols="";
        for(Appraisallevel al : appraisallevels){
            String tableName="t"+tabIndex;
            sql2+=String.format(template2, al.getId(),tableName,role.equals("")?"":" and employee.department="+role);
            if(beginDate!=null || endDate!=null)
                sql2+=String.format(" and createdate between '%1$s' and '%2$s'",beginDate==null?"1900-1-1":DateFormat.getDateInstance().format(beginDate),endDate==null?"9999-12-31":DateFormat.getDateInstance().format(endDate));
            sql2+=String.format(template3, tableName);
            
            sql3+=String.format(template4, tableName)+" OR ";
            tableCols+=","+tableName+"."+tableName+"count";
            tabIndex++;
        }
        sql1 = String.format(template1, tableCols);
        
        return appraisalJpaController.exeSQL(sql1+sql2+sql3.substring(0, sql3.length()-4)+" ORDER BY employee.deporderid,employee.orderid");
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
