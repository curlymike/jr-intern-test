<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:include page="header.jsp" />
<body>
<jsp:include page="navigation.jsp" />
<jsp:include page="messages.jsp" />
<div>&nbsp;</div>

<!-- Development output -->
<c:if test="${not empty errors}">
    <div>Got errors, man.</div>
</c:if>

<%-- modelAttribute="book" commandName="book" --%>
<div class="book-form-wrapper">
<form:form method="POST" modelAttribute="book" action="" >
    <table>
        <tr class="title">
            <td><form:label path="title">Название</form:label></td>
            <td>
                <form:input path="title" size="50" />
                <form:errors path="title" cssClass="error" />
            </td>
        </tr>
        <tr class="description">
            <td><form:label path="description">Описание</form:label></td>
            <td>
                <form:textarea path="description" rows="8" cols="60" />
                <form:errors path="description" cssClass="error" />
            </td>
        </tr>
        <tr class="author">
            <td><form:label path="author">Автор</form:label></td>
            <td>
                <c:choose>
                    <c:when test="${not empty bookId}">
                        <%-- Пользователь видит нередактируемый элемент, но
                             браузер не отправит его значение и форма выдаст ошибку
                             "поле Автор не может быть пустым" --%>
                        <form:input path="author" size="40" disabled="true" />
                        <%-- Для этого тут скрытый элемент с тем же именем и со значением,
                             а на backend-е я это поле для существующих книг не обновляю --%>
                        <form:hidden path="author" />
                    </c:when>
                    <c:otherwise>
                        <form:input path="author" size="40" />
                    </c:otherwise>
                </c:choose>
                <form:errors path="author" cssClass="error" />
            </td>
        </tr>
        <tr class="isbn">
            <td><form:label path="isbn">ISBN</form:label></td>
            <td>
                <form:input path="isbn"/>
                <form:errors path="isbn" cssClass="error" />
            </td>
        </tr>
        <tr class="print-year">
            <td><form:label path="printYear">Год</form:label></td>
            <td>
                <form:input path="printYear"/>
                <form:errors path="printYear" cssClass="error" />
            </td>
        </tr>
        <tr class="read-already">
            <td><form:label path="readAlready">Прочитана</form:label></td>
            <td>
                <span><form:checkbox path="readAlready"/></span><span class="description-text">Это значение так же можно менять в таблице книг щелкнув true/false</span>
            </td>
        </tr>
        <tr class="submit">
            <td></td>
            <td>
            <form:hidden path="id"/>
            <c:choose>
                <c:when test="${not empty bookId}">
                    <input type="submit" value="Сохранить"/>
                </c:when>
                <c:otherwise>
                    <input type="submit" value="Добавить"/>
                </c:otherwise>
            </c:choose>
            </td>
        </tr>
    </table>
</form:form>
</div>

<div>&nbsp;</div>

<c:if test="${empty bookId}">
<div class="remote-book-wrapper">
<div class="remote-book-top-text">Можно загрузить информацию о книге с сайта litres.ru, для этого укажите в строке ниже адрес страницы с описанием книги на сайте litres.ru</div>
<form method="GET" action="" >
    <table>
        <tr>
            <td><label for="url">URL<label></td>
            <td>
                <input name="url" size="60" value="${remoteUrl}" />
            </td>
            <td><input type="submit" value="Загрузить"/></td>
        </tr>
    </table>
</form>
<div class="remote-book-bottom-text">Пример: <strong>https://www.litres.ru/mett-vaysfeld/obektno-orientirovannoe-myshlenie/</strong></div>
</div>
</c:if>

<div>&nbsp;</div>

<jsp:include page="footer.jsp" />
</body>
</html>

