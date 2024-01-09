/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import static util.DBHandler.runQuery;
import util.DBHandler.QueryResult;
import static util.TokenGenerator.generateToken;

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
            QueryResult queryResult2 = runQuery("SELECT TITLE FROM EZQ.COURSES WHERE ID = ?", String.valueOf(queryResult1.getRow(i)[0]));
            Object row[] = queryResult2.getRow(0);
            courses[i] = new CourseModel(row[0].toString());   
        }
        
        return courses;
    }
    
    public static int getIdFromUsername(String username){
        QueryResult qr = runQuery("SELECT ID FROM EZQ.USERS WHERE EMAIL = ?", username);
        if (qr.isEmpty()){
            return -1;
        }
        return (int) qr.getRow(0)[0];
    }
    
    public static void createUser(String email, String password, boolean admin) {
        runQuery("INSERT INTO EZQ.USERS (EMAIL, PASSWORD, ADMIN) VALUES (?, ?, ?)", email, password, String.valueOf(admin));
    }
    
    public static boolean userInviteTokenExists(String token) {
        return !runQuery("SELECT * FROM EZQ.INVITED_USERS WHERE TOKEN = ?", token).isEmpty();
    }
    
    public static String getEmailFromInviteToken(String token) {
        QueryResult queryResult = runQuery("SELECT EMAIL FROM EZQ.INVITED_USERS WHERE TOKEN = ?", token);
        
        if (queryResult.isEmpty()) {
            return null;
        }
        
        return (String) queryResult.getRow(0)[0];
    }
    
    public static boolean getAdminStatusFromInviteToken(String token) {
        QueryResult queryResult = runQuery("SELECT ADMIN FROM EZQ.INVITED_USERS WHERE TOKEN = ?", token);
        
        if (queryResult.isEmpty()) {
            return false;
        }
        
        return (boolean) queryResult.getRow(0)[0];
    }
    
    public static String createUserInvite(String email, boolean admin) {
        String token = null;
        while (true) {
            token = generateToken();
            
            if (!userInviteTokenExists(token)) {
                break;
            }
        }
        runQuery("INSERT INTO EZQ.INVITED_USERS (EMAIL, ADMIN, TOKEN) VALUES (?, ?, ?)", email, String.valueOf(admin), token);
        return token;
    }
    
    public static void deleteUserInvite(String email) {
        runQuery("DELETE FROM EZQ.INVITED_USERS WHERE EMAIL = ?", email);
    }
}
