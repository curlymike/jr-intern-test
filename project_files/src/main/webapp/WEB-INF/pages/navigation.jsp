<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="navigation">
    <a href="/">Главная</a>
    <a href="/book/add<c:if test="${pageNum > 0}">?page=${pageNum}</c:if>">Добавить книгу</a>
</div>
