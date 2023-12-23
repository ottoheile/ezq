/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author André
 */
public class UserModel {
    private final String email;
    private CourseModel[] courses;
    
    public UserModel(String email, String password){
        this.email = email;
        this.courses = getAssignedCourses();
    }
    
    public String getEmail(){
        return email;
    }
    
    public CourseModel[] getCourses(){
        return courses;
    }
    
    private CourseModel[] getAssignedCourses(){
        CourseModel tmpCourse1 = new CourseModel("IX1200");
        CourseModel tmpCourse2 = new CourseModel("IX1300");
        CourseModel tmpCourse3 = new CourseModel("IX1400");
        CourseModel[] courses = new CourseModel[3];
        courses[0] = tmpCourse1;
        courses[1] = tmpCourse2;
        courses[2] = tmpCourse3;
        
        return courses;
    }
}
