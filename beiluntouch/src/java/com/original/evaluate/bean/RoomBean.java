/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.original.evaluate.bean;

import com.original.evaluate.bo.CategoryBO;
import com.original.evaluate.bo.DepartmentBO;
import com.original.evaluate.bo.EmployeeBO;
import com.original.evaluate.bo.RoomBO;
import com.original.evaluate.entity.Category;
import com.original.evaluate.entity.Department;
import com.original.evaluate.entity.Employee;
import com.original.evaluate.entity.Room;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author kanehe
 */
@Named(value="roomBean")
@SessionScoped
public class RoomBean implements Serializable {
    
    private RoomBO roomBO;
    private DepartmentBO departmentBO;
    private CategoryBO categoryBO;
    
    private List<Room> rooms;
    private List<Room> selectedRooms = null;
    private List<Department> departments;
    private List<Category> categorys;
    private List<Employee> employees;
    private Room editRoom;
    
    private int editDepartmentID;
    private int editCategoryID;
    private String editno;

    public List<Room> getSelectedRooms() {
        return selectedRooms;
    }

    public int getEditDepartmentID() {
        return editDepartmentID;
    }

    public void setEditDepartmentID(int editDepartmentID) {
        this.editDepartmentID = editDepartmentID;
    }

    public int getEditCategoryID() {
        return editCategoryID;
    }

    public void setEditCategoryID(int editCategoryID) {
        this.editCategoryID = editCategoryID;
    }

    public String getEditno() {
        return editno;
    }

    public void setEditno(String editno) {
        this.editno = editno;
    }
    
    public void setSelectedRooms(List<Room> selectedRooms) {
        this.selectedRooms = selectedRooms;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Category> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<Category> categorys) {
        this.categorys = categorys;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }   
    
    public Room getEditRoom() {
        return editRoom;
    }

    public void setEditRoom(Room editRoom) {
        this.editRoom = editRoom;
    }

    public RoomBO getRoomBO() {
        return roomBO;
    }

    public void setRoomBO(RoomBO roomBO) {
        this.roomBO = roomBO;
    }
    
    public RoomBean() throws NamingException {
        roomBO = new RoomBO();
        departmentBO = new DepartmentBO();
        categoryBO = new CategoryBO();
        
        editRoom = new Room();
        
        rooms = roomBO.getAllRoomList();
        departments = departmentBO.getAllDepartmentList();
        categorys = categoryBO.getAllCategoryList();
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;   
    }
    
    public void update() throws Exception{
        editRoom.setDepartment(departmentBO.getDepartmentById(editDepartmentID));
        editRoom.setCategory(categoryBO.getCategoryById(editCategoryID));
        editRoom.setNo(editno);
        roomBO.save(editRoom);
        rooms = roomBO.getAllRoomList();
    }
    
    public void create() throws Exception{
        editRoom.setDepartment(departmentBO.getDepartmentById(editDepartmentID));
        editRoom.setCategory(categoryBO.getCategoryById(editCategoryID));
        editRoom.setNo(editno);
        roomBO.create(editRoom);
        rooms = roomBO.getAllRoomList();
    }
    
    public void delete() throws Exception{
        for(Room room:selectedRooms){
            roomBO.delete(room);
        }
        rooms = roomBO.getAllRoomList();
    }
    
    public void refresh() throws NamingException{
        rooms = roomBO.getAllRoomList();
    }
    
    public void prepareCreate() {
        editRoom = new Room();
        editDepartmentID=-1;
        editCategoryID=-1;
    }
}
