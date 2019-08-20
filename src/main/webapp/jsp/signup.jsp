<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>User Java Bean Page</title>
  <link rel="stylesheet" type="text/css" href="<c:url value='/css/style.css'/>"/>
</head>
<body>

<c:import url='nav.jsp'/>
<c:import url='alerts.jsp'/>

<h3>Sign Up</h3>

<form action="<c:url value='/signup'/>" method="post">
  <%--@elvariable id="__user" type="com.javamentor.jmp_project.model.User"--%>
  <p><label>Login: <input type="text" name="login"
                          value="<c:out value="${empty __user ? null : __user.login}"/>"></label></p>
  <p><label>Password: <input type="text" name="password"
                             value="<c:out value='${empty __user ? null : __user.password}'/>"></label></p>
  <p>
    <label>Role:
      <select name="role" default="user">
        <option value="user"<c:if test="${not empty __user && __user.role=='user'}"> selected</c:if>>user</option>
        <option value="admin"<c:if test="${not empty __user && __user.role=='admin'}"> selected</c:if>>admin</option>
      </select>
    </label>
  </p>
  <p><label>Name: <input type="text" name="name" value="<c:out value='${empty __user ? null : __user.name}'/>"></label>
  </p>
  <p><label>Email: <input type="text" name="email"
                          value="<c:out value='${empty __user ? null : __user.email}'/>"></label></p>
  <p><input type="submit" value="submit"></p>
</form>

</body>
</html>
