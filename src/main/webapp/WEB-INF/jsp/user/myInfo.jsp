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
        <h5>비밀번호 또는 주소를 변경할 수 있습니다.</h5>
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
        <div class="address container">
            <div class="d-flex flex-column align-items-center">
                <div class="d-flex align-items-center justify-content-center w-50 mb-1">
                    <input type="text" class="form-control postcode" id="sample6_postcode" value="${loginUser.postcode}" placeholder="우편번호">
                    <input type="button" class="btn btn-secondary" onclick="sample6_execDaumPostcode()" value="우편번호 찾기"><br>
                </div>
                <div class="d-flex align-items-center w-50 mb-1">
                    <input type="text" class="form-control w-100 main-address" id="sample6_address" value="${loginUser.address}" placeholder="주소" readonly><br>
                </div>
                <div class="d-flex align-items-center justify-content-center w-50">
                    <input type="text" class="form-control detail-address" id="sample6_detailAddress" placeholder="상세주소">
                    <input type="text" class="form-control extra-address" id="sample6_extraAddress" placeholder="참고항목">
                </div>
            </div>
        </div>
    <div class="join-bottom">
        <button type="submit" class="btn btn-primary">정보 변경</button>
    </div>
</div>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    function sample6_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    document.getElementById("sample6_extraAddress").value = extraAddr;

                } else {
                    document.getElementById("sample6_extraAddress").value = '';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('sample6_postcode').value = data.zonecode;
                document.getElementById("sample6_address").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("sample6_detailAddress").focus();
            }
        }).open();
    }
</script>
<script src="/js/join.js"></script>
</body>
</html>
