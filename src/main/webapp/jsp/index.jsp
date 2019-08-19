<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>User Java Bean Page</title>
  <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>"/>
</head>
<body>

<c:import url="nav.jsp"/>
<c:import url="alerts.jsp"/>

<h2>CRUD</h2>

<p>Get all users: <a href="<c:url value="/user/all"/>">all users</a></p>

<p>
<form action="<c:url value="/user"/>" method="get">
  <label>Get user by id: <input type="number" name="id"><input type="submit" value="get"></label>
</form>

<p>
<form action="<c:url value="/user"/>" method="get">
  <label>Get user by login: <input type="text" name="login"><input type="submit" value="get"></label>
</form>

<h3>Create user:</h3>

<form action="<c:url value="/user/create"/>" method="post">
  <%--@elvariable id="user" type="com.javamentor.jmp_project.model.User"--%>
  <p><label>Login: <input type="text" name="login" value="<c:out value="${empty user ? null : user.login}"/>"></label>
  </p>
  <p><label>Password: <input type="text" name="password" value=<c:out
      value="${empty user ? null : user.password}"/>></label></p>
  <p><label>Name: <input type="text" name="name" value=<c:out value="${empty user ? null : user.name}"/>></label></p>
  <p><label>Email: <input type="text" name="email" value=<c:out value="${empty user ? null : user.email}"/>></label></p>
  <p><input type="submit" value="submit"></p>
</form>

</body>
</html>
