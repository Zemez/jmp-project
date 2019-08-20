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

<h3>User:</h3>

<jsp:useBean id="user" scope="session" type="com.javamentor.jmp_project.model.User"/>
<form action="<c:url value="${user.role=='admin' ? '/admin/update' : '/user/update'}"/>" method="post">
  <jsp:useBean id="_user" scope="request" type="com.javamentor.jmp_project.model.User"/>
  <p><label>Id: <input type="text" name="id" value="<c:out value="${_user.id}" />" readonly="readonly"></label></p>
  <p><label>Login: <input type="text" name="login" value="<c:out value="${_user.login}"/>" readonly="readonly"></label>
  </p>
  <p><label>Password: <input type="text" name="password" value=<c:out value="${_user.password}"/>></label></p>
  <p>
    <label>Role:
      <select name="role" default="user">
        <option value="user"<c:if test="${_user.role=='user'}"> selected</c:if>>user</option>
        <option value="admin"<c:if test="${_user.role=='admin'}"> selected</c:if>>admin</option>
      </select>
    </label>
  </p>
  <p><label>Name: <input type="text" name="name" value=<c:out value="${_user.name}"/>></label></p>
  <p><label>Email: <input type="text" name="email" value=<c:out value="${_user.email}"/>></label></p>
  <p><input type="submit" value="update"></p>
</form>

<form action="<c:url value="${user.role=='admin' ? '/admin/delete' : '/user/delete'}"/>" method="get">
  <c:if test="${user.role=='admin'}"><input type="hidden" name="id" value="<c:out value="${_user.id}"/>"></c:if>
  <input type="submit" value="delete">
</form>

</body>
</html>
