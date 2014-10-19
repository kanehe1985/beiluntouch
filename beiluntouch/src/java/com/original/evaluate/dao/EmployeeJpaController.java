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
import com.original.evaluate.entity.Appraisal;
import com.original.evaluate.entity.Category;
import com.original.evaluate.entity.Department;
import com.original.evaluate.entity.Department_;
import com.original.evaluate.entity.Employee;
import com.original.evaluate.entity.Employee_;
import com.original.evaluate.entity.Notice;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author kanehe
 */
public class EmployeeJpaController implements Serializable {

    public EmployeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Employee employee) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (employee.getAppraisalCollection() == null) {
            employee.setAppraisalCollection(new ArrayList<Appraisal>());
        }
        if (employee.getNoticeCollection() == null) {
            employee.setNoticeCollection(new ArrayList<Notice>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category category = employee.getCategory();
            if (category != null) {
                category = em.getReference(category.getClass(), category.getId());
                employee.setCategory(category);
            }
            Department department = employee.getDepartment();
            if (department != null) {
                department = em.getReference(department.getClass(), department.getId());
                employee.setDepartment(department);
            }
            Collection<Appraisal> attachedAppraisalCollection = new ArrayList<Appraisal>();
            for (Appraisal appraisalCollectionAppraisalToAttach : employee.getAppraisalCollection()) {
                appraisalCollectionAppraisalToAttach = em.getReference(appraisalCollectionAppraisalToAttach.getClass(), appraisalCollectionAppraisalToAttach.getId());
                attachedAppraisalCollection.add(appraisalCollectionAppraisalToAttach);
            }
            employee.setAppraisalCollection(attachedAppraisalCollection);
            Collection<Notice> attachedNoticeCollection = new ArrayList<Notice>();
            for (Notice noticeCollectionNoticeToAttach : employee.getNoticeCollection()) {
                noticeCollectionNoticeToAttach = em.getReference(noticeCollectionNoticeToAttach.getClass(), noticeCollectionNoticeToAttach.getId());
                attachedNoticeCollection.add(noticeCollectionNoticeToAttach);
            }
            employee.setNoticeCollection(attachedNoticeCollection);
            em.persist(employee);
            if (category != null) {
                category.getEmployeeCollection().add(employee);
                category = em.merge(category);
            }
            if (department != null) {
                department.getEmployeeCollection().add(employee);
                department = em.merge(department);
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
            for (Notice noticeCollectionNotice : employee.getNoticeCollection()) {
                Employee oldEmployeeOfNoticeCollectionNotice = noticeCollectionNotice.getEmployee();
                noticeCollectionNotice.setEmployee(employee);
                noticeCollectionNotice = em.merge(noticeCollectionNotice);
                if (oldEmployeeOfNoticeCollectionNotice != null) {
                    oldEmployeeOfNoticeCollectionNotice.getNoticeCollection().remove(noticeCollectionNotice);
                    oldEmployeeOfNoticeCollectionNotice = em.merge(oldEmployeeOfNoticeCollectionNotice);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEmployee(employee.getId()) != null) {
                throw new PreexistingEntityException("Employee " + employee + " already exists.", ex);
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
            em = getEntityManager();
            em.getTransaction().begin();
            Employee persistentEmployee = em.find(Employee.class, employee.getId());
            Category categoryOld = persistentEmployee.getCategory();
            Category categoryNew = employee.getCategory();
            Department departmentOld = persistentEmployee.getDepartment();
            Department departmentNew = employee.getDepartment();
            Collection<Appraisal> appraisalCollectionOld = persistentEmployee.getAppraisalCollection();
            Collection<Appraisal> appraisalCollectionNew = employee.getAppraisalCollection();
            Collection<Notice> noticeCollectionOld = persistentEmployee.getNoticeCollection();
            Collection<Notice> noticeCollectionNew = employee.getNoticeCollection();
            List<String> illegalOrphanMessages = null;
            for (Appraisal appraisalCollectionOldAppraisal : appraisalCollectionOld) {
                if (!appraisalCollectionNew.contains(appraisalCollectionOldAppraisal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Appraisal " + appraisalCollectionOldAppraisal + " since its employee field is not nullable.");
                }
            }
            for (Notice noticeCollectionOldNotice : noticeCollectionOld) {
                if (!noticeCollectionNew.contains(noticeCollectionOldNotice)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notice " + noticeCollectionOldNotice + " since its employee field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (categoryNew != null) {
                categoryNew = em.getReference(categoryNew.getClass(), categoryNew.getId());
                employee.setCategory(categoryNew);
            }
            if (departmentNew != null) {
                departmentNew = em.getReference(departmentNew.getClass(), departmentNew.getId());
                employee.setDepartment(departmentNew);
            }
            Collection<Appraisal> attachedAppraisalCollectionNew = new ArrayList<Appraisal>();
            for (Appraisal appraisalCollectionNewAppraisalToAttach : appraisalCollectionNew) {
                appraisalCollectionNewAppraisalToAttach = em.getReference(appraisalCollectionNewAppraisalToAttach.getClass(), appraisalCollectionNewAppraisalToAttach.getId());
                attachedAppraisalCollectionNew.add(appraisalCollectionNewAppraisalToAttach);
            }
            appraisalCollectionNew = attachedAppraisalCollectionNew;
            employee.setAppraisalCollection(appraisalCollectionNew);
            Collection<Notice> attachedNoticeCollectionNew = new ArrayList<Notice>();
            for (Notice noticeCollectionNewNoticeToAttach : noticeCollectionNew) {
                noticeCollectionNewNoticeToAttach = em.getReference(noticeCollectionNewNoticeToAttach.getClass(), noticeCollectionNewNoticeToAttach.getId());
                attachedNoticeCollectionNew.add(noticeCollectionNewNoticeToAttach);
            }
            noticeCollectionNew = attachedNoticeCollectionNew;
            employee.setNoticeCollection(noticeCollectionNew);
            employee = em.merge(employee);
            if (categoryOld != null && !categoryOld.equals(categoryNew)) {
                categoryOld.getEmployeeCollection().remove(employee);
                categoryOld = em.merge(categoryOld);
            }
            if (categoryNew != null && !categoryNew.equals(categoryOld)) {
                categoryNew.getEmployeeCollection().add(employee);
                categoryNew = em.merge(categoryNew);
            }
            if (departmentOld != null && !departmentOld.equals(departmentNew)) {
                departmentOld.getEmployeeCollection().remove(employee);
                departmentOld = em.merge(departmentOld);
            }
            if (departmentNew != null && !departmentNew.equals(departmentOld)) {
                departmentNew.getEmployeeCollection().add(employee);
                departmentNew = em.merge(departmentNew);
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
            for (Notice noticeCollectionNewNotice : noticeCollectionNew) {
                if (!noticeCollectionOld.contains(noticeCollectionNewNotice)) {
                    Employee oldEmployeeOfNoticeCollectionNewNotice = noticeCollectionNewNotice.getEmployee();
                    noticeCollectionNewNotice.setEmployee(employee);
                    noticeCollectionNewNotice = em.merge(noticeCollectionNewNotice);
                    if (oldEmployeeOfNoticeCollectionNewNotice != null && !oldEmployeeOfNoticeCollectionNewNotice.equals(employee)) {
                        oldEmployeeOfNoticeCollectionNewNotice.getNoticeCollection().remove(noticeCollectionNewNotice);
                        oldEmployeeOfNoticeCollectionNewNotice = em.merge(oldEmployeeOfNoticeCollectionNewNotice);
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
            em = getEntityManager();
            em.getTransaction().begin();
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
            Collection<Notice> noticeCollectionOrphanCheck = employee.getNoticeCollection();
            for (Notice noticeCollectionOrphanCheckNotice : noticeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the Notice " + noticeCollectionOrphanCheckNotice + " in its noticeCollection field has a non-nullable employee field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Category category = employee.getCategory();
            if (category != null) {
                category.getEmployeeCollection().remove(employee);
                category = em.merge(category);
            }
            Department department = employee.getDepartment();
            if (department != null) {
                department.getEmployeeCollection().remove(employee);
                department = em.merge(department);
            }
            em.remove(employee);
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

    public List<Employee> findEmployeeEntities() {
        return findEmployeeEntities(true, -1, -1);
    }

    public List<Employee> findEmployeeEntities(int maxResults, int firstResult) {
        return findEmployeeEntities(false, maxResults, firstResult);
    }
    
    public List<Employee> findAllEmployeeEntities(){
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Employee.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    private List<Employee> findEmployeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> root = cq.from(Employee.class);
            Path<Boolean> pAllow = root.get("isallowappraisal");
            
            cq.where(em.getCriteriaBuilder().equal(pAllow, true)).orderBy(em.getCriteriaBuilder().asc(root.get("orderid")));
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
    
    public List<Employee> findEmployeeEntities(String group, String room) {
        return findEmployeeEntities("",group,room);
    }

    public List<Employee> findEmployeeEntities(String departmentID, String categoryID, String room) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> root = cq.from(Employee.class);
            
            Path<Department> pdepID = root.get("department");
            Path<Category> pCategoryID = root.get("category");
            Path<String> pRoom = root.get("romno");
            Path<Boolean> pAllow = root.get("isallowappraisal");
            
            List<Predicate> pAll = new ArrayList<>();
            pAll.add(em.getCriteriaBuilder().equal(pAllow, true));
            if(departmentID.length()>0){
                Department department = em.getReference(Department.class, Integer.parseInt(departmentID));
                pAll.add(em.getCriteriaBuilder().equal(pdepID, department));
            }
                      
            if(categoryID != null && !categoryID.isEmpty()){
                Category category = em.getReference(Category.class, Integer.parseInt(categoryID));
                pAll.add(em.getCriteriaBuilder().equal(pCategoryID, category));
            }
//            if (group != null && !group.isEmpty()) {
//                pAll.add(em.getCriteriaBuilder().like(pGroup, group));
//            }  
            if (room != null && !room.isEmpty()) {
                pAll.add(em.getCriteriaBuilder().equal(pRoom, room));
            }
            
            Predicate[] pArray = new Predicate[pAll.size()];
            pArray = pAll.toArray(pArray);
            
            List<Order> orders = new ArrayList<>();
            orders.add(em.getCriteriaBuilder().asc(root.get("deporderid")));
            orders.add(em.getCriteriaBuilder().asc(root.get("orderid")));
            cq.where(pArray).orderBy(orders);
            
//            cq.where(pArray).orderBy(em.getCriteriaBuilder().asc(root.get("department orderid")));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }  
    
    public List<Employee> findEmployeeEntitiesByTag(String tag, String categoryID, String room) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> employeeRoot = cq.from(Employee.class);
            Join<Employee,Department> departmentJoin = employeeRoot.join(Employee_.department);
            
            
//            Root<Department> depRoot = cq.from(Department.class);
//            Join<Department,Employee> employeeJoin = depRoot.join(Department_.employeeCollection);
            
            Path<String> pDepartmentTag = departmentJoin.get("tag");
            Path<Category> pCategoryID = employeeRoot.get("category");
            Path<String> pRoom = employeeRoot.get("romno");
            Path<Boolean> pAllow = employeeRoot.get("isallowappraisal");
            
            List<Predicate> pAll = new ArrayList<>();
            pAll.add(em.getCriteriaBuilder().equal(pAllow, true));
            
            if(tag != null && ! tag.isEmpty()){
//                pAll.add(em.getCriteriaBuilder().equal(employeeJoin.get(Employee_.department).get(Department_.tag), tag));
                pAll.add(em.getCriteriaBuilder().equal(pDepartmentTag, tag));
            }
                                  
            if(categoryID != null && !categoryID.isEmpty()){
                Category category = em.getReference(Category.class, Integer.parseInt(categoryID));
                pAll.add(em.getCriteriaBuilder().equal(pCategoryID, category));
            }
//            if (group != null && !group.isEmpty()) {
//                pAll.add(em.getCriteriaBuilder().like(pGroup, group));
//            }  
            if (room != null && !room.isEmpty()) {
                pAll.add(em.getCriteriaBuilder().equal(pRoom, room));
            }
            
            Predicate[] pArray = new Predicate[pAll.size()];
            pArray = pAll.toArray(pArray);
            
            List<Order> orders = new ArrayList<>();
            orders.add(em.getCriteriaBuilder().asc(employeeRoot.get("deporderid")));
            orders.add(em.getCriteriaBuilder().asc(employeeRoot.get("orderid")));
            cq.where(pArray).orderBy(orders);
            
//            cq.where(pArray).orderBy(em.getCriteriaBuilder().asc(employeeRoot.get("department orderid")));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
