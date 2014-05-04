/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.dao;

import com.original.evaluate.dao.exceptions.NonexistentEntityException;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.original.evaluate.entity.Department;
import com.original.evaluate.entity.Category;
import com.original.evaluate.entity.Employee;
import com.original.evaluate.entity.Notice;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author kanehe
 */
public class NoticeJpaController implements Serializable {

    public NoticeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Notice notice) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {            
            em = getEntityManager();
            em.getTransaction().begin();
            Department department = notice.getDepartment();
            if (department != null) {
                department = em.getReference(department.getClass(), department.getId());
                notice.setDepartment(department);
            }
            Category category = notice.getCategory();
            if (category != null) {
                category = em.getReference(category.getClass(), category.getId());
                notice.setCategory(category);
            }
            Employee employee = notice.getEmployee();
            if (employee != null) {
                employee = em.getReference(employee.getClass(), employee.getId());
                notice.setEmployee(employee);
            }
            em.persist(notice);
            if (department != null) {
                department.getNoticeCollection().add(notice);
                department = em.merge(department);
            }
            if (category != null) {
                category.getNoticeCollection().add(notice);
                category = em.merge(category);
            }
            if (employee != null) {
                employee.getNoticeCollection().add(notice);
                employee = em.merge(employee);
            }
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

    public void edit(Notice notice) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Notice persistentNotice = em.find(Notice.class, notice.getId());
            Department departmentOld = persistentNotice.getDepartment();
            Department departmentNew = notice.getDepartment();
            Category categoryOld = persistentNotice.getCategory();
            Category categoryNew = notice.getCategory();
            Employee employeeOld = persistentNotice.getEmployee();
            Employee employeeNew = notice.getEmployee();
            if (departmentNew != null) {
                departmentNew = em.getReference(departmentNew.getClass(), departmentNew.getId());
                notice.setDepartment(departmentNew);
            }
            if (categoryNew != null) {
                categoryNew = em.getReference(categoryNew.getClass(), categoryNew.getId());
                notice.setCategory(categoryNew);
            }
            if (employeeNew != null) {
                employeeNew = em.getReference(employeeNew.getClass(), employeeNew.getId());
                notice.setEmployee(employeeNew);
            }
            notice = em.merge(notice);
            if (departmentOld != null && !departmentOld.equals(departmentNew)) {
                departmentOld.getNoticeCollection().remove(notice);
                departmentOld = em.merge(departmentOld);
            }
            if (departmentNew != null && !departmentNew.equals(departmentOld)) {
                departmentNew.getNoticeCollection().add(notice);
                departmentNew = em.merge(departmentNew);
            }
            if (categoryOld != null && !categoryOld.equals(categoryNew)) {
                categoryOld.getNoticeCollection().remove(notice);
                categoryOld = em.merge(categoryOld);
            }
            if (categoryNew != null && !categoryNew.equals(categoryOld)) {
                categoryNew.getNoticeCollection().add(notice);
                categoryNew = em.merge(categoryNew);
            }
            if (employeeOld != null && !employeeOld.equals(employeeNew)) {
                employeeOld.getNoticeCollection().remove(notice);
                employeeOld = em.merge(employeeOld);
            }
            if (employeeNew != null && !employeeNew.equals(employeeOld)) {
                employeeNew.getNoticeCollection().add(notice);
                employeeNew = em.merge(employeeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = notice.getId();
                if (findNotice(id) == null) {
                    throw new NonexistentEntityException("The notice with id " + id + " no longer exists.");
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
            Notice notice;
            try {
                notice = em.getReference(Notice.class, id);
                notice.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The notice with id " + id + " no longer exists.", enfe);
            }
            Department department = notice.getDepartment();
            if (department != null) {
                department.getNoticeCollection().remove(notice);
                department = em.merge(department);
            }
            Category category = notice.getCategory();
            if (category != null) {
                category.getNoticeCollection().remove(notice);
                category = em.merge(category);
            }
            Employee employee = notice.getEmployee();
            if (employee != null) {
                employee.getNoticeCollection().remove(notice);
                employee = em.merge(employee);
            }
            em.remove(notice);
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

    public List<Notice> findNoticeEntities() {
        return findNoticeEntities(true, -1, -1);
    }

    public List<Notice> findNoticeEntities(int maxResults, int firstResult) {
        return findNoticeEntities(false, maxResults, firstResult);
    }

    private List<Notice> findNoticeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Notice.class));
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

    public Notice findNotice(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Notice.class, id);
        } finally {
            em.close();
        }
    }

    public int getNoticeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Notice> rt = cq.from(Notice.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
