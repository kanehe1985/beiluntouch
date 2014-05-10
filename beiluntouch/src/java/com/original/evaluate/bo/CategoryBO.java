/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.CategoryJpaController;
import com.original.evaluate.dao.CategoryJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Category;
import com.original.util.FacesUtil;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrator
 */
public class CategoryBO {

    private CategoryJpaController categoryJpaController;
    
    public CategoryBO() throws NamingException{
        if (categoryJpaController == null) {
            UserTransaction utx = FacesUtil.getUserTransaction();
            EntityManagerFactory emf = FacesUtil.getEntityManagerFactory(); 
            categoryJpaController = new CategoryJpaController(emf);
        }
    }

    public List<Category> getAllCategoryList() throws NamingException {
        return categoryJpaController.findCategoryEntities();
    }
    
    public Category getCategoryById(Integer id) {
        return categoryJpaController.findCategory(id);
    }
    
    public void create(Category category) throws RollbackFailureException, Exception{
        categoryJpaController.create(category);
    }
    
    public void save(Category category) throws RollbackFailureException, Exception{
        categoryJpaController.edit(category);
    }
    
    public void delete(Category category) throws RollbackFailureException, Exception{
        categoryJpaController.destroy(category.getId());
    }
}
