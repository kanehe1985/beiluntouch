/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.dao;

import com.original.evaluate.dao.exceptions.NonexistentEntityException;
import com.original.evaluate.dao.exceptions.PreexistingEntityException;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Adminuser;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.original.evaluate.entity.Department;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Kane
 */
public class AdminuserJpaController implements Serializable {

    public AdminuserJpaController(EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Adminuser adminuser) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            Department department = adminuser.getDepartment();
            if (department != null) {
                department = em.getReference(department.getClass(), department.getId());
                adminuser.setDepartment(department);
            }
            em.persist(adminuser);
            if (department != null) {
                department.getAdminuserCollection().add(adminuser);
                department = em.merge(department);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAdminuser(adminuser.getId()) != null) {
                throw new PreexistingEntityException("Adminuser " + adminuser + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Adminuser adminuser) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            Adminuser persistentAdminuser = em.find(Adminuser.class, adminuser.getId());
            Department departmentOld = persistentAdminuser.getDepartment();
            Department departmentNew = adminuser.getDepartment();
            if (departmentNew != null) {
                departmentNew = em.getReference(departmentNew.getClass(), departmentNew.getId());
                adminuser.setDepartment(departmentNew);
            }
            adminuser = em.merge(adminuser);
            if (departmentOld != null && !departmentOld.equals(departmentNew)) {
                departmentOld.getAdminuserCollection().remove(adminuser);
                departmentOld = em.merge(departmentOld);
            }
            if (departmentNew != null && !departmentNew.equals(departmentOld)) {
                departmentNew.getAdminuserCollection().add(adminuser);
                departmentNew = em.merge(departmentNew);
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
                Integer id = adminuser.getId();
                if (findAdminuser(id) == null) {
                    throw new NonexistentEntityException("The adminuser with id " + id + " no longer exists.");
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
            
            Adminuser adminuser;
            try {
                adminuser = em.getReference(Adminuser.class, id);
                adminuser.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adminuser with id " + id + " no longer exists.", enfe);
            }
            Department department = adminuser.getDepartment();
            if (department != null) {
                department.getAdminuserCollection().remove(adminuser);
                department = em.merge(department);
            }
            em.remove(adminuser);
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

    public List<Adminuser> findAdminuserEntities() {
        return findAdminuserEntities(true, -1, -1);
    }

    public List<Adminuser> findAdminuserEntities(int maxResults, int firstResult) {
        return findAdminuserEntities(false, maxResults, firstResult);
    }

    private List<Adminuser> findAdminuserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Adminuser.class));
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

    public Adminuser findAdminuser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Adminuser.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdminuserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Adminuser> rt = cq.from(Adminuser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
