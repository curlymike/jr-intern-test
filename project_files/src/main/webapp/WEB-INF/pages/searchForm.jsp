<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<form:form method="GET" modelAttribute="search" action="" >
    <table>
        <tr>
            <td>
                <input name="searchText" size="30" value="${searchText}" />
            </td>
            <td>
                <select name="searchScope">
                    <option value="title" <c:if test="${searchScopeTitle}">selected="selected"</c:if> >Название</option>
                    <option value="description" <c:if test="${searchScopeDescription}">selected="selected"</c:if> >Описание</option>
                    <option value="both" <c:if test="${searchScopeBoth}">selected="selected"</c:if> >Оба поля</option>
                </select>
            </td>
            <td>
                <input type="submit" value="Искать"/>
            </td>
        </tr>
    </table>
</form:form>