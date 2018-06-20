<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<jsp:include page="header.jsp" />
<body>
<jsp:include page="navigation.jsp" />
<jsp:include page="messages.jsp" />
<jsp:include page="searchForm.jsp" />
<div>
<jsp:include page="booktable.jsp" />
</div>

<c:if test="${showPager}">
<ul class="pager">
<c:forEach begin="1" end="${totalPages}" step="1" varStatus="i">
  <c:choose>
    <c:when test="${i.index-1 eq pageNum}">
      <li class="active"><span>${i.index}</span></li>
    </c:when>
    <c:otherwise>
      <li><a href="/?page=${i.index-1}">${i.index}</a></li>
    </c:otherwise>
  </c:choose>
</c:forEach>
</ul>

<div class="clearfix"></div>
<div class="bottom-numbers">Книг в библиотеке: <strong>${totalElements}</strong></div>
</c:if>

<c:if test="${isSearch}">
<div class="bottom-numbers">Найдено книг: <strong>${elementsCount}</strong></div>
</c:if>

<jsp:include page="footer.jsp" />
</body>
</html>