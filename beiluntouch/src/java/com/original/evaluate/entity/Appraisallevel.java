/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kanehe
 */
@Entity
@Table(name = "appraisallevel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Appraisallevel.findAll", query = "SELECT a FROM Appraisallevel a"),
    @NamedQuery(name = "Appraisallevel.findById", query = "SELECT a FROM Appraisallevel a WHERE a.id = :id"),
    @NamedQuery(name = "Appraisallevel.findByName", query = "SELECT a FROM Appraisallevel a WHERE a.name = :name"),
    @NamedQuery(name = "Appraisallevel.findByIsalert", query = "SELECT a FROM Appraisallevel a WHERE a.isalert = :isalert")})
public class Appraisallevel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @Column(name = "isalert")
    private Boolean isalert;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "appraisallevel")
    private Collection<Appraisal> appraisalCollection;

    public Appraisallevel() {
    }

    public Appraisallevel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsalert() {
        return isalert;
    }

    public void setIsalert(Boolean isalert) {
        this.isalert = isalert;
    }

    @XmlTransient
    public Collection<Appraisal> getAppraisalCollection() {
        return appraisalCollection;
    }

    public void setAppraisalCollection(Collection<Appraisal> appraisalCollection) {
        this.appraisalCollection = appraisalCollection;
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
        if (!(object instanceof Appraisallevel)) {
            return false;
        }
        Appraisallevel other = (Appraisallevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.original.evaluate.entity.Appraisallevel[ id=" + id + " ]";
    }
    
}
