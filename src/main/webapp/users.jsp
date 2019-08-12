<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>User Java Bean Page</title>
  <style>
    table, th, td {
      color: #2b2b2b;
      border: 1px solid #2b2b2b;
      border-collapse: collapse;
      padding: 5px;
    }
  </style>
</head>
<body>
<div>
  <table>
    <thead>
      <tr><th>Id</th><th>Login</th><th>Password</th><th>Name</th><th>Email</th></tr>
    </thead>
    <tbody>
      <jsp:useBean id="users" scope="session" type="java.util.List<com.javamentor.jmp_project.model.User>"/>
      <c:forEach var="user" items="${users}">
        <tr>
          <jsp:useBean id="user" scope="page" type="com.javamentor.jmp_project.model.User"/>
          <td><c:out value="${user.id}" /></td>
          <td><c:out value="${user.login}" /></td>
          <td><c:out value="${user.password}" /></td>
          <td><c:out value="${user.name}" /></td>
          <td><c:out value="${user.email}" /></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
