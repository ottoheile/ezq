/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static models.CourseModel.getIdForCourse;
import models.UserModel;
import static models.ListModel.getListsForCourse;

/**
 *
 * @author André
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
        request.setAttribute("lists", getListsForCourse(getIdForCourse((String) request.getSession().getAttribute("courseName"), usr)));
        
        request.getRequestDispatcher("/WEB-INF/jsp/course.jsp").forward(request, response);
    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        
//        String givenEmail = request.getParameter("email");
//        String givenPassword = request.getParameter("password");
//        
//        //VALIDERA UPPGIFTER!!!!!!!!!!
//        request.getSession().setAttribute("user", new UserModel(givenEmail, givenPassword));
//        request.getSession().setAttribute("email", givenEmail);
//        response.sendRedirect("menu");
//    }
}
