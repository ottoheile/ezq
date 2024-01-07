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
import static models.UserModel.createUser;
import static models.UserModel.deleteUserInvite;
import static models.UserModel.getAdminStatusFromInviteToken;
import static models.UserModel.getEmailFromInviteToken;
import static models.UserModel.userInviteTokenExists;

/**
 *
 * @author Otto
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        request.getSession().setAttribute("token", token);
        request.setAttribute("validToken", userInviteTokenExists(token));
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = (String) request.getSession().getAttribute("token");
        
        if (token == null || !userInviteTokenExists(token)) {
            response.sendRedirect("login");
            return;
        }
        
        String email = getEmailFromInviteToken(token);
        String password = request.getParameter("hashedPassword");
        boolean isAdmin = getAdminStatusFromInviteToken(token);
        
        createUser(email, password, isAdmin);
        deleteUserInvite(email);
        
        request.getSession().setAttribute("user", new UserModel(email, password));
        request.getSession().setAttribute("email", email);
        
        response.sendRedirect("menu");
    }
}
