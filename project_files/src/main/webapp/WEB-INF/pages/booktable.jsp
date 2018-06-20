<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <table class="index">
        <tr>
            <th class="id">Id</th>
            <th class="title">Название</th>
            <th class="description">Описание</th>
            <th class="author">Автор</th>
            <th class="isbn">ISBN</th>
            <th class="year">Год</th>
            <th class="read-already">Прочитана</th>
            <th class="actions"></th>
        </tr>
    <c:forEach items="${books}" var="element">
        <tr>
            <td class="id">${element.id}</td>
            <td class="title">${element.title}</td>
            <td class="description">${element.description}</td>
            <td class="author">${element.author}</td>
            <td class="isbn">${element.isbn}</td>
            <td class="year">${element.printYear}</td>
            <td class="read-already read-already-${element.readAlready}">
              <a class="read-already read-already-${element.readAlready}" href="/book/${element.id}/toggle-read-already<c:if test="${pageNum > 0}">?page=${pageNum}</c:if>">${element.readAlready}</a>
            </td>
            <td class="actions">
                <a href="/book/${element.id}<c:if test="${pageNum > 0}">?page=${pageNum}</c:if>">
                    <img src="/static/edit.gif" width="16" height="16" />
                </a>
                <a href="/book/${element.id}/delete<c:if test="${pageNum > 0}">?page=${pageNum}</c:if>">
                    <img src="/static/delete.gif" width="16" height="16" />
                </a>
            </td>
        </tr>
    </c:forEach>
    </table>