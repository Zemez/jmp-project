<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Exception</title>
</head>
<body>
<h2>Exception occurred while processing the request</h2>
<p>Type: <c:out value="${pageContext.getException().getClass().toString()}" /></p>
<p>Message: <c:out value="${pageContext.getException().getMessage()}" /></p>
<p>Stack Trace:</p>
<ul>
  <c:forEach var="ste" items="${pageContext.getException().getStackTrace()}">
    <li><c:out value="${ste}" /></li>
  </c:forEach>
</ul>
</body>
</html>
