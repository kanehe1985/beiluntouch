/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.original.evaluate.bo;

import com.original.evaluate.dao.RoomJpaController;
import com.original.evaluate.dao.exceptions.RollbackFailureException;
import com.original.evaluate.entity.Room;
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
public class RoomBO {

    private RoomJpaController roomJpaController;
    
    public RoomBO() throws NamingException{
        if (roomJpaController == null) {
            UserTransaction utx = FacesUtil.getUserTransaction();
            EntityManagerFactory emf = FacesUtil.getEntityManagerFactory(); 
            roomJpaController = new RoomJpaController(emf);
        }
    }

    public List<Room> getAllRoomList() throws NamingException {
        return roomJpaController.findRoomEntities();
    }
    
    public Room getRoomById(Integer id) {
        return roomJpaController.findRoom(id);
    }
    
    public void create(Room room) throws RollbackFailureException, Exception{
        roomJpaController.create(room);
    }
    
    public void save(Room room) throws RollbackFailureException, Exception{
        roomJpaController.edit(room);
    }
    
    public void delete(Room room) throws RollbackFailureException, Exception{
        roomJpaController.destroy(room.getId());
    }
}
