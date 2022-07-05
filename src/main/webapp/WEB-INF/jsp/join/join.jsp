<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Join</title>
    <link rel="stylesheet" href="/css/user/join.css">
</head>
<body>
<form method="post" action="/user/join" id="joinForm">
    <div class="join-top">
        <h1>회원가입</h1>
        <h5>정확한 정보를 입력해주십시오. 타 정보 무단 도용 또는 허위 정보 입력 시 회원가입 승인이 되지 않으며 불이익을 받으실 수 있습니다.</h5>
    </div>
    <div class="join-body">
        <div class="join-info">
            <h5>회원정보</h5>
            <h5>(*) 필수입력항목</h5>
        </div>
        <div class="join-input">
            <div>
                <div class="join-detail join-detail-top">
                    <label for="id">(*)아이디</label><input type="text" id="id" name="id">
                    <button class="idchk">중복확인</button>
                </div>
                <div class="join-detail">
                    <label for="pw">(*)비밀번호</label><input type="password" id="pw" name="pw">
                </div>
                <div class="join-detail">
                    <label for="id">(*)휴대폰번호</label><input type="text" id="phone" name="phone">
                </div>
            </div>
            <div>
                <div class="join-detail join-detail-top">
                    <label for="id">(*)이름</label><input type="text" id="name" name="name">
                </div>
                <div class="join-detail">
                    <label for="pw">(*)비밀번호 확인</label><input type="password" id="pwchk" name="pwchk">
                </div>
                <div class="join-detail">
                    <label for="id">(*)이메일 주소</label><input type="email" id="mail" name="mail">
                </div>
            </div>
        </div>

    </div>
    <div class="join-bottom">
        <button type="submit">가입</button>
    </div>
</form>
<script src="/js/join.js"></script>
</body>
</html>
