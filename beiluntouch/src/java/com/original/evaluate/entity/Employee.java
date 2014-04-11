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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

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
    @NamedQuery(name = "Employee.findByGroupname", query = "SELECT e FROM Employee e WHERE e.groupname = :groupname"),
    @NamedQuery(name = "Employee.findByIsallowappraisal", query = "SELECT e FROM Employee e WHERE e.isallowappraisal = :isallowappraisal"),
    @NamedQuery(name = "Employee.findByPicture", query = "SELECT e FROM Employee e WHERE e.picture = :picture")})
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
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
    @Size(max = 45)
    @Column(name = "groupname")
    private String groupname;
    @Column(name = "isallowappraisal")
    private Boolean isallowappraisal;
    @Size(max = 255)
    @Column(name = "picture")
    private String picture;
    @JoinColumn(name = "department", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Department department;

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

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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
