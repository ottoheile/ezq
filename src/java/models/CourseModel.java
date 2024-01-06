/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import util.DBHandler.QueryResult;
import static util.DBHandler.runQuery;
import static models.ListModel.deleteAllListsForCourse;

/**
 *
 * @author André, Otto
 */
public class CourseModel {
    private final String name;
    
    public CourseModel(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public static int getIdForCourse(String courseName, UserModel user){
        QueryResult qr;
        if (user.isAdmin()){
            qr = runQuery("SELECT ID FROM EZQ.COURSES WHERE TITEL = ? AND ADMIN_ID = ?", courseName, String.valueOf(user.getId()));
            return (int) qr.getRow(0)[0];
        }
        else {
            qr = runQuery("SELECT COURSE_ID FROM EZQ.COURSE_USER WHERE USER_ID = ?", String.valueOf(user.getId()));
            for (int i = 0; i < qr.getNumberOfRows(); i++){
                Object[] row = qr.getRow(i);
                QueryResult tempQr = runQuery("SELECT ID FROM EZQ.COURSES WHERE ID = ? AND TITEL = ?", String.valueOf((int)row[0]), courseName);
                if (!tempQr.isEmpty()){
                    return (int) tempQr.getRow(0)[0];
                }
            }
        } 
        return -1;
    }
    
    public static String getCourseNameFromListID(int listID) {
        QueryResult queryResult = runQuery("SELECT C.TITEL FROM EZQ.COURSES AS C INNER JOIN EZQ.LISTS AS L ON " +
                                           "L.COURSE_ID = C.ID WHERE L.ID = ?", String.valueOf(listID));
        if (queryResult.isEmpty())
            return "";
        
        return (String) queryResult.getRow(0)[0];
    }
    
    public static void createNewCourse(String titel, int admin_id){
        QueryResult qr = runQuery("SELECT ID FROM EZQ.COURSES WHERE TITEL = ? AND ADMIN_ID = ?", titel, String.valueOf(admin_id));
        if (!qr.isEmpty())
            return;
        runQuery("INSERT INTO EZQ.COURSES (titel, admin_id) VALUES (?, ?)", titel, String.valueOf(admin_id));
    }
    
    public static void deleteCourse(String titel, UserModel user){
        deleteAllListsForCourse(getIdForCourse(titel, user));
        int course_id = getIdForCourse(titel, user);
        runQuery("DELETE FROM EZQ.COURSE_USER WHERE COURSE_ID = ?", String.valueOf(course_id));
        runQuery("DELETE FROM EZQ.COURSES WHERE TITEL = ? AND ADMIN_ID = ?", titel, String.valueOf(user.getId()));
    }
    
    public static void addUserToCourse(String titel, int admin_id, int user_id){
        QueryResult qr = runQuery("SELECT ID FROM EZQ.COURSES WHERE TITEL = ? AND ADMIN_ID = ?", titel, String.valueOf(admin_id));
        Object[] row = qr.getRow(0);
        int course_id = (int) row[0];
        
        QueryResult qrTemp = runQuery("SELECT ID FROM EZQ.COURSE_USER WHERE COURSE_ID = ? AND USER_ID= ?", String.valueOf(course_id), String.valueOf(user_id));
        if(qrTemp.isEmpty())
            runQuery("INSERT INTO EZQ.COURSE_USER (course_id, user_id) VALUES (?, ?)", String.valueOf(course_id), String.valueOf(user_id));
    }
}
