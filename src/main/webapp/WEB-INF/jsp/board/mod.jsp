<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modify</title>
    <link rel="stylesheet" href="/css/board/write.css">
    <!-- include libraries(jQuery, bootstrap) -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

    <!-- include summernote css/js -->
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
</head>
<body>
<div class="container">
    <div class="write-top">
        <div>
            <h1>게시글 수정</h1>
        </div>
        <div>
            <h5>(*)필수 입력 항목</h5>
        </div>
    </div>
    <div class="write-body">
        <table class="table">
            <tr>
                <td>제목(*)</td>
                <td><input class="title form-control" id="title" name="title" type="text" value="${detail.title}"></td>
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
                <td><input class="writer form-control" id="writer" name="writer" value="${loginUser}" type="text" readonly></td>
                <td>작성일자(*)</td>
                <td><input class="rdt" type="date" id="rdt" name="rdt" disabled></td>
            </tr>
            <tr>
                <td>내용(*)</td>
                <td class="ctnt-elem" colspan="3"><textarea class="ctnt" style="text-align: left" id="summernote" name="ctnt" placeholder="내용을 입력하세요.">${detail.ctnt}</textarea></td>
            </tr>
            <tr>
                <td>첨부파일</td>
                <td colspan="3">
                    <div class="attached-file-elem">
                        <input type="file" id="file-upload" name="file-upload" multiple="multiple">
                        <div>
                            <span style="font-size: small">※ 첨부파일 당 최대 5MB까지 업로드 가능하며, 최대5개까지 등록할 수 있습니다.</span>
                            <span style="font-size: small">※ 수정화면에서 파일첨부 후, 저장 시 기존 첨부파일은 새로운 첨부파일으로 대체됩니다.</span>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td></td>
                <td colspan="3">
                    <div>
                        <c:forEach var="item" items="${file}">
                            <div class="attached-file">
                                <h5>첨부파일 : </h5><span>${item.fileNm}</span>
                                <button class="file-del-btn btn btn-danger">삭제</button>
                            </div>
                        </c:forEach>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div class="write-bottom">
        <div>
            <button class="cancel btn btn-secondary">취소</button>
            <button class="save-btn btn btn-primary">저장</button>
        </div>
    </div>
</div>

<script>
    $('#summernote').summernote({
        tabsize: 5,
        height: 500
    });
</script>
<script src="/js/write.js"></script>
</body>
</html>
