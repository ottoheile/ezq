/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import static util.DBHandler.runQuery;
import util.DBHandler.QueryResult;

/**
 *
 * @author André, Otto
 */
public class UserModel {
    private final String email;
    private boolean admin;
    private final int id;

    public UserModel(String email, String password){
        QueryResult queryResult = runQuery("SELECT ID FROM EZQ.USERS WHERE EMAIL = ?", email);
        this.id = (int) queryResult.getRow(0)[0];
        this.email = email;
        this.admin = UserModel.getAdminStatus(email);
    }
    
    public String getEmail(){
        return email;
    }
    
    public boolean isAdmin() {
       return admin;
    }
    
    public int getId(){
        return id;
    }
    
    public static boolean isValidCredentials(String email, String password) {
        return !runQuery("SELECT ID FROM EZQ.USERS WHERE EMAIL = ? AND PASSWORD = ?", email, password).isEmpty();
    }
    
    public static boolean exists(String email) {
        return (boolean) !runQuery("SELECT ID FROM EZQ.USERS WHERE EMAIL = ?", email).isEmpty();
    }
    
    public static boolean getAdminStatus(String email){
        QueryResult qr = runQuery("SELECT ADMIN FROM EZQ.USERS WHERE EMAIL = ?", email);
        
        return (boolean) qr.getRow(0)[0];
    }
    
    public static int getIDFromEmail(String email) {
        if (exists(email))
            return (int) runQuery("SELECT ID FROM EZQ.USERS WHERE EMAIL = ?", email).getRow(0)[0];
        return -1;
    }
    
    public CourseModel[] getAssignedCourses(){
        QueryResult queryResult1;
        if (admin)
            queryResult1 = runQuery("SELECT ID FROM EZQ.COURSES WHERE ADMIN_ID = ?", String.valueOf(this.id));
        else
            queryResult1 = runQuery("SELECT COURSE_ID FROM EZQ.COURSE_USER WHERE USER_ID = ?", String.valueOf(this.id));
        
        CourseModel[] courses = new CourseModel[queryResult1.getNumberOfRows()];
        
        for (int i = 0; i < queryResult1.getNumberOfRows(); i++){
            QueryResult queryResult2 = runQuery("SELECT TITEL FROM EZQ.COURSES WHERE ID = ?", String.valueOf(queryResult1.getRow(i)[0]));
            Object row[] = queryResult2.getRow(0);
            courses[i] = new CourseModel(row[0].toString());   
        }
        
        return courses;
    }
}
