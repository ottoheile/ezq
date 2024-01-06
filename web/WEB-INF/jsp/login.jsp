<%-- 
    Document   : login
    Created on : 23 Dec 2023, 20:41:00
    Author     : André, Otto
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.1.1/crypto-js.min.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <style>
            body{
                background-color: lightgrey;
            }
            div{
                background-color: white;
                width: 50%;
                margin-left: auto;
                margin-right: auto;
                border-radius: 10px;
                box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
                padding-bottom: 50px;
            }
            form {
                margin-left: auto;
                margin-right: auto;
                width: 20%;
                text-align: center;
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
            #button {
                border: solid 1px; /* Add a border for better visibility */
                border-radius: 10px 1px;
                padding: 5px;
            }
        </style>
    </head>
    <body>
        <div>
            <h1 id="title">Login</h1>
            <form id="loginForm" action="login" method="post">
                <% if ((boolean) request.getAttribute("wrongCred")) {%>
                <label> Wrong credentials</label><br><br>
                <% } %>
                <label>Email</label><br>
                <input type="email" id="email" name="email" required><br>
                <label>Password</label><br>
                <input type="password" id="password" name="password" required><br>
                <input type="hidden" id="hashedPassword" name="hashedPassword">
                <button type="button" onclick="submitLogin()">Login</button>
            </form>
        </div>
        <script>
            function submitLogin() {
                const password = document.getElementById("password").value;
                document.getElementById("password").value = null;
                
                document.getElementById("hashedPassword").value = CryptoJS.SHA256(password).toString();
                
                document.getElementById("loginForm").submit();
            }
        </script>
    </body>
</html>
