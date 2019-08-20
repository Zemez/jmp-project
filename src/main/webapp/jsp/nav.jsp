<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav>
  <a href="<c:url value='/'/>">Index</a> |
  <a href="<c:url value='/user'/>">User</a> |
  <a href="<c:url value='/admin/all'/>">All users</a> |
  <a href="<c:url value='/signup'/>">SignUp</a> |
  <%--@elvariable id="user" type="com.javamentor.jmp_project.model.User"--%>
  <c:choose>
    <c:when test='${empty user}'>
      <a href="<c:url value='/signin'/>">SignIn</a>
    </c:when>
    <c:otherwise>
      <a href="<c:url value='/signout'/>">SignOut</a>
    </c:otherwise>
  </c:choose>
</nav>
