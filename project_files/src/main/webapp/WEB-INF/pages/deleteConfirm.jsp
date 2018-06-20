<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:include page="header.jsp" />
<body>
<jsp:include page="navigation.jsp" />
<jsp:include page="messages.jsp" />
<div>&nbsp;</div>
<div>Вы уверены что хотите удалить эту книгу?</div>
<form:form method="POST" modelAttribute="confirmation" action="" >
    <c:if test="${not empty description}"><div class="confirm-description">${description}</div></c:if>
    <c:if test="${not empty message}"><div class="confirm-message">${message}</div></c:if>
    <form:hidden path="id"/>
    <input type="submit" name="op" value="Удалить"/>
    <input type="submit" name="op" value="Отмена"/>
</form:form>
<div>&nbsp;</div>
<jsp:include page="footer.jsp" />
</body>
</html>