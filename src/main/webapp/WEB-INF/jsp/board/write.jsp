<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Write</title>
    <link rel="stylesheet" href="/css/board/write.css">
</head>
<body>
<div class="write-top">
    <div>
        <h1>공지사항 등록</h1>
    </div>
    <h5>(*)필수 입력 항목</h5>
</div>
<div class="write-body">
    <table>
        <tr>
            <td>제목(*)</td>
            <td><input class="title" id="title" name="title" type="text"></td>
            <td>상단공지 여부(*)</td>
            <td>
                <form class="fix">
                    <input type="radio" name="fix" value="1" /> 예
                    <input type="radio" name="fix" checked="checked" value="0" /> 아니오
                </form>
            </td>
        </tr>
        <tr>
            <td>작성자(*)</td>
            <td><input class="writer" id="writer" name="writer" value="${loginUser}" type="text" readonly></td>
            <td>작성일자(*)</td>
            <td><input class="rdt" type="date" id="rdt" name="rdt"></td>
        </tr>
        <tr>
            <td>내용(*)</td>
            <td colspan="3"><textarea class="ctnt" cols="100" rows="10"></textarea></td>
        </tr>
        <tr>
            <td>영상파일</td>
            <td colspan="3"></td>
        </tr>
        <tr>
            <td>첨부파일</td>
            <td colspan="3">
                <button>파일선택</button>
                <span>※ 첨부파일 당 최대 5MB까지 업로드 가능하며, 최대5개까지 등록할 수 있습니다.</span></td>
        </tr>
        <tr>
            <td></td>
            <td colspan="3">첨부파일~</td>
        </tr>
    </table>
</div>
<div class="write-bottom">
    <button>취소</button>
    <button class="save-btn">저장</button>
</div>
<script src="/js/write.js"></script>
</body>
</html>
