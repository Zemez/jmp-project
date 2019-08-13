<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />

<h2>CRUD: user</h2>

<form action="<c:url value="/user"/>" method="post">
  <input type="hidden" name="_method" value="put">
  <jsp:useBean id="user" scope="request" type="com.javamentor.jmp_project.model.User"/>
  <p><label>Id: <input type="text" name="id" value="<c:out value="${user.id}" />" readonly="readonly"></label></p>
  <p><label>Login: <input type="text" name="login" value="<c:out value="${user.login}"/>" readonly="readonly"></label>
  </p>
  <p><label>Password: <input type="text" name="password" value=<c:out value="${user.password}" />></label></p>
  <p><label>Name: <input type="text" name="name" value=<c:out value="${user.name}"/>></label></p>
  <p><label>Email: <input type="text" name="email" value=<c:out value="${user.email}"/>></label></p>
  <p><input type="submit" value="update"></p>
</form>
<form action="<c:url value="/user"/>" method="post">
  <input type="hidden" name="_method" value="delete">
  <input type="hidden" name="id" value="<c:out value="${user.id}"/>">
  <input type="submit" value="delete">
</form>

<jsp:include page="footer.jsp" />
