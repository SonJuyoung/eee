<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Detail</title>
    <link rel="stylesheet" href="/css/board/detail.css">
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
        <button class="mod hidden">수정</button>
        <button class="del hidden">삭제</button>
    </div>
<div class="file">
    <span><h5>첨부파일 : </h5>${detail.file}</span>
</div>
    <div class="ctnt">
        <h3>${detail.ctnt}</h3>
    </div>

    <div class="btns">
        <div class="prevIboard" style="display: none">${prevIboard}</div>
        <div class="nextIboard" style="display: none">${nextIboard}</div>
        <button class="prev">이전 글</button>
        <button class="toList">목록으로</button>
        <button class="next">다음 글</button>
    </div>
</div>
<script src="/js/detail.js"></script>
</body>
</html>
