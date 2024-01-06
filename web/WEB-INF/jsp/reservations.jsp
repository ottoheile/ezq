<%-- 
    Document   : reservations
    Created on : 5 Jan 2024, 15:14:15
    Author     : Otto
--%>

<%@ page import="models.CourseModel, models.ListModel" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reservations</title>
        <style>
            html {
                background-color: lightgrey;
            }
            body {
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
            h3 {
                margin-left: auto;
                margin-right: auto;
                text-align: center;
            }
            .container {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
                gap: 30px;
                margin: 0 auto;
            }
            .item {
                height: auto;
                border: solid 1px;
                border-radius: 20px 1px;
                margin-left: 15px;
                margin-right: 15px;
                padding: 8px;
                box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
                font-size: 12px;
            }
            .item form input {
                margin-top: 20px;
                border-radius: 10px 1px;
            }
            .item form input:active {
                background-color: rgb(230,230,230);
            }
            .item button {
                margin-bottom: 5px;
                border-radius: 10px 1px;
            }

            #goback {
                border: solid 1px;
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
        <h2>Reservations</h2>

        <div class="container">
            <% ListModel[] reservations = ((ListModel[]) request.getAttribute("reservations"));
            if (reservations.length > 0) {
                for (int i = 0; i < reservations.length; i++) { %>
                    <div class="item">
                        <form action="reservations" method="post" id="bookingForm<%=i%>">
                            <h3><%= CourseModel.getCourseNameFromListID(reservations[i].getId()) %></h3>
                            <label><%= reservations[i].getStartTime() %> </label><br>
                            <p><%= reservations[i].getDescription() %></p>
                            <p>Location: <%= reservations[i].getLocation() %></p>
                            <input type="hidden" name="cancel<%= i %>" value="<%= reservations[i].getId() %>">
                            <button type="button" onclick="confirmCancel(<%=i%>)">Cancel reservation</button>
                            <input type="hidden" name="formIndex" value="<%=i%>"/>
                        </form>
                    </div>
                <% } %>
            <% } else { %>
            <h3>No reservations found</h3>
            <% }%>
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
            
            function confirmCancel(formIndex) {
                if (confirm("Are you sure you want to remove this reservation?")) {
                    document.getElementById("bookingForm" + formIndex).submit();
                }
            }
        </script>
    </body>
</html>
