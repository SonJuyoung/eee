<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MyInfo</title>
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
        <h1>회원정보</h1>
        <h5>비밀번호를 변경할 수 있습니다.</h5>
    </div>
    <div class="join-body">
        <div class="join-info">
            <div>
                <h5 style="font-weight: bold">회원정보</h5>
            </div>
        </div>
        <div class="join-input">
            <div>
                <div class="join-detail join-detail-top">
                    <label for="id">(*)아이디</label><input class="form-control" type="text" id="id" name="id" value="${loginUser.uid}" data-set="${loginUser.iuser}" readonly>
                </div>
                <div class="join-detail">
                    <label for="pw">(*)비밀번호</label><input class="form-control" type="password" id="pw" name="pw">
                </div>
                <div class="join-detail">
                    <label for="id">(*)휴대폰번호</label><input class="form-control" type="text" id="phone" name="phone" value="${loginUser.phone}" readonly>
                </div>
            </div>
            <div>
                <div class="join-detail join-detail-top">
                    <label for="id">(*)이름</label><input class="form-control" type="text" id="name" name="name" value="${loginUser.name}" readonly>
                </div>
                <div class="join-detail">
                    <label for="pw">(*)비밀번호 확인</label><input class="form-control" type="password" id="pwchk" name="pwchk">
                </div>
                <div class="join-detail">
                    <label for="id">(*)이메일 주소</label><input class="form-control" type="email" id="mail" name="mail" value="${loginUser.mail}" readonly>
                </div>
            </div>
        </div>

    </div>
    <div class="join-bottom">
        <button type="submit" class="btn btn-primary">정보 변경</button>
    </div>
</div>
<script src="/js/join.js"></script>
</body>
</html>
