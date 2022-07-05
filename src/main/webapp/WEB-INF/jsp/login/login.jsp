<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="/css/user/login.css">
</head>
<body>
<div class="login-top">
    <h1>로그인</h1>
    <h5>시스템 이용을 위해 로그인 하시기 바랍니다.</h5>
</div>
<div class="login-body">
    <div class="login-input">
        <div>
            <label for="id">아이디</label><input type="text" id="id" name="id">
        </div>
        <div>
            <label for="pw">비밀번호</label><input type="password" id="pw" name="password">
        </div>

    </div>
    <div class="login-btn">
        <input id="login-btn" type="button" name="btn" value="로그인">
    </div>
</div>
<div class="id-save">
    <input type="checkbox" id="saveIdChk" name="">
    <label for="saveIdChk">아이디 기억하기</label>
</div>
<div class="login-bottom">
    <div>
        <h5>회원가입 후 이용이 가능합니다.</h5>
        <h5>시스템 이용 문의사항 (T) 053-000-0000</h5>
    </div>
    <div class="btns">
        <a href="/join"><button>회원가입</button></a>
        <button>아이디/비밀번호 찾기</button>
    </div>
</div>
<script src="/js/login.js"></script>
</body>
</html>
