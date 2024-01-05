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
import models.ListModel;
import static models.ListModel.getAllBookedListsForUserOrderedByCourseName;
import static models.ListModel.removeUserFromList;
import models.UserModel;

/**
 *
 * @author Otto
 */
@WebServlet("/reservations")
public class ReservationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        ListModel[] reservations = getAllBookedListsForUserOrderedByCourseName(user.getId());
        
        request.setAttribute("reservations", reservations);
        
        request.getRequestDispatcher("/WEB-INF/jsp/reservations.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        int formIndex = Integer.parseInt(request.getParameter("formIndex"));
        int listID = Integer.parseInt(request.getParameter("cancel" + formIndex));
        removeUserFromList(user.getId(), listID);
        response.sendRedirect("reservations");
    }
}
