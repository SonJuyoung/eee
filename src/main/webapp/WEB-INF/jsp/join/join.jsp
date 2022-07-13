<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Join</title>
    <link rel="stylesheet" href="/css/user/join.css">
    <!-- Latest compiled and minified CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

</head>
<body>
<div class="container">
<%--<form method="post" action="/user/join" id="joinForm">--%>
    <div class="join-top">
        <h1>회원가입</h1>
        <h5>정확한 정보를 입력해주십시오. 타 정보 무단 도용 또는 허위 정보 입력 시 회원가입 승인이 되지 않으며 불이익을 받으실 수 있습니다.</h5>
    </div>
    <div class="join-body">
        <div class="join-info">
            <div>
                <h5 style="font-weight: bold">회원정보</h5>
                <h5 style="font-size: small" >(*) 필수입력항목</h5>
            </div>
            <h5>아이디는 영문자로 시작하는 영문자 또는 숫자 6~20자 / 비밀번호는 8 ~ 20자 영문, 숫자, 특수문자를 최소 한가지씩 조합하세요.</h5>
        </div>
        <div class="join-input">
            <div>
                <div class="join-detail join-detail-top">
                    <label for="id">(*)아이디</label><input class="form-control" type="text" id="id" name="id">
                    <button class="idchk btn btn-light">중복확인</button>
                </div>
                <div class="join-detail">
                    <label for="pw">(*)비밀번호</label><input class="form-control" type="password" id="pw" name="pw">
                </div>
                <div class="join-detail">
                    <label for="id">(*)휴대폰번호</label><input class="form-control" type="text" id="phone" name="phone">
                </div>
            </div>
            <div>
                <div class="join-detail join-detail-top">
                    <label for="id">(*)이름</label><input class="form-control" type="text" id="name" name="name">
                </div>
                <div class="join-detail">
                    <label for="pw">(*)비밀번호 확인</label><input class="form-control" type="password" id="pwchk" name="pwchk">
                </div>
                <div class="join-detail">
                    <label for="id">(*)이메일 주소</label><input class="form-control" type="email" id="mail" name="mail">
                </div>
            </div>
        </div>

    </div>
    <div class="join-bottom">
        <button type="submit" class="btn btn-primary">가입</button>
    </div>
</div>
<script src="/js/join.js"></script>
</body>
</html>
