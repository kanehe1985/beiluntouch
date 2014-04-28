/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kanehe
 */
@Entity
@Table(name = "appraisal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Appraisal.findAll", query = "SELECT a FROM Appraisal a"),
    @NamedQuery(name = "Appraisal.findById", query = "SELECT a FROM Appraisal a WHERE a.id = :id"),
    @NamedQuery(name = "Appraisal.findByContent", query = "SELECT a FROM Appraisal a WHERE a.content = :content"),
    @NamedQuery(name = "Appraisal.findByAppraiser", query = "SELECT a FROM Appraisal a WHERE a.appraiser = :appraiser"),
    @NamedQuery(name = "Appraisal.findByContact", query = "SELECT a FROM Appraisal a WHERE a.contact = :contact"),
    @NamedQuery(name = "Appraisal.findByCreatedate", query = "SELECT a FROM Appraisal a WHERE a.createdate = :createdate")})
public class Appraisal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 1000)
    @Column(name = "content")
    private String content;
    @Size(max = 45)
    @Column(name = "appraiser")
    private String appraiser;
    @Size(max = 100)
    @Column(name = "contact")
    private String contact;
    @Column(name = "createdate")
    @Temporal(TemporalType.DATE)
    private Date createdate;
    @JoinColumn(name = "appraisallevel", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Appraisallevel appraisallevel;
    @JoinColumn(name = "employee", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employee;

    public Appraisal() {
    }

    public Appraisal(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAppraiser() {
        return appraiser;
    }

    public void setAppraiser(String appraiser) {
        this.appraiser = appraiser;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Appraisallevel getAppraisallevel() {
        return appraisallevel;
    }

    public void setAppraisallevel(Appraisallevel appraisallevel) {
        this.appraisallevel = appraisallevel;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
        if (!(object instanceof Appraisal)) {
            return false;
        }
        Appraisal other = (Appraisal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.original.evaluate.entity.Appraisal[ id=" + id + " ]";
    }
    
}
