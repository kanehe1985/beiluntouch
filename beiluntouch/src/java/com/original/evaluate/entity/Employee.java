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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kanehe
 */
@Entity
@Table(name = "employee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id"),
    @NamedQuery(name = "Employee.findByName", query = "SELECT e FROM Employee e WHERE e.name = :name"),
    @NamedQuery(name = "Employee.findByRomno", query = "SELECT e FROM Employee e WHERE e.romno = :romno"),
    @NamedQuery(name = "Employee.findByIsallowappraisal", query = "SELECT e FROM Employee e WHERE e.isallowappraisal = :isallowappraisal"),
    @NamedQuery(name = "Employee.findByPicture", query = "SELECT e FROM Employee e WHERE e.picture = :picture"),
    @NamedQuery(name = "Employee.findByOrderid", query = "SELECT e FROM Employee e WHERE e.orderid = :orderid"),
    @NamedQuery(name = "Employee.findByTelephone", query = "SELECT e FROM Employee e WHERE e.telephone = :telephone")})
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "romno")
    private String romno;
    @Column(name = "isallowappraisal")
    private Boolean isallowappraisal;
    @Size(max = 255)
    @Column(name = "picture")
    private String picture;
    @Column(name = "orderid")
    private Integer orderid;
    @Size(max = 45)
    @Column(name = "telephone")
    private String telephone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<Appraisal> appraisalCollection;
    @JoinColumn(name = "category", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Category category;
    @JoinColumn(name = "department", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Department department;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<Notice> noticeCollection;

    public Employee() {
    }

    public Employee(Integer id) {
        this.id = id;
    }

    public Employee(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public String getRomno() {
        return romno;
    }

    public void setRomno(String romno) {
        this.romno = romno;
    }

    public Boolean getIsallowappraisal() {
        return isallowappraisal;
    }

    public void setIsallowappraisal(Boolean isallowappraisal) {
        this.isallowappraisal = isallowappraisal;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @XmlTransient
    public Collection<Appraisal> getAppraisalCollection() {
        return appraisalCollection;
    }

    public void setAppraisalCollection(Collection<Appraisal> appraisalCollection) {
        this.appraisalCollection = appraisalCollection;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @XmlTransient
    public Collection<Notice> getNoticeCollection() {
        return noticeCollection;
    }

    public void setNoticeCollection(Collection<Notice> noticeCollection) {
        this.noticeCollection = noticeCollection;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.original.evaluate.entity.Employee[ id=" + id + " ]";
    }
    
}
