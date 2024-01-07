<%-- 
    Document   : register
    Created on : Jan 7, 2024, 5:27:35 PM
    Author     : Otto
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.1.1/crypto-js.min.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
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
                border: solid 1px;
                border-radius: 10px 1px;
                padding: 5px;
            }
            #mismatchingPasswords {
                display: none;
            }
        </style>
    </head>
    <body>
        <div>
            <h1 id="title">Set Password</h1>
            <form id="registerForm" action="register" method="post">
                <% if ((boolean) request.getAttribute("validToken")) { %>
                    <label id="mismatchingPasswords">The passwords do not match.</label><br>
                    <label>Password</label><br>
                    <input type="password" id="password" name="password"><br>
                    <label>Confirm password</label><br>
                    <input type="password" id="confirmPassword" name="confirmPassword"><br>
                    <input type="hidden" id="hashedPassword" name="hashedPassword">
                    <button type="button" onclick="submitPassword()">Register</button>
                <% } else { %>
                    <label>The account creation invite is no longer valid.</label><br><br>
                    <button type="submit">Return to home</button>
                <% } %>
            </form>
        </div>
        <script>
            function submitPassword() {
                const password = document.getElementById("password").value;
                const confirmedPassword = document.getElementById("confirmPassword").value;
                
                if (confirmedPassword === password) {
                    document.getElementById("hashedPassword").value = CryptoJS.SHA256(password).toString();
                
                    document.getElementById("registerForm").submit();
                } else {
                    document.getElementById("mismatchingPasswords").style.display = "block";
                }
            }
        </script>
    </body>
</html>
