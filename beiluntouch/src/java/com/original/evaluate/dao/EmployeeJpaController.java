/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.dao;

import com.original.evaluate.dao.exceptions.IllegalOrphanException;
import com.original.evaluate.dao.exceptions.NonexistentEntityException;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.original.evaluate.entity.Location;
import com.original.evaluate.entity.Appraisal;
import com.original.evaluate.entity.Employee;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author kanehe
 */
public class EmployeeJpaController implements Serializable {

    public EmployeeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Employee employee) throws RollbackFailureException, Exception {
        if (employee.getAppraisalCollection() == null) {
            employee.setAppraisalCollection(new ArrayList<Appraisal>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Location locationId = employee.getLocationId();
            if (locationId != null) {
                locationId = em.getReference(locationId.getClass(), locationId.getId());
                employee.setLocationId(locationId);
            }
            Collection<Appraisal> attachedAppraisalCollection = new ArrayList<Appraisal>();
            for (Appraisal appraisalCollectionAppraisalToAttach : employee.getAppraisalCollection()) {
                appraisalCollectionAppraisalToAttach = em.getReference(appraisalCollectionAppraisalToAttach.getClass(), appraisalCollectionAppraisalToAttach.getId());
                attachedAppraisalCollection.add(appraisalCollectionAppraisalToAttach);
            }
            employee.setAppraisalCollection(attachedAppraisalCollection);
            em.persist(employee);
            if (locationId != null) {
                locationId.getEmployeeCollection().add(employee);
                locationId = em.merge(locationId);
            }
            for (Appraisal appraisalCollectionAppraisal : employee.getAppraisalCollection()) {
                Employee oldEmployeeOfAppraisalCollectionAppraisal = appraisalCollectionAppraisal.getEmployee();
                appraisalCollectionAppraisal.setEmployee(employee);
                appraisalCollectionAppraisal = em.merge(appraisalCollectionAppraisal);
                if (oldEmployeeOfAppraisalCollectionAppraisal != null) {
                    oldEmployeeOfAppraisalCollectionAppraisal.getAppraisalCollection().remove(appraisalCollectionAppraisal);
                    oldEmployeeOfAppraisalCollectionAppraisal = em.merge(oldEmployeeOfAppraisalCollectionAppraisal);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
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

    public void edit(Employee employee) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Employee persistentEmployee = em.find(Employee.class, employee.getId());
            Location locationIdOld = persistentEmployee.getLocationId();
            Location locationIdNew = employee.getLocationId();
            Collection<Appraisal> appraisalCollectionOld = persistentEmployee.getAppraisalCollection();
            Collection<Appraisal> appraisalCollectionNew = employee.getAppraisalCollection();
            List<String> illegalOrphanMessages = null;
            for (Appraisal appraisalCollectionOldAppraisal : appraisalCollectionOld) {
                if (!appraisalCollectionNew.contains(appraisalCollectionOldAppraisal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Appraisal " + appraisalCollectionOldAppraisal + " since its employee field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (locationIdNew != null) {
                locationIdNew = em.getReference(locationIdNew.getClass(), locationIdNew.getId());
                employee.setLocationId(locationIdNew);
            }
            Collection<Appraisal> attachedAppraisalCollectionNew = new ArrayList<Appraisal>();
            for (Appraisal appraisalCollectionNewAppraisalToAttach : appraisalCollectionNew) {
                appraisalCollectionNewAppraisalToAttach = em.getReference(appraisalCollectionNewAppraisalToAttach.getClass(), appraisalCollectionNewAppraisalToAttach.getId());
                attachedAppraisalCollectionNew.add(appraisalCollectionNewAppraisalToAttach);
            }
            appraisalCollectionNew = attachedAppraisalCollectionNew;
            employee.setAppraisalCollection(appraisalCollectionNew);
            employee = em.merge(employee);
            if (locationIdOld != null && !locationIdOld.equals(locationIdNew)) {
                locationIdOld.getEmployeeCollection().remove(employee);
                locationIdOld = em.merge(locationIdOld);
            }
            if (locationIdNew != null && !locationIdNew.equals(locationIdOld)) {
                locationIdNew.getEmployeeCollection().add(employee);
                locationIdNew = em.merge(locationIdNew);
            }
            for (Appraisal appraisalCollectionNewAppraisal : appraisalCollectionNew) {
                if (!appraisalCollectionOld.contains(appraisalCollectionNewAppraisal)) {
                    Employee oldEmployeeOfAppraisalCollectionNewAppraisal = appraisalCollectionNewAppraisal.getEmployee();
                    appraisalCollectionNewAppraisal.setEmployee(employee);
                    appraisalCollectionNewAppraisal = em.merge(appraisalCollectionNewAppraisal);
                    if (oldEmployeeOfAppraisalCollectionNewAppraisal != null && !oldEmployeeOfAppraisalCollectionNewAppraisal.equals(employee)) {
                        oldEmployeeOfAppraisalCollectionNewAppraisal.getAppraisalCollection().remove(appraisalCollectionNewAppraisal);
                        oldEmployeeOfAppraisalCollectionNewAppraisal = em.merge(oldEmployeeOfAppraisalCollectionNewAppraisal);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = employee.getId();
                if (findEmployee(id) == null) {
                    throw new NonexistentEntityException("The employee with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Employee employee;
            try {
                employee = em.getReference(Employee.class, id);
                employee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Appraisal> appraisalCollectionOrphanCheck = employee.getAppraisalCollection();
            for (Appraisal appraisalCollectionOrphanCheckAppraisal : appraisalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the Appraisal " + appraisalCollectionOrphanCheckAppraisal + " in its appraisalCollection field has a non-nullable employee field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Location locationId = employee.getLocationId();
            if (locationId != null) {
                locationId.getEmployeeCollection().remove(employee);
                locationId = em.merge(locationId);
            }
            em.remove(employee);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
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

    public List<Employee> findEmployeeEntities() {
        return findEmployeeEntities(true, -1, -1);
    }

    public List<Employee> findEmployeeEntities(int maxResults, int firstResult) {
        return findEmployeeEntities(false, maxResults, firstResult);
    }

    private List<Employee> findEmployeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Employee.class));
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

    public Employee findEmployee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmployeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> rt = cq.from(Employee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
