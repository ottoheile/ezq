<%-- 
    Document   : course
    Created on : 23 Dec 2023, 23:11:18
    Author     : André
--%>

<%@ page import="models.ListModel" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Menu</title>
    <style>
        html{
            background-color: lightgrey;
        }
        body{
            background-color: white;
            position: relative;
            min-height: 300px;
            width: 50%;
            margin-left: auto;
            margin-right: auto;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
            padding-bottom: 50px;
        }
        h2 {
            margin-left: auto;
            margin-right: auto;
            text-align: center;
            margin-bottom: 50px;
            font-family: cursive;
        }
        .container {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 30px; /* Adjust the gap between divs as needed */
            margin: 0 auto;
        }
        .item {
            height: auto; /* Set the height as needed */
            border: solid 1px; /* Add a border for better visibility */
            border-radius: 20px 1px;
            margin-left: 15px;
            margin-right: 15px;
            padding: 8px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
        }
        .item form{
            height: 100%;
        }
        .item form input{
            margin: auto;
            border-radius: 10px 1px;
        }
        .item form input:active{
            background-color: rgba(230,230,230);
        }
        
        button {
            border: solid 1px; /* Add a border for better visibility */
            border-radius: 20px 1px;
            margin-bottom: 10px;
            padding: 5px;
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
        }
    </style>
</head>
<body>
    <h2> Course <%= (String) request.getSession().getAttribute("courseName")%></h2>
    
    <div class="container">
        <% ListModel[] lists = ((ListModel[]) request.getAttribute("lists"));
        for (int i = 0; i < lists.length; i++) { %>
            <% if (!(lists[i].slotsLeft() == 0)) { %>
                <div class="item">
                    <form action="menu" method="post">
                        <label><%= lists[i].getStartTime() %> </label><br>
                        <p><%= lists[i].getDescription() %></p>
                        <p><%= lists[i].getLocation() %></p>
                        <p><%= lists[i].slotsLeft() %></p>
                        <input type="submit" name="list" value="Book <%= lists[i].getStartTime() %>">
                    </form>
                </div>
                <% } %>
        <% } %>
        <% if ((boolean) request.getSession().getAttribute("admin")) { %>
        <div class="item">
                <form action="menu" method="post">
                    <label>Add List</label><br>
                    <input type="submit" name="list" value="Add">
                </form>
        </div>
        <% } %>
    </div>
    <button id="goBack"> Go back </button>
    <script>
        document.getElementById('goBack').addEventListener('click', function() {
            var currentUrl = window.location.href;
            
            var newPart = 'menu';
            
            var urlParts = currentUrl.split('/');
            
            urlParts[urlParts.length - 1] = newPart;
            
            var newUrl = urlParts.join('/');
            
            window.location.href = newUrl;
        });
    </script>
</body>
</html>

