/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.NoticeJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Notice;
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
public class NoticeBO {

    private NoticeJpaController noticeJpaController;
    
    public NoticeBO() throws NamingException{
        if (noticeJpaController == null) {
            UserTransaction utx = FacesUtil.getUserTransaction();
            EntityManagerFactory emf = FacesUtil.getEntityManagerFactory(); 
            noticeJpaController = new NoticeJpaController(emf);
        }
    }

    public List<Notice> getAllNoticeList() throws NamingException {
        return noticeJpaController.findNoticeEntities();
    }
    
    public Notice getNoticeById(Integer id) {
        return noticeJpaController.findNotice(id);
    }
    
    public void create(Notice notice) throws RollbackFailureException, Exception{
        noticeJpaController.create(notice);
    }
    
    public void save(Notice notice) throws RollbackFailureException, Exception{
        noticeJpaController.edit(notice);
    }
    
    public void delete(Notice notice) throws RollbackFailureException, Exception{
        noticeJpaController.destroy(notice.getId());
    }
}
