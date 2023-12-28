/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.UserModel;
import static models.CourseModel.createNewCourse;


/**
 *
 * @author André
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
        String course = splitValueFromInputButton[splitValueFromInputButton.length - 1];
        
        request.getSession().setAttribute("courseName", course);
        if (course.equals("Add")){
            UserModel usr = (UserModel) request.getSession().getAttribute("user");
            String newCourseTitel = request.getParameter("titel");
            createNewCourse(newCourseTitel, usr.getId());
            response.sendRedirect("menu");
        }
        else if (course.equals("LogOut")){
            request.getSession().invalidate();
            response.sendRedirect("login");
        }
        else{
            response.sendRedirect("course");
        }
    }
}
