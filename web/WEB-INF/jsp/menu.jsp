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
            height: 100px; /* Set the height as needed */
            border: solid 1px; /* Add a border for better visibility */
            text-align: center;
            border-radius: 20px 1px;
            margin-left: 15px;
            margin-right: 15px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
        }
        .item form{
            height: 100%;
            padding: 10px;
        }
        .item form input{
            margin-top: 20px;
            border-radius: 10px 1px;
        }
        .item form input:active{
            background-color: rgba(230,230,230);
        }
        .item button:active{
            background-color: rgba(230,230,230);
        }
        .item button{
            margin-top: 20px;
            border-radius: 10px 1px;
        }
        #popup {
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
        #popup form button{
            position: absolute;
            right: 20px;
        }
        #popup #titel{
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
        
    </style>
</head>
    <body>
        <h2>Welcome <%= ((String) request.getSession().getAttribute("email")).split("@")[0] %></h2>
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
            <% if ((boolean) request.getSession().getAttribute("admin")) { %>
            <div class="item">
                        <label>Add course</label><br>
                        <button onclick="showPopup()">Add</button>
            </div>
            <% } else {%>
                <div class="item">
                        <form action="menu" method="post">
                            <label>Show my reservations</label><br>
                            <input type="submit" name="course" value="Reservations">
                        </form>
                </div>
            <% } %>

            <div id="popup">
                <h2>Add Course</h2>
                <form id="popupForm" action="menu" method="post">
                  <!-- Your input fields go here -->
                  <label for="inputField">Titel:</label>
                  <input type="text" id="titel" name="titel" required><br>
                  <input type="submit" id="add" name="course" value="Add">
                  <button onclick="closePopup()">Close</button>
                </form>
            </div>
        </div>

        <form  id="logoutForm" action="menu" method="post">
            <input id="logout" type="submit" name="course" value="LogOut">
        </form>
        <script>
            // Function to show the popup
            function showPopup() {
              document.getElementById('popup').style.display = 'block';
            }

            // Function to close the popup
            function closePopup() {
              document.getElementById('popup').style.display = 'none';
            }

            // Function to handle confirmation (you can modify this based on your needs)
            function confirmPopup() {
              // Add your logic here to handle form data or perform any other actions
              alert('Form submitted successfully!');
              closePopup(); // Close the popup after processing
            }
         </script>
    </body>
</html>
