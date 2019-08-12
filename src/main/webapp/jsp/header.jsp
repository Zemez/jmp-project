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
    .alert {
      color: red;
      background: lightpink;
      border: 1px solid red;
      padding: 10px;
    }
    .message {
      color: green;
      background: lightgreen;
      border: 1px solid green;
      padding: 10px;
    }
  </style>
</head>
<body>
<a href="/">Index</a>
<%--@elvariable id="alert" type="com.javamentor.jmp_project.util.TemporaryMessage"--%>
<c:if test="${alert.show}">
<p class="alert"><c:out value="${alert.toString()}" /></p>
</c:if>
<%--@elvariable id="message" type="com.javamentor.jmp_project.util.TemporaryMessage"--%>
<c:if test="${message.show}">
<p class="message"><c:out value="${message.toString()}" /></p>
</c:if>
