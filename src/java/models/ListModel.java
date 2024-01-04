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
 * @author André
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
        deleteListFromReservations(list_id);
    }
    
    public static void deleteAllListsForCourse(int course_id ){
        QueryResult qr = runQuery("SELECT ID FROM EZQ.LISTS WHERE COURSE_ID = ?", String.valueOf(course_id));
        runQuery("DELETE FROM EZQ.LISTS WHERE COURSE_ID = ?", String.valueOf(course_id));
        for (int i = 0; i < qr.getNumberOfRows(); i++){
            deleteListFromReservations((int) qr.getRow(i)[0]);
        }
    }
    
    public static void addListToCourse (int max_slots, int interval, String datetime, String location, String desc, int course_id, int user_id){
        datetime = datetime + ":00";
        runQuery("INSERT INTO EZQ.LISTS (COURSE_ID, USER_ID, DESCRIPTION, LOCATION, START, INTERVAL, MAX_SLOTS)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)", String.valueOf(course_id), String.valueOf(user_id), desc, location, 
                datetime, String.valueOf(interval), String.valueOf(max_slots));
    }
    
}
