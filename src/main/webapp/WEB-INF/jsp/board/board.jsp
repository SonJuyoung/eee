<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Board</title>
    <link rel="stylesheet" href="/css/board/list.css">
</head>
<body>
<div class="board-top">
    <h1>공지사항</h1>
    <div class="count-search">
        <div class="count">
            <h5>전체 | ${count}</h5>
            <div hidden class="fix-count">${fixListCount}</div>
        </div>
        <div class="search">
            <select name="search" class="search-option">
                <option value="0">전체</option>
                <option value="1">제목</option>
                <option value="2">내용</option>
                <option value="3">작성자</option>
            </select>
            <input class="search-text" type="text">
            <button class="search-btn">검색</button>
        </div>
    </div>
</div>

<table>
    <tr class="table-top">
        <th>상태</th>
        <th>NO</th>
        <th><input type="checkbox"></th>
        <th>제목</th>
        <th>작성자</th>
        <th>최종 수정일자</th>
    </tr>
    <c:forEach var="item" items="${fixList}">
        <tr class="table-body">
            <td>${item.fix}</td>
            <td>${item.iboard}</td>
            <td><input type="checkbox"></td>
            <td>${item.title}</td>
            <td>${item.writer}</td>
            <td>${item.rdt}</td>
        </tr>
    </c:forEach>
    <c:forEach var="item" items="${list}">
        <tr class="table-body">
            <td>${item.fix}</td>
            <td>${item.iboard}</td>
            <td><input type="checkbox"></td>
            <td>${item.title}</td>
            <td>${item.writer}</td>
            <td>${item.rdt}</td>
        </tr>
    </c:forEach>
</table>
<div class="toWrite">
    <a href="/board/write"><button>글쓰기</button></a>
</div>
<div class="pagination">
<%--    <p><</p>--%>
<%--    <c:forEach var="i" begin="1" end="${count/10+1}">--%>
<%--        <p class="pageNum">${i}</p>--%>
<%--    </c:forEach>--%>
<%--    <p>></p>--%>
</div>
<script src="/js/board.js"></script>
</body>
</html>
