<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" />

<h2>CRUD</h2>

<p>Get all users: <a href="<c:url value="/users"/>">all users</a></p>

<p>
<form action="<c:url value="/user"/>" method="get">
  <label>Get user by id: <input type="number" name="id"><input type="submit" value="get"></label>
</form>

<p>
<form action="<c:url value="/user"/>" method="get">
  <label>Get user by login: <input type="text" name="login"><input type="submit" value="get"></label>
</form>

<p>Create user:</p>

<form action="<c:url value="/user"/>" method="post">
  <p><label>Login: <input type="text" name="login"></label></p>
  <p><label>Password: <input type="text" name="password"></label></p>
  <p><label>Name: <input type="text" name="name"></label></p>
  <p><label>Email: <input type="text" name="email"></label></p>
  <p><input type="submit" value="submit"></p>
</form>

<jsp:include page="footer.jsp" />
