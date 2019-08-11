<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%
  String exception = pageContext.getException().getClass().toString();
  String message = pageContext.getException().getMessage();
  StackTraceElement[] stack = pageContext.getException().getStackTrace();
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Exception</title>
</head>
<body>
<h2>Exception occurred while processing the request</h2>
<p>Type: <%= exception%>
</p>
<p>Message: <%= message %>
</p>
<p>Stack Trace:
<ul><% for (StackTraceElement ste : stack) out.println("<li>" + ste.toString() + "</li>"); %></ul>
</p>
</body>
</html>
