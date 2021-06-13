<%--
  Created by IntelliJ IDEA.
  User: teo07
  Date: 12.06.2021
  Time: 14:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Meal Form</h2>
<form action="/topjava/meals?action=add" method="post">
    id: <input type="text" readonly="readonly" name="id" value="${meal.id}"/><br>
    date: <input type="datetime-local" name="dateTime" value="${meal.dateTime}"/><br>
    description: <input type="text" name="description" value="${meal.description}"/><br>
    amount of calories: <input type="text" name="calories" value="${meal.calories}"/><br>
    <input type="submit" value="Submit">
</form>
</body>
</html>
