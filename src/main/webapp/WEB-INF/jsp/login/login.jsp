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
    <form method="post" action="/login">
    <div class="login-body">
            <div class="login-input">
                <div>
                    <label for="id">아이디</label>
                    <input class="form-control" type="text" id="id" name="username" placeholder="아이디를 입력해 주세요.">
                </div>
                <div>
                    <label for="pw">비밀번호</label>
                    <input class="form-control" type="password" id="pw" name="password" placeholder="비밀번호를 입력해 주세요.">
                </div>
            </div>
    </div>
    <div class="id-save">
        <input type="checkbox" id="saveIdChk" name="">
        <label for="saveIdChk">아이디 기억하기</label>
    </div>
    <div class="login-btn">
        <input class="btn btn-primary" id="login-btn" type="submit" name="btn" value="로그인">
    </div>
    </form>
    <div class="login-bottom">
        <div>
            <div class="login-bottom-btn">
                <h5>회원가입 후 이용이 가능합니다.</h5><a href="/join">
                <button class="btn btn-info">회원가입</button>
            </a>
            </div>
            <div class="login-bottom-btn">
                <h5>시스템 이용 문의사항 (T) 053-000-0000</h5>
                <button class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#myModal">아이디/비밀번호 찾기</button>
            </div>
        </div>
    </div>

    <!-- The Modal -->
    <div class="modal" id="myModal" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">아이디 찾기</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body">
                    이름<input type="text" class="findByName">
                    휴대폰 번호<input type="text" class="findByPhone">
                    메일<input type="text" class="findByMail">

                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <div>
                        <button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#myModal2">비밀번호 찾기</button>
                    </div>
                    <div>
                        <button type="button" class="btn btn-primary findUserId" data-bs-toggle="modal" data-bs-target="#myModal1">찾기</button>
                        <button type="button" class="btn btn-danger" data-bs-dismiss="modal">닫기</button>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <div class="modal" id="myModal1" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">아이디 찾기 결과</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body result">
                    <h3></h3>
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-bs-dismiss="modal">닫기</button>
                </div>

            </div>
        </div>
    </div>

    <div class="modal" id="myModal2" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">비밀번호 찾기</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body result">
                    아이디<input type="text" class="pw-findById">
                    이름<input type="text" class="pw-findByName">
                    휴대폰 번호<input type="text" class="pw-findByPhone">
                    메일<input type="text" class="pw-findByMail">
                </div>

                <!-- Modal footer -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary findUserPw" data-bs-toggle="modal" data-bs-target="#myModal3">비밀번호 찾기</button>
                    <button type="button" class="btn btn-danger" data-bs-dismiss="modal">닫기</button>
                </div>

            </div>
        </div>
    </div>
    <div class="modal" id="myModal3" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">비밀번호 변경</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- Modal body -->
                <div class="modal-body resultPw">

                </div>

                <!-- Modal footer -->
                <div class="modal-footer pw-set">
                    <button type="button" class="btn btn-danger" data-bs-dismiss="modal">닫기</button>
                </div>

            </div>
        </div>
    </div>
</div>
<script src="/js/login.js"></script>
</body>
</html>
