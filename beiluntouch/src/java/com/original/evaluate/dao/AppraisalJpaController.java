/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.dao;

import com.original.evaluate.dao.exceptions.NonexistentEntityException;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Appraisal;
import com.original.evaluate.entity.Appraisallevel;
import com.original.evaluate.entity.Employee;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author kanehe
 */
public class AppraisalJpaController implements Serializable {

    public AppraisalJpaController(EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Appraisal appraisal) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employee = appraisal.getEmployee();
            if (employee != null) {
                employee = em.getReference(employee.getClass(), employee.getId());
                appraisal.setEmployee(employee);
            }
            Appraisallevel appraisallevel = appraisal.getAppraisallevel();
            if (appraisallevel != null) {
                appraisallevel = em.getReference(appraisallevel.getClass(), appraisallevel.getId());
                appraisal.setAppraisallevel(appraisallevel);
            }
            em.persist(appraisal);
//            if (employee != null) {
//                employee.getAppraisalCollection().add(appraisal);
//                employee = em.merge(employee);
//            }
//            if (appraisallevel != null) {
//                appraisallevel.getAppraisalCollection().add(appraisal);
//                appraisallevel = em.merge(appraisallevel);
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Appraisal appraisal) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Appraisal persistentAppraisal = em.find(Appraisal.class, appraisal.getId());
            Employee employeeOld = persistentAppraisal.getEmployee();
            Employee employeeNew = appraisal.getEmployee();
            Appraisallevel appraisallevelOld = persistentAppraisal.getAppraisallevel();
            Appraisallevel appraisallevelNew = appraisal.getAppraisallevel();
            if (employeeNew != null) {
                employeeNew = em.getReference(employeeNew.getClass(), employeeNew.getId());
                appraisal.setEmployee(employeeNew);
            }
            if (appraisallevelNew != null) {
                appraisallevelNew = em.getReference(appraisallevelNew.getClass(), appraisallevelNew.getId());
                appraisal.setAppraisallevel(appraisallevelNew);
            }
            appraisal = em.merge(appraisal);
//            if (employeeOld != null && !employeeOld.equals(employeeNew)) {
//                employeeOld.getAppraisalCollection().remove(appraisal);
//                employeeOld = em.merge(employeeOld);
//            }
//            if (employeeNew != null && !employeeNew.equals(employeeOld)) {
//                employeeNew.getAppraisalCollection().add(appraisal);
//                employeeNew = em.merge(employeeNew);
//            }
//            if (appraisallevelOld != null && !appraisallevelOld.equals(appraisallevelNew)) {
//                appraisallevelOld.getAppraisalCollection().remove(appraisal);
//                appraisallevelOld = em.merge(appraisallevelOld);
//            }
//            if (appraisallevelNew != null && !appraisallevelNew.equals(appraisallevelOld)) {
//                appraisallevelNew.getAppraisalCollection().add(appraisal);
//                appraisallevelNew = em.merge(appraisallevelNew);
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = appraisal.getId();
                if (findAppraisal(id) == null) {
                    throw new NonexistentEntityException("The appraisal with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Appraisal appraisal;
            try {
                appraisal = em.getReference(Appraisal.class, id);
                appraisal.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The appraisal with id " + id + " no longer exists.", enfe);
            }
//            Employee employee = appraisal.getEmployee();
//            if (employee != null) {
//                employee.getAppraisalCollection().remove(appraisal);
//                employee = em.merge(employee);
//            }
//            Appraisallevel appraisallevel = appraisal.getAppraisallevel();
//            if (appraisallevel != null) {
//                appraisallevel.getAppraisalCollection().remove(appraisal);
//                appraisallevel = em.merge(appraisallevel);
//            }
            em.remove(appraisal);
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Appraisal> findAppraisalEntities() {
        return findAppraisalEntities(true, -1, -1);
    }

    public List<Appraisal> findAppraisalEntities(int maxResults, int firstResult) {
        return findAppraisalEntities(false, maxResults, firstResult);
    }
            
    public List<Appraisal> findAppraisalEntitiesByDuration(Date beginDate, Date endDate) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Appraisal> appraisal = cq.from(Appraisal.class);
            Path<Date> createdate = appraisal.get("createdate");
            
            List<Predicate> plist = new ArrayList<>();
            if(beginDate != null){
                Predicate pBeginDate = cb.greaterThanOrEqualTo(createdate, beginDate);
                plist.add(pBeginDate);
            }            
            
            if(endDate != null){
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(endDate);
                rightNow.add(Calendar.DAY_OF_YEAR, 1);
                Predicate pEndDate = cb.lessThanOrEqualTo(createdate, rightNow.getTime());
                plist.add(pEndDate);
            }
            
            Predicate[] parray = new Predicate[plist.size()];
            plist.toArray(parray);
            Predicate p = cb.and(parray);
            
            cq.where(p);
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
            
    private List<Appraisal> findAppraisalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Appraisal.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Appraisal findAppraisal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Appraisal.class, id);
        } finally {
            em.close();
        }
    }
    
    public List exeSQL(String sql){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery(sql);
        return query.getResultList();
    }

    public int getAppraisalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Appraisal> rt = cq.from(Appraisal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
