/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.NoticeBO;
import com.original.evaluate.entity.Notice;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author kanehe
 */
@Named(value="noticeBean")
@SessionScoped
public class NoticeBean implements Serializable {
    
    private NoticeBO noticeBO;
    
    private List<Notice> notices;
    private List<Notice> selectedNotices = null;
    private Notice editNotice;

    public List<Notice> getSelectedNotices() {
        return selectedNotices;
    }

    public void setSelectedNotices(List<Notice> selectedNotices) {
        this.selectedNotices = selectedNotices;
    }    

    public Notice getEditNotice() {
        return editNotice;
    }

    public void setEditNotice(Notice editNotice) {
        this.editNotice = editNotice;
    }

    public NoticeBO getNoticeBO() {
        return noticeBO;
    }

    public void setNoticeBO(NoticeBO noticeBO) {
        this.noticeBO = noticeBO;
    }
    
    public NoticeBean() throws NamingException {
        noticeBO = new NoticeBO();
        notices = noticeBO.getAllNoticeList();
    }

    public List<Notice> getNotices() {
        return notices;
    }

    public void setNotices(List<Notice> notices) {
        this.notices = notices;   
    }
    
    public void update() throws Exception{
        noticeBO.save(editNotice);        
        notices = noticeBO.getAllNoticeList();
    }
    
    public void create() throws Exception{
        noticeBO.create(editNotice);        
        notices = noticeBO.getAllNoticeList();
    }
    
    public void delete() throws Exception{
        for(Notice notice:selectedNotices){
            noticeBO.delete(notice);
        }
        notices = noticeBO.getAllNoticeList();
    }
    
    public void refresh() throws NamingException{
        notices = noticeBO.getAllNoticeList();
    }
    
    public void prepareCreate() {
        editNotice = new Notice();
    }
}
