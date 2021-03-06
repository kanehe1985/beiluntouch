/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kanehe
 */
@Entity
@Table(name = "setting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Setting.findAll", query = "SELECT s FROM Setting s"),
    @NamedQuery(name = "Setting.findById", query = "SELECT s FROM Setting s WHERE s.id = :id"),
    @NamedQuery(name = "Setting.findByAdminpassword", query = "SELECT s FROM Setting s WHERE s.adminpassword = :adminpassword"),
    @NamedQuery(name = "Setting.findByTelphoneno", query = "SELECT s FROM Setting s WHERE s.telphoneno = :telphoneno"),
    @NamedQuery(name = "Setting.findByMessageserver", query = "SELECT s FROM Setting s WHERE s.messageserver = :messageserver")})
public class Setting implements Serializable {
    @Size(max = 45)
    @Column(name = "messageappid")
    private String messageappid;
    @Size(max = 45)
    @Column(name = "messagepwd")
    private String messagepwd;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "adminpassword")
    private String adminpassword;
    @Size(max = 100)
    @Column(name = "telphoneno")
    private String telphoneno;
    @Size(max = 100)
    @Column(name = "messageserver")
    private String messageserver;

    public Setting() {
    }

    public Setting(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdminpassword() {
        return adminpassword;
    }

    public void setAdminpassword(String adminpassword) {
        this.adminpassword = adminpassword;
    }

    public String getTelphoneno() {
        return telphoneno;
    }

    public void setTelphoneno(String telphoneno) {
        this.telphoneno = telphoneno;
    }

    public String getMessageserver() {
        return messageserver;
    }

    public void setMessageserver(String messageserver) {
        this.messageserver = messageserver;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Setting)) {
            return false;
        }
        Setting other = (Setting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.original.evaluate.entity.Setting[ id=" + id + " ]";
    }

    public String getMessageappid() {
        return messageappid;
    }

    public void setMessageappid(String messageappid) {
        this.messageappid = messageappid;
    }

    public String getMessagepwd() {
        return messagepwd;
    }

    public void setMessagepwd(String messagepwd) {
        this.messagepwd = messagepwd;
    }
    
}
