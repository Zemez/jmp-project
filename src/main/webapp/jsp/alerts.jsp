<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="error" type="com.javamentor.jmp_project.util.AlertMessage"--%>
<c:if test="${error.show}">
  <p class="error"><c:out value="${error.toString()}"/></p>
</c:if>
<%--@elvariable id="note" type="com.javamentor.jmp_project.util.AlertMessage"--%>
<c:if test="${note.show}">
  <p class="note"><c:out value="${note.toString()}"/></p>
</c:if>
