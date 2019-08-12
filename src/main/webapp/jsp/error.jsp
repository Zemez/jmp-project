<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp" /><body>

<h2>Exception occurred while processing the request</h2>
<p>Type: <c:out value="${pageContext.getException().getClass().toString()}" /></p>
<p>Message: <c:out value="${pageContext.getException().getMessage()}" /></p>
<p>Stack Trace:</p>
<ul>
  <c:forEach var="ste" items="${pageContext.getException().getStackTrace()}">
    <li><c:out value="${ste}" /></li>
  </c:forEach>
</ul>

<jsp:include page="footer.jsp" />
