<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="/css/user/login.css">
    <!-- Latest compiled and minified CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container">
    <div class="login-top">
        <h1>로그인</h1>
        <h5>시스템 이용을 위해 로그인 하시기 바랍니다.</h5>
    </div>
    <div class="login-body">
        <div class="login-input">
            <div>
                <label for="id">아이디</label>
                <input class="form-control" type="text" id="id" name="id" placeholder="아이디를 입력해 주세요.">
            </div>
            <div>
                <label for="pw">비밀번호</label>
                <input class="form-control" type="password" id="pw" name="pw" placeholder="비밀번호를 입력해 주세요.">
            </div>

        </div>
    </div>
    <div class="id-save">
        <input type="checkbox" id="saveIdChk" name="">
        <label for="saveIdChk">아이디 기억하기</label>
    </div>
    <div class="login-btn">
        <button class="btn btn-primary" id="login-btn" type="button" name="btn">로그인</button>
    </div>
    <div class="login-bottom">
        <div>
            <div class="login-bottom-btn">
                <h5>회원가입 후 이용이 가능합니다.</h5><a href="/join"><button class="btn btn-info">회원가입</button></a>
            </div>
            <div class="login-bottom-btn">
            <h5>시스템 이용 문의사항 (T) 053-000-0000</h5> <button class="btn btn-warning">아이디/비밀번호 찾기</button>
            </div>
        </div>
    </div>
</div>
<script src="/js/login.js"></script>
</body>
</html>
