<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>User Java Bean Page</title>
</head>
<body>
<div>
  <jsp:useBean id="user" scope="session" type="com.javamentor.jmp_project.model.User"/>
  <p>Id: <c:out value="${user.id}" /></p>
  <p>Login: <c:out value="${user.login}" /></p>
  <p>Password: <c:out value="${user.password}" /></p>
  <p>Name: <c:out value="${user.name}" /></p>
  <p>Email: <c:out value="${user.email}" /></p>
  <p>SessionID: <c:out value="${cookie.JSESSIONID.value}" /></p>
  <p>User-Agent: <c:out value="${header['User-Agent']}" /></p>
</div>
</body>
</html>
