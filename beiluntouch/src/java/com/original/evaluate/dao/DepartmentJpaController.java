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
import com.original.evaluate.entity.Department;
import com.original.evaluate.entity.Employee;
import com.original.evaluate.entity.Notice;
import com.original.evaluate.entity.Room;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author kanehe
 */
public class DepartmentJpaController implements Serializable {

    public DepartmentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Department department) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (department.getEmployeeCollection() == null) {
            department.setEmployeeCollection(new ArrayList<Employee>());
        }
        if (department.getNoticeCollection() == null) {
            department.setNoticeCollection(new ArrayList<Notice>());
        }
		if (department.getRoomCollection() == null) {
            department.setRoomCollection(new ArrayList<Room>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Employee> attachedEmployeeCollection = new ArrayList<Employee>();
            for (Employee employeeCollectionEmployeeToAttach : department.getEmployeeCollection()) {
                employeeCollectionEmployeeToAttach = em.getReference(employeeCollectionEmployeeToAttach.getClass(), employeeCollectionEmployeeToAttach.getId());
                attachedEmployeeCollection.add(employeeCollectionEmployeeToAttach);
            }
            department.setEmployeeCollection(attachedEmployeeCollection);
            Collection<Notice> attachedNoticeCollection = new ArrayList<Notice>();
            for (Notice noticeCollectionNoticeToAttach : department.getNoticeCollection()) {
                noticeCollectionNoticeToAttach = em.getReference(noticeCollectionNoticeToAttach.getClass(), noticeCollectionNoticeToAttach.getId());
                attachedNoticeCollection.add(noticeCollectionNoticeToAttach);
            }
            department.setNoticeCollection(attachedNoticeCollection);
			Collection<Room> attachedRoomCollection = new ArrayList<Room>();
            for (Room roomCollectionRoomToAttach : department.getRoomCollection()) {
                roomCollectionRoomToAttach = em.getReference(roomCollectionRoomToAttach.getClass(), roomCollectionRoomToAttach.getId());
                attachedRoomCollection.add(roomCollectionRoomToAttach);
            }
			department.setRoomCollection(attachedRoomCollection);
            em.persist(department);
            for (Employee employeeCollectionEmployee : department.getEmployeeCollection()) {
                Department oldDepartmentOfEmployeeCollectionEmployee = employeeCollectionEmployee.getDepartment();
                employeeCollectionEmployee.setDepartment(department);
                employeeCollectionEmployee = em.merge(employeeCollectionEmployee);
                if (oldDepartmentOfEmployeeCollectionEmployee != null) {
                    oldDepartmentOfEmployeeCollectionEmployee.getEmployeeCollection().remove(employeeCollectionEmployee);
                    oldDepartmentOfEmployeeCollectionEmployee = em.merge(oldDepartmentOfEmployeeCollectionEmployee);
                }
            }
            for (Notice noticeCollectionNotice : department.getNoticeCollection()) {
                Department oldDepartmentOfNoticeCollectionNotice = noticeCollectionNotice.getDepartment();
                noticeCollectionNotice.setDepartment(department);
                noticeCollectionNotice = em.merge(noticeCollectionNotice);
                if (oldDepartmentOfNoticeCollectionNotice != null) {
                    oldDepartmentOfNoticeCollectionNotice.getNoticeCollection().remove(noticeCollectionNotice);
                    oldDepartmentOfNoticeCollectionNotice = em.merge(oldDepartmentOfNoticeCollectionNotice);
                }
            }
			for (Room roomCollectionRoom : department.getRoomCollection()) {
                Department oldDepartmentOfRoomCollectionRoom = roomCollectionRoom.getDepartment();
                roomCollectionRoom.setDepartment(department);
                roomCollectionRoom = em.merge(roomCollectionRoom);
                if (oldDepartmentOfRoomCollectionRoom != null) {
                    oldDepartmentOfRoomCollectionRoom.getRoomCollection().remove(roomCollectionRoom);
                    oldDepartmentOfRoomCollectionRoom = em.merge(oldDepartmentOfRoomCollectionRoom);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDepartment(department.getId()) != null) {
                throw new PreexistingEntityException("Department " + department + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Department department) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Department persistentDepartment = em.find(Department.class, department.getId());
            Collection<Employee> employeeCollectionOld = persistentDepartment.getEmployeeCollection();
            Collection<Employee> employeeCollectionNew = department.getEmployeeCollection();
            Collection<Notice> noticeCollectionOld = persistentDepartment.getNoticeCollection();
            Collection<Notice> noticeCollectionNew = department.getNoticeCollection();
			Collection<Room> roomCollectionOld = persistentDepartment.getRoomCollection();
            Collection<Room> roomCollectionNew = department.getRoomCollection();
           	List<String> illegalOrphanMessages = null;
            for (Employee employeeCollectionOldEmployee : employeeCollectionOld) {
                if (!employeeCollectionNew.contains(employeeCollectionOldEmployee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Employee " + employeeCollectionOldEmployee + " since its department field is not nullable.");
                }
            }
            for (Notice noticeCollectionOldNotice : noticeCollectionOld) {
                if (!noticeCollectionNew.contains(noticeCollectionOldNotice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notice " + noticeCollectionOldNotice + " since its department field is not nullable.");
                }
            }
			for (Room roomCollectionOldRoom : roomCollectionOld) {
                if (!roomCollectionNew.contains(roomCollectionOldRoom)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Room " + roomCollectionOldRoom + " since its department field is not nullable.");
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
            department.setEmployeeCollection(employeeCollectionNew);
            Collection<Notice> attachedNoticeCollectionNew = new ArrayList<Notice>();
            for (Notice noticeCollectionNewNoticeToAttach : noticeCollectionNew) {
                noticeCollectionNewNoticeToAttach = em.getReference(noticeCollectionNewNoticeToAttach.getClass(), noticeCollectionNewNoticeToAttach.getId());
                attachedNoticeCollectionNew.add(noticeCollectionNewNoticeToAttach);
            }
            noticeCollectionNew = attachedNoticeCollectionNew;
            department.setNoticeCollection(noticeCollectionNew);
            Collection<Room> attachedRoomCollectionNew = new ArrayList<Room>();
            for (Room roomCollectionNewRoomToAttach : roomCollectionNew) {
                roomCollectionNewRoomToAttach = em.getReference(roomCollectionNewRoomToAttach.getClass(), roomCollectionNewRoomToAttach.getId());
                attachedRoomCollectionNew.add(roomCollectionNewRoomToAttach);
            }
            roomCollectionNew = attachedRoomCollectionNew;
            department.setRoomCollection(roomCollectionNew);
            department = em.merge(department);
            for (Employee employeeCollectionNewEmployee : employeeCollectionNew) {
                if (!employeeCollectionOld.contains(employeeCollectionNewEmployee)) {
                    Department oldDepartmentOfEmployeeCollectionNewEmployee = employeeCollectionNewEmployee.getDepartment();
                    employeeCollectionNewEmployee.setDepartment(department);
                    employeeCollectionNewEmployee = em.merge(employeeCollectionNewEmployee);
                    if (oldDepartmentOfEmployeeCollectionNewEmployee != null && !oldDepartmentOfEmployeeCollectionNewEmployee.equals(department)) {
                        oldDepartmentOfEmployeeCollectionNewEmployee.getEmployeeCollection().remove(employeeCollectionNewEmployee);
                        oldDepartmentOfEmployeeCollectionNewEmployee = em.merge(oldDepartmentOfEmployeeCollectionNewEmployee);
                    }
                }
            }
            for (Notice noticeCollectionNewNotice : noticeCollectionNew) {
                if (!noticeCollectionOld.contains(noticeCollectionNewNotice)) {
                    Department oldDepartmentOfNoticeCollectionNewNotice = noticeCollectionNewNotice.getDepartment();
                    noticeCollectionNewNotice.setDepartment(department);
                    noticeCollectionNewNotice = em.merge(noticeCollectionNewNotice);
                    if (oldDepartmentOfNoticeCollectionNewNotice != null && !oldDepartmentOfNoticeCollectionNewNotice.equals(department)) {
                        oldDepartmentOfNoticeCollectionNewNotice.getNoticeCollection().remove(noticeCollectionNewNotice);
                        oldDepartmentOfNoticeCollectionNewNotice = em.merge(oldDepartmentOfNoticeCollectionNewNotice);
                    }
                }
            }
			for (Room roomCollectionNewRoom : roomCollectionNew) {
                if (!roomCollectionOld.contains(roomCollectionNewRoom)) {
                    Department oldDepartmentOfRoomCollectionNewRoom = roomCollectionNewRoom.getDepartment();
                    roomCollectionNewRoom.setDepartment(department);
                    roomCollectionNewRoom = em.merge(roomCollectionNewRoom);
                    if (oldDepartmentOfRoomCollectionNewRoom != null && !oldDepartmentOfRoomCollectionNewRoom.equals(department)) {
                        oldDepartmentOfRoomCollectionNewRoom.getRoomCollection().remove(roomCollectionNewRoom);
                        oldDepartmentOfRoomCollectionNewRoom = em.merge(oldDepartmentOfRoomCollectionNewRoom);
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
                Integer id = department.getId();
                if (findDepartment(id) == null) {
                    throw new NonexistentEntityException("The department with id " + id + " no longer exists.");
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
            Department department;
            try {
                department = em.getReference(Department.class, id);
                department.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The department with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Employee> employeeCollectionOrphanCheck = department.getEmployeeCollection();
            for (Employee employeeCollectionOrphanCheckEmployee : employeeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the Employee " + employeeCollectionOrphanCheckEmployee + " in its employeeCollection field has a non-nullable department field.");
            }
            Collection<Notice> noticeCollectionOrphanCheck = department.getNoticeCollection();
            for (Notice noticeCollectionOrphanCheckNotice : noticeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the Notice " + noticeCollectionOrphanCheckNotice + " in its noticeCollection field has a non-nullable department field.");
            }
            Collection<Room> roomCollectionOrphanCheck = department.getRoomCollection();
            for (Room roomCollectionOrphanCheckRoom : roomCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the Room " + roomCollectionOrphanCheckRoom + " in its roomCollection field has a non-nullable department field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(department);
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

    public List<Department> findDepartmentEntities() {
        return findDepartmentEntities(true, -1, -1);
    }

    public List<Department> findDepartmentEntities(int maxResults, int firstResult) {
        return findDepartmentEntities(false, maxResults, firstResult);
    }

    private List<Department> findDepartmentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Department.class));
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

    public Department findDepartment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Department.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartmentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Department> rt = cq.from(Department.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Department> getDeparmentListByTag(String tag) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Department> rt = cq.from(Department.class);

            Path<String> pTag = rt.get("tag");
            Predicate p = em.getCriteriaBuilder().equal(pTag, tag);
            
            cq.where(p);
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
