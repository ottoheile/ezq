<%-- 
    Document   : course
    Created on : 23 Dec 2023, 23:11:18
    Author     : André
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= ((String) request.getSession().getAttribute("courseName")) %></title>
    </head>
    <body>
        <h1><%= ((String) request.getSession().getAttribute("courseName")) %></h1>
    </body>
</html>
