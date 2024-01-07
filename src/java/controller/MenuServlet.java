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
import models.UserModel;
import static models.CourseModel.createNewCourse;;
import static models.UserModel.exists;
import static models.CourseModel.deleteCourse;
import util.SendEmail;
import static models.UserModel.createUserInvite;

/**
 *
 * @author André, Otto
 */
@WebServlet("/menu")
public class MenuServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        UserModel usr = (UserModel) request.getSession().getAttribute("user");
        request.setAttribute("courses", usr.getAssignedCourses());
        request.getSession().setAttribute("admin", usr.isAdmin());
        
        request.getRequestDispatcher("/WEB-INF/jsp/menu.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] splitValueFromInputButton = request.getParameter("course").split(" ");
        String eventName = splitValueFromInputButton[splitValueFromInputButton.length - 1];
        UserModel usr = (UserModel) request.getSession().getAttribute("user");
        
        request.getSession().setAttribute("courseName", eventName);
        if (eventName.equals("Add")){
            String newCourseTitel = request.getParameter("title");
            createNewCourse(newCourseTitel, usr.getId());
            response.sendRedirect("menu");
        }
        else if (eventName.equals("LogOut")){
            request.getSession().invalidate();
            response.sendRedirect("login");
        }
        else if(eventName.equals("Delete")){
            deleteCourse(splitValueFromInputButton[0], usr);
            response.sendRedirect("menu");
        }
        else if (eventName.equals("Reservations")) {
            response.sendRedirect("reservations");
        }
        else if (eventName.equals("invite")) {
            String email = request.getParameter("email");
            boolean inviteSent = false;
            
            if (!exists(email)) {
                boolean admin = request.getParameter("adminCheckbox") != null;
                
                String token = createUserInvite(email, admin);

                if (token != null) {
                    Properties properties = new Properties();
                    properties.load(SendEmail.class.getClassLoader().getResourceAsStream("conf/credentials/email.properties"));
                    String websiteURL = properties.getProperty("website-url");

                    SendEmail.sendEmail(email, "Get started with EzQ", "Get started with EzQ by setting a password for your account:\n\n" +
                                        websiteURL + "/register?token=" + token + "\n\n" + 
                                        "This email was automatically generated as an EzQ admin has created an account for you.\n\n" +
                                        "If you believe this is an error, please ignore this email.");
                }
                inviteSent = true;
            }
            
            request.setAttribute("inviteSent", inviteSent);
            request.setAttribute("courses", usr.getAssignedCourses());
            request.getSession().setAttribute("admin", usr.isAdmin());
        
            request.getRequestDispatcher("/WEB-INF/jsp/menu.jsp").forward(request, response);
        }
        else{
            response.sendRedirect("course");
        }
    }
}
