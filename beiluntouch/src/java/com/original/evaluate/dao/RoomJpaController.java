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
import com.original.evaluate.entity.Room;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author kanehe
 */
public class RoomJpaController implements Serializable {

    public RoomJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Room room) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Department department = room.getDepartment();
            if (department != null) {
                department = em.getReference(department.getClass(), department.getId());
                room.setDepartment(department);
            }
            Category category = room.getCategory();
            if (category != null) {
                category = em.getReference(category.getClass(), category.getId());
                room.setCategory(category);
            }
            em.persist(room);
            if (department != null) {
                department.getRoomCollection().add(room);
                department = em.merge(department);
            }
            if (category != null) {
                category.getRoomCollection().add(room);
                category = em.merge(category);
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

    public void edit(Room room) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Room persistentRoom = em.find(Room.class, room.getId());
            Department departmentOld = persistentRoom.getDepartment();
            Department departmentNew = room.getDepartment();
            Category categoryOld = persistentRoom.getCategory();
            Category categoryNew = room.getCategory();
            if (departmentNew != null) {
                departmentNew = em.getReference(departmentNew.getClass(), departmentNew.getId());
                room.setDepartment(departmentNew);
            }
            if (categoryNew != null) {
                categoryNew = em.getReference(categoryNew.getClass(), categoryNew.getId());
                room.setCategory(categoryNew);
            }
            room = em.merge(room);
            if (departmentOld != null && !departmentOld.equals(departmentNew)) {
                departmentOld.getRoomCollection().remove(room);
                departmentOld = em.merge(departmentOld);
            }
            if (departmentNew != null && !departmentNew.equals(departmentOld)) {
                departmentNew.getRoomCollection().add(room);
                departmentNew = em.merge(departmentNew);
            }
            if (categoryOld != null && !categoryOld.equals(categoryNew)) {
                categoryOld.getRoomCollection().remove(room);
                categoryOld = em.merge(categoryOld);
            }
            if (categoryNew != null && !categoryNew.equals(categoryOld)) {
                categoryNew.getRoomCollection().add(room);
                categoryNew = em.merge(categoryNew);
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
                Integer id = room.getId();
                if (findRoom(id) == null) {
                    throw new NonexistentEntityException("The room with id " + id + " no longer exists.");
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
            Room room;
            try {
                room = em.getReference(Room.class, id);
                room.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The room with id " + id + " no longer exists.", enfe);
            }
            Department department = room.getDepartment();
            if (department != null) {
                department.getRoomCollection().remove(room);
                department = em.merge(department);
            }
            Category category = room.getCategory();
            if (category != null) {
                category.getRoomCollection().remove(room);
                category = em.merge(category);
            }
            em.remove(room);
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

    public List<Room> findRoomEntities() {
        return findRoomEntities(true, -1, -1);
    }

    public List<Room> findRoomEntities(int maxResults, int firstResult) {
        return findRoomEntities(false, maxResults, firstResult);
    }

    private List<Room> findRoomEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Room.class));
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

    public Room findRoom(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Room.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoomCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Room> rt = cq.from(Room.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
