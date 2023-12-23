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
import models.UserModel;

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
        request.setAttribute("courses", usr.getCourses());
        
        request.getRequestDispatcher("/WEB-INF/jsp/menu.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] splitValueFromInputButton = request.getParameter("course").split(" ");
        String course = splitValueFromInputButton[splitValueFromInputButton.length - 1];
        
        request.getSession().setAttribute("courseName", course);
        response.sendRedirect("course");
    }
}
