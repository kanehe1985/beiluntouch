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
    @NamedQuery(name = "Employee.findBySex", query = "SELECT e FROM Employee e WHERE e.sex = :sex"),
    @NamedQuery(name = "Employee.findByIsallowappraisal", query = "SELECT e FROM Employee e WHERE e.isallowappraisal = :isallowappraisal"),
    @NamedQuery(name = "Employee.findByRom", query = "SELECT e FROM Employee e WHERE e.rom = :rom"),
    @NamedQuery(name = "Employee.findByGroup", query = "SELECT e FROM Employee e WHERE e.group = :group"),
    @NamedQuery(name = "Employee.findByRomno", query = "SELECT e FROM Employee e WHERE e.romno = :romno"),
    @NamedQuery(name = "Employee.findByGroupname", query = "SELECT e FROM Employee e WHERE e.groupname = :groupname")})
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "sex")
    private String sex;
    @Column(name = "isallowappraisal")
    private Boolean isallowappraisal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rom")
    private int rom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "group")
    private int group;
    @Size(max = 45)
    @Column(name = "romno")
    private String romno;
    @Size(max = 45)
    @Column(name = "groupname")
    private String groupname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private Collection<Appraisal> appraisalCollection;
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location locationId;

    public Employee() {
    }

    public Employee(Integer id) {
        this.id = id;
    }

    public Employee(Integer id, String name, String sex, int rom, int group) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.rom = rom;
        this.group = group;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Boolean getIsallowappraisal() {
        return isallowappraisal;
    }

    public void setIsallowappraisal(Boolean isallowappraisal) {
        this.isallowappraisal = isallowappraisal;
    }

    public int getRom() {
        return rom;
    }

    public void setRom(int rom) {
        this.rom = rom;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
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

    @XmlTransient
    public Collection<Appraisal> getAppraisalCollection() {
        return appraisalCollection;
    }

    public void setAppraisalCollection(Collection<Appraisal> appraisalCollection) {
        this.appraisalCollection = appraisalCollection;
    }

    public Location getLocationId() {
        return locationId;
    }

    public void setLocationId(Location locationId) {
        this.locationId = locationId;
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
