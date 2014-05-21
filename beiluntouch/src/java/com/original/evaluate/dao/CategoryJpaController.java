/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.dao;

import com.original.evaluate.dao.exceptions.IllegalOrphanException;
import com.original.evaluate.dao.exceptions.NonexistentEntityException;
import com.original.evaluate.dao.exceptions.PreexistingEntityException;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Category;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.original.evaluate.entity.Employee;
import java.util.ArrayList;
import java.util.Collection;
import com.original.evaluate.entity.Notice;
import com.original.evaluate.entity.Room;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author kanehe
 */
public class CategoryJpaController implements Serializable {

    public CategoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Category category) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (category.getEmployeeCollection() == null) {
            category.setEmployeeCollection(new ArrayList<Employee>());
        }
        if (category.getNoticeCollection() == null) {
            category.setNoticeCollection(new ArrayList<Notice>());
        }
        if (category.getRoomCollection() == null) {
            category.setRoomCollection(new ArrayList<Room>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Employee> attachedEmployeeCollection = new ArrayList<Employee>();
            for (Employee employeeCollectionEmployeeToAttach : category.getEmployeeCollection()) {
                employeeCollectionEmployeeToAttach = em.getReference(employeeCollectionEmployeeToAttach.getClass(), employeeCollectionEmployeeToAttach.getId());
                attachedEmployeeCollection.add(employeeCollectionEmployeeToAttach);
            }
            category.setEmployeeCollection(attachedEmployeeCollection);
            Collection<Notice> attachedNoticeCollection = new ArrayList<Notice>();
            for (Notice noticeCollectionNoticeToAttach : category.getNoticeCollection()) {
                noticeCollectionNoticeToAttach = em.getReference(noticeCollectionNoticeToAttach.getClass(), noticeCollectionNoticeToAttach.getId());
                attachedNoticeCollection.add(noticeCollectionNoticeToAttach);
            }
            category.setNoticeCollection(attachedNoticeCollection);
            Collection<Room> attachedRoomCollection = new ArrayList<Room>();
            for (Room roomCollectionRoomToAttach : category.getRoomCollection()) {
                roomCollectionRoomToAttach = em.getReference(roomCollectionRoomToAttach.getClass(), roomCollectionRoomToAttach.getId());
                attachedRoomCollection.add(roomCollectionRoomToAttach);
            }
            category.setRoomCollection(attachedRoomCollection);
            em.persist(category);
            for (Employee employeeCollectionEmployee : category.getEmployeeCollection()) {
                Category oldCategoryOfEmployeeCollectionEmployee = employeeCollectionEmployee.getCategory();
                employeeCollectionEmployee.setCategory(category);
                employeeCollectionEmployee = em.merge(employeeCollectionEmployee);
                if (oldCategoryOfEmployeeCollectionEmployee != null) {
                    oldCategoryOfEmployeeCollectionEmployee.getEmployeeCollection().remove(employeeCollectionEmployee);
                    oldCategoryOfEmployeeCollectionEmployee = em.merge(oldCategoryOfEmployeeCollectionEmployee);
                }
            }
            for (Notice noticeCollectionNotice : category.getNoticeCollection()) {
                Category oldCategoryOfNoticeCollectionNotice = noticeCollectionNotice.getCategory();
                noticeCollectionNotice.setCategory(category);
                noticeCollectionNotice = em.merge(noticeCollectionNotice);
                if (oldCategoryOfNoticeCollectionNotice != null) {
                    oldCategoryOfNoticeCollectionNotice.getNoticeCollection().remove(noticeCollectionNotice);
                    oldCategoryOfNoticeCollectionNotice = em.merge(oldCategoryOfNoticeCollectionNotice);
                }
            }
            for (Room roomCollectionRoom : category.getRoomCollection()) {
                Category oldCategoryOfRoomCollectionRoom = roomCollectionRoom.getCategory();
                roomCollectionRoom.setCategory(category);
                roomCollectionRoom = em.merge(roomCollectionRoom);
                if (oldCategoryOfRoomCollectionRoom != null) {
                    oldCategoryOfRoomCollectionRoom.getRoomCollection().remove(roomCollectionRoom);
                    oldCategoryOfRoomCollectionRoom = em.merge(oldCategoryOfRoomCollectionRoom);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCategory(category.getId()) != null) {
                throw new PreexistingEntityException("Category " + category + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Category persistentCategory = em.find(Category.class, category.getId());
            Collection<Employee> employeeCollectionOld = persistentCategory.getEmployeeCollection();
            Collection<Employee> employeeCollectionNew = category.getEmployeeCollection();
            Collection<Notice> noticeCollectionOld = persistentCategory.getNoticeCollection();
            Collection<Notice> noticeCollectionNew = category.getNoticeCollection();
            Collection<Room> roomCollectionOld = persistentCategory.getRoomCollection();
            Collection<Room> roomCollectionNew = category.getRoomCollection();
            List<String> illegalOrphanMessages = null;
            for (Employee employeeCollectionOldEmployee : employeeCollectionOld) {
                if (!employeeCollectionNew.contains(employeeCollectionOldEmployee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Employee " + employeeCollectionOldEmployee + " since its category field is not nullable.");
                }
            }
            for (Notice noticeCollectionOldNotice : noticeCollectionOld) {
                if (!noticeCollectionNew.contains(noticeCollectionOldNotice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notice " + noticeCollectionOldNotice + " since its category field is not nullable.");
                }
            }
            for (Room roomCollectionOldRoom : roomCollectionOld) {
                if (!roomCollectionNew.contains(roomCollectionOldRoom)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Room " + roomCollectionOldRoom + " since its category field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Employee> attachedEmployeeCollectionNew = new ArrayList<Employee>();
            for (Employee employeeCollectionNewEmployeeToAttach : employeeCollectionNew) {
                employeeCollectionNewEmployeeToAttach = em.getReference(employeeCollectionNewEmployeeToAttach.getClass(), employeeCollectionNewEmployeeToAttach.getId());
                attachedEmployeeCollectionNew.add(employeeCollectionNewEmployeeToAttach);
            }
            employeeCollectionNew = attachedEmployeeCollectionNew;
            category.setEmployeeCollection(employeeCollectionNew);
            Collection<Notice> attachedNoticeCollectionNew = new ArrayList<Notice>();
            for (Notice noticeCollectionNewNoticeToAttach : noticeCollectionNew) {
                noticeCollectionNewNoticeToAttach = em.getReference(noticeCollectionNewNoticeToAttach.getClass(), noticeCollectionNewNoticeToAttach.getId());
                attachedNoticeCollectionNew.add(noticeCollectionNewNoticeToAttach);
            }
            noticeCollectionNew = attachedNoticeCollectionNew;
            category.setNoticeCollection(noticeCollectionNew);
            Collection<Room> attachedRoomCollectionNew = new ArrayList<Room>();
            for (Room roomCollectionNewRoomToAttach : roomCollectionNew) {
                roomCollectionNewRoomToAttach = em.getReference(roomCollectionNewRoomToAttach.getClass(), roomCollectionNewRoomToAttach.getId());
                attachedRoomCollectionNew.add(roomCollectionNewRoomToAttach);
            }
            roomCollectionNew = attachedRoomCollectionNew;
            category.setRoomCollection(roomCollectionNew);
            category = em.merge(category);
            for (Employee employeeCollectionNewEmployee : employeeCollectionNew) {
                if (!employeeCollectionOld.contains(employeeCollectionNewEmployee)) {
                    Category oldCategoryOfEmployeeCollectionNewEmployee = employeeCollectionNewEmployee.getCategory();
                    employeeCollectionNewEmployee.setCategory(category);
                    employeeCollectionNewEmployee = em.merge(employeeCollectionNewEmployee);
                    if (oldCategoryOfEmployeeCollectionNewEmployee != null && !oldCategoryOfEmployeeCollectionNewEmployee.equals(category)) {
                        oldCategoryOfEmployeeCollectionNewEmployee.getEmployeeCollection().remove(employeeCollectionNewEmployee);
                        oldCategoryOfEmployeeCollectionNewEmployee = em.merge(oldCategoryOfEmployeeCollectionNewEmployee);
                    }
                }
            }
            for (Notice noticeCollectionNewNotice : noticeCollectionNew) {
                if (!noticeCollectionOld.contains(noticeCollectionNewNotice)) {
                    Category oldCategoryOfNoticeCollectionNewNotice = noticeCollectionNewNotice.getCategory();
                    noticeCollectionNewNotice.setCategory(category);
                    noticeCollectionNewNotice = em.merge(noticeCollectionNewNotice);
                    if (oldCategoryOfNoticeCollectionNewNotice != null && !oldCategoryOfNoticeCollectionNewNotice.equals(category)) {
                        oldCategoryOfNoticeCollectionNewNotice.getNoticeCollection().remove(noticeCollectionNewNotice);
                        oldCategoryOfNoticeCollectionNewNotice = em.merge(oldCategoryOfNoticeCollectionNewNotice);
                    }
                }
            }
            for (Room roomCollectionNewRoom : roomCollectionNew) {
                if (!roomCollectionOld.contains(roomCollectionNewRoom)) {
                    Category oldCategoryOfRoomCollectionNewRoom = roomCollectionNewRoom.getCategory();
                    roomCollectionNewRoom.setCategory(category);
                    roomCollectionNewRoom = em.merge(roomCollectionNewRoom);
                    if (oldCategoryOfRoomCollectionNewRoom != null && !oldCategoryOfRoomCollectionNewRoom.equals(category)) {
                        oldCategoryOfRoomCollectionNewRoom.getRoomCollection().remove(roomCollectionNewRoom);
                        oldCategoryOfRoomCollectionNewRoom = em.merge(oldCategoryOfRoomCollectionNewRoom);
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
                Integer id = category.getId();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Employee> employeeCollectionOrphanCheck = category.getEmployeeCollection();
            for (Employee employeeCollectionOrphanCheckEmployee : employeeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Category (" + category + ") cannot be destroyed since the Employee " + employeeCollectionOrphanCheckEmployee + " in its employeeCollection field has a non-nullable category field.");
            }
            Collection<Notice> noticeCollectionOrphanCheck = category.getNoticeCollection();
            for (Notice noticeCollectionOrphanCheckNotice : noticeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Category (" + category + ") cannot be destroyed since the Notice " + noticeCollectionOrphanCheckNotice + " in its noticeCollection field has a non-nullable category field.");
            }
            Collection<Room> roomCollectionOrphanCheck = category.getRoomCollection();
            for (Room roomCollectionOrphanCheckRoom : roomCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Category (" + category + ") cannot be destroyed since the Room " + roomCollectionOrphanCheckRoom + " in its roomCollection field has a non-nullable category field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(category);
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

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Category> rt = cq.from(Category.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
