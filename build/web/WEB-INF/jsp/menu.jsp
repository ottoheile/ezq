<%@ page import="models.CourseModel" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Menu</title>
    <style>
        html{
            background-color: #1E1E1E  ;
        }
        body{
            width: 50%;
            margin-left: auto;
            margin-right: auto;
            background-color: aliceblue;
            height: 100vh;
            border: solid 1px;
            border-radius: 10px;
        }
        h1 {
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
            height: 100px; /* Set the height as needed */
            background-color: lightblue; /* lightblue Set the background color as needed */
            font-size: 20px;
            border: solid 1px; /* Add a border for better visibility */
            text-align: center;
            border-radius: 20px 1px;
            margin-left: 15px;
            margin-right: 15px;
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
    </style>
</head>
<body>
    <h1>Welcome <%= ((String) request.getSession().getAttribute("email")).split("@")[0] %></h1>
    <div class="container">
        <% CourseModel[] courses = ((CourseModel[]) request.getAttribute("courses"));
        for (int i = 0; i < courses.length; i++) { %>
            <div class="item">
                <form action="menu" method="post">
                    <label><%= courses[i].getName() %> </label><br>
                    <input type="submit" name="course" value="Show available lists for <%= courses[i].getName() %>">
                </form>
            </div>
        <% } %>
        <div class="item">
                <form action="menu" method="post">
                    <label>Add course</label><br>
                    <input type="submit" name="course" value="Add">
                </form>
        </div>
    </div>
</body>
</html>
