<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Detail</title>
    <link rel="stylesheet" href="/css/board/detail.css">
    <!-- Latest compiled and minified CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container">
    <div class="user-chk" hidden>${user}</div>
    <div class="title">
        <h1>${detail.title}</h1>
    </div>
    <div class="detail">
        <h5>작성자 : ${detail.writer}</h5>
        <h5>작성 일자 : ${detail.rdt}</h5>
        <button class="mod hidden btn btn-warning">수정</button>
        <button class="del hidden btn btn-danger">삭제</button>
    </div>
<div class="file">
<c:forEach var="item" items="${file}">
    <div class="attached-file">
        <h5>첨부파일 : </h5><span>${item.fileNm}</span>
    </div>
</c:forEach>
</div>
    <div class="ctnt">
        <h3>${detail.ctnt}</h3>
    </div>

    <div class="btns">
        <div class="prevIboard" style="display: none">${prevIboard}</div>
        <div class="nextIboard" style="display: none">${nextIboard}</div>
        <button class="prev btn btn-light">이전 글</button>
        <button class="toList btn btn-info">목록으로</button>
        <button class="next btn btn-light">다음 글</button>
    </div>
</div>
<script src="/js/detail.js"></script>
</body>
</html>
