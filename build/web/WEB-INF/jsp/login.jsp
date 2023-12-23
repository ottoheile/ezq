<%-- 
    Document   : login
    Created on : 23 Dec 2023, 20:41:00
    Author     : André
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <style>
            body{
                background-image: url("https://i.pinimg.com/originals/88/15/63/881563d6444b370fa4ceea0c3183bb4c.gif");
                background-repeat: no-repeat;
                background-size: cover;
                color: white;
            }
            form {
                margin-left: auto;
                margin-right: auto;
                width: 20%;
                text-align: center;
                border: solid;
            }
            form input{
                margin-bottom: 20px;
            }
            #title {
                width: 50%;
                margin-left: auto;
                margin-right: auto;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <div>
            <h1 id="title">Log in to your account!</h1>
            <form action="login" method="post">
                <label>Email</label><br>
                <input type="email" id="email" name="email" required><br>
                <label>Password</label><br>
                <input type="password" id="password" name="password" required><br>
                <input type="submit" value="Create account/Log in">
            </form>
        </div>
    </body>
</html>
