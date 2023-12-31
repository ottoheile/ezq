/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static models.CourseModel.getIdForCourse;
import models.ListModel;
import models.UserModel;
import static models.UserModel.exists;
import static models.UserModel.getIDFromEmail;
import static models.ListModel.addUserToList;
import static models.ListModel.getListsForCourse;
import static models.ListModel.addListToCourse;
import static models.ListModel.deleteList;
import static models.CourseModel.addUserToCourse;
import static models.UserModel.getIdFromUsername;
import static models.ListModel.getAllEmailsFromBookedList;
import static models.ListModel.getListFromID;
import static models.ListModel.removeUserFromList;
import static models.ListModel.userHasBookedList;
import util.SendEmail;

/**
 *
 * @author André, Otto
 */
@WebServlet("/course")
public class CourseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        if (request.getSession().getAttribute("courseName") == null){
            response.sendRedirect("menu");
            return;
        }
        
        UserModel usr = (UserModel) request.getSession().getAttribute("user");
        ListModel[] lists = getListsForCourse(getIdForCourse((String) request.getSession().getAttribute("courseName"), usr));
        
        request.setAttribute("lists", lists);
        
        request.getRequestDispatcher("/WEB-INF/jsp/course.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserModel usr = (UserModel) request.getSession().getAttribute("user");
        String[] splitValueFromInputButton = request.getParameter("list").split(" ");
        String eventName = splitValueFromInputButton[splitValueFromInputButton.length - 1];
                
        if (eventName.equals("Add")){
            int max_slots = Integer.parseInt(request.getParameter("maxslots"));
            int duration = Integer.parseInt(request.getParameter("duration"));
            String location = request.getParameter("location");
            String description = request.getParameter("description");
            String datetime = request.getParameter("datetime");
            datetime = datetime.contains("T0") ? datetime.replace("T0", " ") : datetime.replace("T", " ");
            String currentCourse = (String) request.getSession().getAttribute("courseName");
            
            addListToCourse(max_slots, duration, datetime, location, description, getIdForCourse(currentCourse, usr), usr.getId());
            response.sendRedirect("course");
        }
        else if (eventName.equals("Delete")){
            deleteList(Integer.parseInt(splitValueFromInputButton[0]));
            response.sendRedirect("course");
        }
        else if(eventName.equals("user")){
            String userToAdd = request.getParameter("username");
            int userToAddId = getIdFromUsername(userToAdd);
            if (userToAddId == -1){
                response.sendRedirect("course");
            }
            else{
                addUserToCourse((String) request.getSession().getAttribute("courseName"), usr.getId(), userToAddId);
                response.sendRedirect("course");
            }
        }
        else if (eventName.equals("Book")) {
            int userID = usr.getId();
            String email = usr.getEmail();
            int formIndex = Integer.parseInt(request.getParameter("formIndex"));
            String courseName = (String) request.getSession().getAttribute("courseName");
            
            if (usr.isAdmin()) {
                email = request.getParameter("userBooked" + formIndex);
                if (!exists(email)) {
                    ListModel[] lists = getListsForCourse(getIdForCourse(courseName, usr));
                    boolean[] userNotExistsTextHelper = new boolean[lists.length];
                    userNotExistsTextHelper[formIndex] = true;
                    
                    request.setAttribute("userNotExistsTextHelper", userNotExistsTextHelper);
                    request.setAttribute("lists", lists);
                    request.getRequestDispatcher("/WEB-INF/jsp/course.jsp").forward(request, response);
                    
                    return;
                }
                userID = getIDFromEmail(email);
            }
            int listID = Integer.parseInt(splitValueFromInputButton[0]);
            
            if (userHasBookedList(userID, listID)) {
                response.sendRedirect("course");
                return;
            }
            
            ListModel list = getListFromID(listID);
            
            if (list == null) {
                return;
            }
            
            addUserToList(userID, listID);
            
            Properties properties = new Properties();
            properties.load(SendEmail.class.getClassLoader().getResourceAsStream("conf/credentials/email.properties"));
            String websiteURL = properties.getProperty("website-url");
            
            SendEmail.sendEmail(email, "EzQ time slot booking confirmation", 
                                "Below is a confirmation of your reserved time slot for the course " + courseName + ":\n\n" +
                                "Description: " + list.getDescription() + "\n" +
                                "When: " + list.getStartTime() + "\n" +
                                "Where: " + list.getLocation() + "\n" + 
                                "\n" +
                                "You can find this and other reservations below.\n\n" +
                                websiteURL + "/reservations\n\n" +
                                "This email can not be responeded to.");
            
            response.sendRedirect("course");
        }
        else if (eventName.equals("Cancel")) {
            int userID = usr.getId();
            int formIndex = Integer.parseInt(request.getParameter("formIndex"));
            
            removeUserFromList(userID, Integer.parseInt(splitValueFromInputButton[0]));
            response.sendRedirect("course");
        }
        else if (eventName.equals("ListUsers")) {
            ListModel[] lists = getListsForCourse(getIdForCourse((String) request.getSession().getAttribute("courseName"), usr));

            request.setAttribute("lists", lists);
            request.setAttribute("emails", getAllEmailsFromBookedList(Integer.parseInt(splitValueFromInputButton[0])));
            request.setAttribute("listID", splitValueFromInputButton[0]);

            request.getRequestDispatcher("/WEB-INF/jsp/course.jsp").forward(request, response);
        }
        else if (eventName.equals("Remove")) {
            int userID = getIDFromEmail(splitValueFromInputButton[1]);
            int listID = Integer.parseInt(splitValueFromInputButton[0]);
            
            removeUserFromList(userID, listID);
            
            ListModel[] lists = getListsForCourse(getIdForCourse((String) request.getSession().getAttribute("courseName"), usr));

            request.setAttribute("lists", lists);
            request.setAttribute("emails", getAllEmailsFromBookedList(Integer.parseInt(splitValueFromInputButton[0])));
            request.setAttribute("listID", splitValueFromInputButton[0]);

            request.getRequestDispatcher("/WEB-INF/jsp/course.jsp").forward(request, response);
        }
    }
}
