<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MyPage</title>
    <link rel="stylesheet" href="/css/user/myPage.css">
    <!-- Latest compiled and minified CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</head>
<body>
<div class="container">
    <div class="mypage-top d-flex justify-content-center">
        <h1>마이페이지</h1>
    </div>
    <div class="mypage-info d-flex flex-column align-items-center mb-2">
        <h5 data-set="${loginUser.iuser}"><strong>${loginUser.name}</strong></h5>
        <h5>${loginUser.mail}</h5>
        <div class="my-photo">
            <input type="file" class="user-img" accept="image/*" style="display: none">
            <img src="${profileImg}" alt="user-image" class="profile-img rounded-circle"
                 onerror="this.src='/images/default-img.png'">
        </div>
    </div>
    <div class="mypge-btns">
        <div class="mb-1">
            <a href="/myinfo" style="text-decoration: none"><i class="fas fa-user-circle">내 정보</i></a>
        </div>
        <div class="mb-1">
            <a href="/myarticle"><i class="fas fa-edit">작성한 글</i></a>
        </div>
        <i class="fas fa-envelope-open">내 쪽지함</i>
    </div>
</div>

<script src="/js/myPage.js"></script>
</body>
</html>
