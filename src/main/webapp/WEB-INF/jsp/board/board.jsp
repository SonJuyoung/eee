<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Board</title>
    <link rel="stylesheet" href="/css/board/list.css">
    <!-- Latest compiled and minified CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="board-top">
    <div class="logout">
        <button class="btn btn-warning logout-btn">로그아웃</button>
    </div>
    <h1>공지사항</h1>
    <div class="count-search">
        <div class="count">
            <h5>전체 | ${count}</h5>
            <div hidden class="fix-count">${fixListCount}</div>
        </div>
        <div class="search">
            <select name="search" class="form-select search-option">
                <option value="0">전체</option>
                <option value="1">제목</option>
                <option value="2">내용</option>
                <option value="3">작성자</option>
            </select>
            <input class="form-control search-text" type="text">
            <button class="search-btn btn btn-light">검색</button>
        </div>
    </div>
</div>

<table>
    <tr class="table-top">
        <th>상태</th>
        <th>NO</th>
        <th><input type="checkbox"></th>
        <th class="title">제목</th>
        <th>작성자</th>
        <th class="rdt">최종 수정일자</th>
    </tr>
    <c:forEach var="item" items="${fixList}">
        <tr class="table-body">
            <td>${item.fix}</td>
            <td>${item.iboard}</td>
            <td><input type="checkbox"></td>
            <td class="title">${item.title}</td>
            <td>${item.writer}</td>
            <td class="rdt">${item.rdt}</td>
        </tr>
    </c:forEach>
    <c:forEach var="item" items="${list}">
        <tr class="table-body">
            <td>${item.fix}</td>
            <td>${item.iboard}</td>
            <td><input type="checkbox"></td>
            <td class="title">${item.title}</td>
            <td>${item.writer}</td>
            <td class="rdt">${item.rdt}</td>
        </tr>
    </c:forEach>
</table>
<div class="toWrite">
    <a href="/board/write"><button class="btn btn-primary">글쓰기</button></a>
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
