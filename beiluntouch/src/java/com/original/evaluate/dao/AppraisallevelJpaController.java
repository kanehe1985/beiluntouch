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
import com.original.evaluate.entity.Appraisal;
import com.original.evaluate.entity.Appraisallevel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author dxx
 */
public class AppraisallevelJpaController implements Serializable {

    public AppraisallevelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Appraisallevel appraisallevel) throws RollbackFailureException, Exception {
        if (appraisallevel.getAppraisalCollection() == null) {
            appraisallevel.setAppraisalCollection(new ArrayList<>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Appraisal> attachedAppraisalCollection = new ArrayList<>();
            for (Appraisal appraisalCollectionAppraisalToAttach : appraisallevel.getAppraisalCollection()) {
                appraisalCollectionAppraisalToAttach = em.getReference(appraisalCollectionAppraisalToAttach.getClass(), appraisalCollectionAppraisalToAttach.getId());
                attachedAppraisalCollection.add(appraisalCollectionAppraisalToAttach);
            }
            appraisallevel.setAppraisalCollection(attachedAppraisalCollection);
            em.persist(appraisallevel);
            for (Appraisal appraisalCollectionAppraisal : appraisallevel.getAppraisalCollection()) {
                Appraisallevel oldAppraisallevelOfAppraisalCollectionAppraisal = appraisalCollectionAppraisal.getAppraisallevel();
                appraisalCollectionAppraisal.setAppraisallevel(appraisallevel);
                appraisalCollectionAppraisal = em.merge(appraisalCollectionAppraisal);
                if (oldAppraisallevelOfAppraisalCollectionAppraisal != null) {
                    oldAppraisallevelOfAppraisalCollectionAppraisal.getAppraisalCollection().remove(appraisalCollectionAppraisal);
                    oldAppraisallevelOfAppraisalCollectionAppraisal = em.merge(oldAppraisallevelOfAppraisalCollectionAppraisal);
                }
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

    public void edit(Appraisallevel appraisallevel) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Appraisallevel persistentAppraisallevel = em.find(Appraisallevel.class, appraisallevel.getId());
            Collection<Appraisal> appraisalCollectionOld = persistentAppraisallevel.getAppraisalCollection();
            Collection<Appraisal> appraisalCollectionNew = appraisallevel.getAppraisalCollection();
            List<String> illegalOrphanMessages = null;
            for (Appraisal appraisalCollectionOldAppraisal : appraisalCollectionOld) {
                if (!appraisalCollectionNew.contains(appraisalCollectionOldAppraisal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Appraisal " + appraisalCollectionOldAppraisal + " since its appraisallevel field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Appraisal> attachedAppraisalCollectionNew = new ArrayList<>();
            for (Appraisal appraisalCollectionNewAppraisalToAttach : appraisalCollectionNew) {
                appraisalCollectionNewAppraisalToAttach = em.getReference(appraisalCollectionNewAppraisalToAttach.getClass(), appraisalCollectionNewAppraisalToAttach.getId());
                attachedAppraisalCollectionNew.add(appraisalCollectionNewAppraisalToAttach);
            }
            appraisalCollectionNew = attachedAppraisalCollectionNew;
            appraisallevel.setAppraisalCollection(appraisalCollectionNew);
            appraisallevel = em.merge(appraisallevel);
            for (Appraisal appraisalCollectionNewAppraisal : appraisalCollectionNew) {
                if (!appraisalCollectionOld.contains(appraisalCollectionNewAppraisal)) {
                    Appraisallevel oldAppraisallevelOfAppraisalCollectionNewAppraisal = appraisalCollectionNewAppraisal.getAppraisallevel();
                    appraisalCollectionNewAppraisal.setAppraisallevel(appraisallevel);
                    appraisalCollectionNewAppraisal = em.merge(appraisalCollectionNewAppraisal);
                    if (oldAppraisallevelOfAppraisalCollectionNewAppraisal != null && !oldAppraisallevelOfAppraisalCollectionNewAppraisal.equals(appraisallevel)) {
                        oldAppraisallevelOfAppraisalCollectionNewAppraisal.getAppraisalCollection().remove(appraisalCollectionNewAppraisal);
                        oldAppraisallevelOfAppraisalCollectionNewAppraisal = em.merge(oldAppraisallevelOfAppraisalCollectionNewAppraisal);
                    }
                }
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
                Integer id = appraisallevel.getId();
                if (findAppraisallevel(id) == null) {
                    throw new NonexistentEntityException("The appraisallevel with id " + id + " no longer exists.");
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
            em = getEntityManager();
            em.getTransaction().begin();
            Appraisallevel appraisallevel;
            try {
                appraisallevel = em.getReference(Appraisallevel.class, id);
                appraisallevel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The appraisallevel with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Appraisal> appraisalCollectionOrphanCheck = appraisallevel.getAppraisalCollection();
            for (Appraisal appraisalCollectionOrphanCheckAppraisal : appraisalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("This Appraisallevel (" + appraisallevel + ") cannot be destroyed since the Appraisal " + appraisalCollectionOrphanCheckAppraisal + " in its appraisalCollection field has a non-nullable appraisallevel field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(appraisallevel);
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

    public List<Appraisallevel> findAppraisallevelEntities() {
        return findAppraisallevelEntities(true, -1, -1);
    }

    public List<Appraisallevel> findAppraisallevelEntities(int maxResults, int firstResult) {
        return findAppraisallevelEntities(false, maxResults, firstResult);
    }

    private List<Appraisallevel> findAppraisallevelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Appraisallevel.class));
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

    public Appraisallevel findAppraisallevel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Appraisallevel.class, id);
        } finally {
            em.close();
        }
    }

    public int getAppraisallevelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Appraisallevel> rt = cq.from(Appraisallevel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
