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

<h3>Sign In:</h3>

<form action="j_security_check" method="post">
  <%--@elvariable id="_login" type="java.lang.String"--%>
  <p><label>Login: <input type="text" name="j_username" value="<c:out value='${_login}'/>"></label></p>
  <p><label>Password: <input type="password" name="j_password"></label></p>
  <p><input type="submit" value="submit"></p>
</form>

</body>
</html>
