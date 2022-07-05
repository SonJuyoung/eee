<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Board</title>
</head>
<body>
<div class="board-top">
    <h1>공지사항</h1>
</div>
<table>
    <tr>
        <th>상태</th>
        <th>NO</th>
        <th><input type="checkbox"></th>
        <th>제목</th>
        <th>작성자</th>
        <th>최종 수정일자</th>
    </tr>
    <c:forEach var="item" items="${list}">
    <tr>
        <td>${item.fix}</td>
        <td>${item.iboard}</td>
        <td><input type="checkbox"></td>
        <td>${item.title}</td>
        <td>${item.writer}</td>
        <td>${item.rdt}</td>
    </tr>
    </c:forEach>
</table>
</body>
</html>
