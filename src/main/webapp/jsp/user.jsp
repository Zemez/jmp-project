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

<h2>CRUD: user</h2>

<form action="<c:url value="/user/update"/>" method="post">
  <jsp:useBean id="user" scope="request" type="com.javamentor.jmp_project.model.User"/>
  <p><label>Id: <input type="text" name="id" value="<c:out value="${user.id}" />" readonly="readonly"></label></p>
  <p><label>Login: <input type="text" name="login" value="<c:out value="${user.login}"/>" readonly="readonly"></label>
  </p>
  <p><label>Password: <input type="text" name="password" value=<c:out value="${user.password}"/>></label></p>
  <p><label>Name: <input type="text" name="name" value=<c:out value="${user.name}"/>></label></p>
  <p><label>Email: <input type="text" name="email" value=<c:out value="${user.email}"/>></label></p>
  <p><input type="submit" value="update"></p>
</form>

<form action="<c:url value="/user/delete"/>" method="get">
  <input type="hidden" name="id" value="<c:out value="${user.id}"/>">
  <input type="submit" value="delete">
</form>

</body>
</html>
