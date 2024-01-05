<%-- 
    Document   : course
    Created on : 23 Dec 2023, 23:11:18
    Author     : André, Otto
--%>

<%@ page import="models.ListModel, models.UserModel" %>
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
            font-size: 12px;
        }
        .item form input{
            margin-top: 20px;
            border-radius: 10px 1px;
        }
        .item form input:active{
            background-color: rgba(230,230,230);
        }
        .item button{
            margin-bottom: 5px;
            border-radius: 10px 1px;
        }
        
        #goback {
            border: solid 1px; /* Add a border for better visibility */
            border-radius: 20px 1px;
            margin-bottom: 10px;
            padding: 5px;
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
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
            width: 100%;
            box-sizing: border-box;
        }
        #popup #title{
            margin-bottom: 10px;
        }
        #popupform label{
            text-align: right;
            margin: 10px;
        }
        #popupForm {
            display: grid;
            gap: 5px;
            grid-template-columns: 1fr 1fr;
        }
        #popupForm input {
            width: 100%;
            box-sizing: border-box;
        }
    </style>
</head>
<body>
    <h2> Course <%= (String) request.getSession().getAttribute("courseName")%></h2>
    
    <div class="container">
        <% ListModel[] lists = ((ListModel[]) request.getAttribute("lists"));
        UserModel user = (UserModel) request.getSession().getAttribute("user");
        boolean isAdmin = (boolean) request.getSession().getAttribute("admin");
        
        for (int i = 0; i < lists.length; i++) {
            boolean userHasBooking = lists[i].userHasBookedList(user.getId(), lists[i].getId());
            if ((lists[i].slotsLeft() > 0) || userHasBooking || isAdmin) { %>
                <div class="item">
                    <form action="course" method="post" id="bookingForm<%=i%>">
                        <label><%= lists[i].getStartTime() %> </label><br>
                        <p><%= lists[i].getDescription() %></p>
                        <p>Location: <%= lists[i].getLocation() %></p>
                        <p>Slots left: <%= lists[i].slotsLeft() %></p>
                        <% if (isAdmin) { %>
                            <% if (((boolean[]) request.getAttribute("userNotExistsTextHelper"))[i]) { %>
                                <p>User does not exist.</p>
                            <% } %>
                            <label for="inputField">User email:</label>
                            <input type="text" id="userBooked" name="userBooked<%=i%>">
                        <% } %>
                        <input type="hidden" name="list" value="<%= lists[i].getId() %> <%= userHasBooking && !isAdmin ? "Cancel" : "Book" %>">
                        <% if (!userHasBooking || isAdmin) { %>
                            <% if (lists[i].slotsLeft() > 0) { %>
                                <button type="submit">Book slot</button>
                            <% } else { %>
                                <button disabled>Book slot</button>
                            <% } %>
                        <% } else { %>
                            <button type="submit">Cancel reservation</button>
                        <% } %>
                        <input type="hidden" name="formIndex" value="<%=i%>"/>
                    </form>
                    <% if ((boolean) request.getSession().getAttribute("admin")) { %>
                    <form action="course" method="post" id="deleteForm">
                        <br>
                        <input type="hidden" name="list" value="<%= lists[i].getId() %> Delete">
                        <button type="button" onclick="confirmDelete()">Delete</button>
                    </form>
                    <% } %>
                </div>
                <% } %>
        <% } %>
        <% if ((boolean) request.getSession().getAttribute("admin")) { %>
        <div class="item">
            <label>Add List</label><br>
            <button type="button" onclick="showPopup()">Add</button>
        </div>
        <% } %>
        
        <div id="popup">
                <h2>Add List</h2>
                <form id="popupForm" action="course" method="post">
                  <!-- Your input fields go here -->
                  <label for="inputField">Start time:</label>
                  <input type="datetime-local" id="datetime" name="datetime" required>
                  <label for="inputField">Duration:</label>
                  <input type="number" id="duration" name="duration" title="Please enter a valid number" required>
                  <label for="inputField">Max slots:</label>
                  <input type="number" id="maxslots" name="maxslots" title="Please enter a valid number" required>
                  <label for="inputField">Location:</label>
                  <input type="text" id="location" name="location" required>
                  <label for="inputField">Description:</label>
                  <input type="text" id="description" name="description" required>
                  <input id="addButton" type="submit" name="list" value="Add">
                  <button onclick="closePopup()">Close</button>
                </form>
        </div>
        </div>
    <button id="goback"> Go back </button>
    <script>
        document.getElementById('goback').addEventListener('click', function() {
            var currentUrl = window.location.href;
            
            var newPart = 'menu';
            
            var urlParts = currentUrl.split('/');
            
            urlParts[urlParts.length - 1] = newPart;
            
            var newUrl = urlParts.join('/');
            
            window.location.href = newUrl;
        });
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
        function confirmDelete() {
                // Display a confirmation dialog
                var isConfirmed = confirm("Are you sure you want to delete this list?");

                // If the user confirms, submit the form
                if (isConfirmed) {
                    var form = document.getElementById('deleteForm');
                    form.submit();
                }
            }
    </script>
</body>
</html>

