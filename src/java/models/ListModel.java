/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import util.DBHandler.QueryResult;
import static util.DBHandler.runQuery;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 *
 * @author André, Otto
 */
public class ListModel {
    
    private String startTime;
    private String description;
    private String location;
    private int interval;
    private int max_slots;
    private final int id;
    
    private ListModel(String start, String desc, String location, int interval, int max_slots, int id){
        this.startTime = start;
        this.description = desc;
        this.location = location;
        this.interval = interval;
        this.max_slots = max_slots;
        this.id = id;
    }

    public String getStartTime() {
        return startTime.replace(":00.0", "");
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public int getInterval() {
        return interval;
    }

    public int getMax_slots() {
        return max_slots;
    }
    
    public int getId(){
        return id;
    }
    
    public int slotsLeft(){
        QueryResult qr = runQuery("SELECT * FROM EZQ.RESERVATIONS WHERE LIST_ID = ?", String.valueOf(id));
        
        return max_slots - qr.getNumberOfRows();
    }
    
    public static ListModel getListFromID(int listID) {
        QueryResult queryResult = runQuery("SELECT START, DESCRIPTION, LOCATION, INTERVAL, MAX_SLOTS FROM EZQ.LISTS WHERE ID = ?", String.valueOf(listID));
        
        if (queryResult.isEmpty()) {
            return null;
        }
        
        Object[] result = queryResult.getRow(0);
        return new ListModel(result[0].toString(), (String) result[1], (String) result[2], (int) result[3], (int) result[4], listID);
    }
    
    public static ListModel[] getListsForCourse(int course_id){
        QueryResult qr = runQuery("SELECT START, DESCRIPTION, LOCATION, INTERVAL, MAX_SLOTS, ID FROM EZQ.LISTS WHERE COURSE_ID = ?", String.valueOf(course_id));
        
        ListModel[] lists = new ListModel[qr.getNumberOfRows()];
        for (int i = 0; i < qr.getNumberOfRows(); i++){
            Object[] row = qr.getRow(i);
            lists[i] = new ListModel(row[0].toString(),(String) row[1],(String) row[2],(int) row[3],(int) row[4], (int) row[5]);
        }
        return lists;
    }
    
    private static void deleteListFromReservations(int list_id){
        runQuery("DELETE FROM EZQ.RESERVATIONS WHERE LIST_ID = ?", String.valueOf(list_id));
    }
    
    public static void deleteList(int list_id){
        runQuery("DELETE FROM EZQ.LISTS WHERE ID = ?", String.valueOf(list_id));
    }
    
    public static void deleteAllListsForCourse(int course_id ){
        runQuery("DELETE FROM EZQ.LISTS WHERE COURSE_ID = ?", String.valueOf(course_id));
    }
    
    public static void addListToCourse (int max_slots, int interval, String datetime, String location, String desc, int course_id, int user_id){
        datetime = datetime + ":00";
        runQuery("INSERT INTO EZQ.LISTS (COURSE_ID, USER_ID, DESCRIPTION, LOCATION, START, INTERVAL, MAX_SLOTS)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)", String.valueOf(course_id), String.valueOf(user_id), desc, location, 
                datetime, String.valueOf(interval), String.valueOf(max_slots));
    }
    
    public static boolean userHasBookedList(int userID, int listID) {
        return !runQuery("SELECT * FROM EZQ.RESERVATIONS WHERE USER_ID = ? AND LIST_ID = ?", String.valueOf(userID), String.valueOf(listID)).isEmpty();
    }
    
    public static void addUserToList(int userID, int listID) {
        if (!userHasBookedList(userID, listID)) {
            runQuery("INSERT INTO EZQ.RESERVATIONS (LIST_ID, USER_ID) VALUES (?, ?)", String.valueOf(listID), String.valueOf(userID));
        }
    }
    
    public static void removeUserFromList(int userID, int listID) {
        if (userHasBookedList(userID, listID)) {
            runQuery("DELETE FROM EZQ.RESERVATIONS WHERE USER_ID = ? AND LIST_ID = ?", String.valueOf(userID), String.valueOf(listID));
        }
    }
    
    public static ListModel[] getAllBookedListsForUserOrderedByCourseName(int userID) {
        QueryResult queryResult = runQuery("SELECT L.START, L.DESCRIPTION, L.LOCATION, L.INTERVAL, L.MAX_SLOTS, L.ID FROM EZQ.LISTS AS L " +
                                           "INNER JOIN EZQ.RESERVATIONS AS R ON R.LIST_ID = L.ID INNER JOIN EZQ.COURSES AS C ON C.ID = L.COURSE_ID " +
                                           "WHERE R.USER_ID = ? ORDER BY C.TITLE, L.ID", String.valueOf(userID));
        int numberOfRows = queryResult.getNumberOfRows();
        ListModel[] reservations = new ListModel[numberOfRows];
        
        for (int i = 0; i < numberOfRows; i++) {
            Object[] row = queryResult.getRow(i);
            reservations[i] = new ListModel(row[0].toString(), (String) row[1], (String) row[2], (int) row[3], (int) row[4], (int) row[5]);
        }
        return reservations;
    }
    
    public static String[] getAllEmailsFromBookedList(int listID) {
        QueryResult queryResult = runQuery("SELECT U.EMAIL FROM EZQ.USERS AS U INNER JOIN EZQ.RESERVATIONS AS R ON R.USER_ID = U.ID " +
                                           "WHERE R.LIST_ID = ?", String.valueOf(listID));
        int numberOfRows = queryResult.getNumberOfRows();
        String[] emails = new String[numberOfRows];
        for (int i = 0; i < numberOfRows; i++) {
            emails[i] = (String) queryResult.getRow(i)[0];
        }
        return emails;
    }
}
