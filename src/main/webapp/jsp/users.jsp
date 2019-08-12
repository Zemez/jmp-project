<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />

<h2>CRUD: users</h2>
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
          <td><a href=<c:url value="/user?id=${user.id}" />><c:out value="${user.id}" /></a></td>
          <td><a href=<c:url value="/user?login=${user.login}" />><c:out value="${user.login}" /></a></td>
          <td><c:out value="${user.password}" /></td>
          <td><c:out value="${user.name}" /></td>
          <td><c:out value="${user.email}" /></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>

<jsp:include page="footer.jsp" />
