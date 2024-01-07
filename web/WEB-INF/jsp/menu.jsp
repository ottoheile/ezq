<%@ page import="models.CourseModel" %>
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
            position: relative;
            min-height: 300px;
            background-color: white; 
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
            border: solid 1px; /* Add a border for better visibility */
            text-align: center;
            border-radius: 20px 1px;
            margin-left: 15px;
            margin-right: 15px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
        }
        .item form input{
            margin-top: 20px;
            border-radius: 10px 1px;
            margin-bottom: 10px;
        }
        .item form input:active{
            background-color: rgba(230,230,230);
        }
        .item button:active{
            background-color: rgba(230,230,230);
        }
        .item button{
            margin-bottom: 5px;
            border-radius: 10px 1px;
        }
        .popup {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            padding: 20px;
            background-color: #fff;
            border: 1px solid #ccc;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            z-index: 1000;
            border-radius: 10px 1px;
        }
        .popup form button{
            position: absolute;
            right: 20px;
        }
        .popup #title{
            margin-bottom: 10px;
        }
        #logout {
            border: solid 1px; /* Add a border for better visibility */
            border-radius: 20px 1px;
            margin-bottom: 10px;
            padding: 7px;
            
        }
        #logoutForm {
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
        }
        #createUserButton{
            position: absolute;
            top: 49px;
            right: 15px;
            border-radius: 10px 1px;
        }
        <% if (request.getAttribute("inviteSent") != null) { %>
        #popupCreateUser {
            display: block;
        }
        <% } %>
        
    </style>
</head>
    <body>
        <h2>Welcome <%= ((String) request.getSession().getAttribute("email")).split("@")[0] %></h2>
        <% if ((boolean) request.getSession().getAttribute("admin")) { %>
            <button id="createUserButton" onclick="showPopup('popupCreateUser')">Create user</button>
        <% } %>
        
        <div class="container">
            <% CourseModel[] courses = ((CourseModel[]) request.getAttribute("courses"));
            for (int i = 0; i < courses.length; i++) { %>
                <div class="item">
                    <form action="menu" method="post">
                        <label><%= courses[i].getName() %> </label><br>
                        <input type="submit" name="course" value="Show available lists for <%= courses[i].getName() %>">
                    </form>
                    <% if ((boolean) request.getSession().getAttribute("admin")) { %>
                    <form action="menu" method="post" id="deleteForm<%= courses[i].getName() %>">
                        <br>
                        <input type="hidden" name="course" value="<%= courses[i].getName() %> Delete">
                        <button type="button" onclick="confirmDelete('<%= courses[i].getName() %>')">Delete</button>
                    </form>
                    <% } %>
                </div>
            <% } %>
            <% if ((boolean) request.getSession().getAttribute("admin")) { %>
            <div class="item">
                        <label>Add course</label><br>
                        <button onclick="showPopup('popupAddCourse')">Add</button>
            </div>
            <% } else {%>
                <div class="item">
                        <form action="menu" method="post">
                            <label>Show my reservations</label><br>
                            <input type="submit" name="course" value="Reservations">
                        </form>
                </div>
            <% } %>

            <div class="popup" id="popupAddCourse">
                <h2>Add Course</h2>
                <form class="popupForm" action="menu" method="post">
                  <!-- Your input fields go here -->
                  <label for="inputField">Title:</label>
                  <input type="text" id="title" name="title" required><br>
                  <input type="submit" id="add" name="course" value="Add">
                  <button onclick="closePopup('popupAddCourse')">Close</button>
                </form>
            </div>
        </div>
        <div class="popup" id="popupCreateUser">
            <h2>Create New User</h2>
            <form action="menu" method="post">
                <% if(request.getAttribute("inviteSent") != null) { %>
                    <% if((boolean) request.getAttribute("inviteSent")) { %>
                        <h3 id="inviteSent">Invite sent!</h3>
                    <% } else { %>
                        <h3 id="inviteSent">User already exists!</h3>
                    <% } %>
                <% } else { %>
                    <h3 id="inviteSent"></h3>
                <% } %>
                <label for="inputField">Email:</label>
                <input type="text" id="email" name="email" required><br>
                <label for="adminCheckbox">Admin:</label>
                <input type="checkbox" id="adminCheckbox" name="adminCheckbox"><br>
                <input type="submit" id="submitInputCreateUser" name="course" value="Send invite">
                <button type="button" onclick="closeCreateUserPopup()">Close</button>
            </form>
        </div>

        <form  id="logoutForm" action="menu" method="post">
            <input id="logout" type="hidden" name="course" value="LogOut">
            <button id="logout" type="button" onclick="confirmLogOut()">Log out</button>
        </form>
        <script>
            // Function to show the popup
            function showPopup(popupName) {
              document.getElementById(popupName).style.display = 'block';
            }

            // Function to close the popup
            function closePopup(popupName) {
              document.getElementById(popupName).style.display = 'none';
            }
            
            function closeCreateUserPopup() {
                document.getElementById("inviteSent").style.display = "none";
                document.getElementById("email").value = "";
                document.getElementById("adminCheckbox").checked = false;
                closePopup("popupCreateUser");
            }

            // Function to handle confirmation (you can modify this based on your needs)
            function confirmPopup() {
              // Add your logic here to handle form data or perform any other actions
              alert('Form submitted successfully!');
              closePopup(); // Close the popup after processing
            }
            function confirmLogOut() {
                // Display a confirmation dialog
                var isConfirmed = confirm("Are you sure you want to log out?");

                // If the user confirms, submit the form
                if (isConfirmed) {
                    var form = document.getElementById('logoutForm');
                    form.submit();
                }
            }
            function confirmDelete(courseName) {
                // Display a confirmation dialog
                var isConfirmed = confirm("Are you sure you want to delete this course?");
                // If the user confirms, submit the form
                if (isConfirmed) {
                    var deleteform = 'deleteForm' + courseName;
                    var form = document.getElementById(deleteform);
                    form.submit();
                }
            }

         </script>
    </body>
</html>