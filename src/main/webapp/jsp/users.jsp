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

<h3>Users:</h3>

<div>
  <table>
    <thead>
    <tr>
      <th>Id</th>
      <th>Login</th>
      <th>Password</th>
      <th>Role</th>
      <th>Name</th>
      <th>Email</th>
    </tr>
    </thead>
    <tbody>
    <jsp:useBean id="users" scope="request" type="java.util.List<com.javamentor.jmp_project.model.User>"/>
      <c:forEach var="user" items="${users}">
        <tr>
          <jsp:useBean id="user" type="com.javamentor.jmp_project.model.User"/>
          <td><a href=<c:url value="/admin?id=${user.id}"/>><c:out value="${user.id}"/></a></td>
          <td><a href=<c:url value="/admin?login=${user.login}"/>><c:out value="${user.login}"/></a></td>
          <td><c:out value="${user.password}" /></td>
          <td><c:out value="${user.role}"/></td>
          <td><c:out value="${user.name}" /></td>
          <td><c:out value="${user.email}" /></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>

</body>
</html>
