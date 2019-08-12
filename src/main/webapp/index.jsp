<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>jmp-project webapp</title>
</head>
<body>
<h2>CRUD</h2>
<p>Get all users: <a href="/users">all users</a></p>
<p>Get user by id: <form action="/user" method="get"><input type="number" name="id"><input type="submit" value="get"></form> </p>
<p>Get user by login: <form action="/user" method="get"><input type="text" name="login"><input type="submit" value="get"></form> </p>
</body>
</html>
