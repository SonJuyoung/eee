let id = document.querySelector("#id");
let nm = document.querySelector("#name");
let pw = document.querySelector("#pw");
let pwchk = document.querySelector("#pwchk");
let phone = document.querySelector("#phone");
let mail = document.querySelector("#mail");
let postcode = document.querySelector(".postcode");
let addressElem = document.querySelector(".main-address");
let detailAddress = document.querySelector(".detail-address");
let extraAddress = document.querySelector(".extra-address");

let idchk = document.querySelector(".idchk");

let joinBtn = document.querySelector(".join-bottom > button");
const url = new URL(location.href).href;
//회원가입 validation
joinBtn.addEventListener("click", (e)=> {

    let postcodeVal = postcode.value;
    let addressVal = addressElem.value;
    let detailAddressVal = detailAddress.value;
    let extraAddressVal = extraAddress.value;

    if (url.includes("myinfo")) {
        let iuser = document.querySelector("#id").dataset.set;

        //비밀번호 유효성 검사
        if (!isPassword(pw.value)) {
            alert("비밀번호는 8 ~ 30자 영문, 숫자, 특수문자를 최소 한가지씩 조합해야 합니다.");
            return;
        }
        if (pw.value !== pwchk.value) {
            alert("비밀번호를 확인해 주세요.");
            return;
        }

        if (addressVal === '') {
            alert("주소를 입력해주세요.");
            return;
        }

        fetch("http://localhost:9000/join", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                "iuser": iuser,
                "id": id.value,
                "name": nm.value,
                "pw": pw.value,
                "mail": mail.value,
                "phone": phone.value,
                "postcode": postcodeVal,
                "address" : addressVal,
                "addressDetail" : detailAddressVal,
                "addressExtra" : extraAddressVal
            }),
        }).then(res => {
            console.log(res);
            return res.json();
        }).then(data => {
            console.log(data);
            if (data === 1) {
                alert("비밀번호 변경 성공!");
                location.href="/board/list";
            }
        }).catch(e=> {
            console.log(e);
            alert("비밀번호 변경을 실패했습니다. 비밀번호를 확인 후, 다시 시도해 주세요.");
        });
    } else { //회원가입
        e.preventDefault();

        if (!joinBtn.classList.contains("pass")) {
            alert("중복확인을 해 주세요.");
            return;
        }

        if (pw.value !== pwchk.value) {
            alert("비밀번호를 확인해 주세요.");
            return;
        }

        //아이디 유효성 검사
        if (!isId(id.value)) {
            alert("아이디는 영문자로 시작하는 영문자 또는 숫자 6~20자 입니다.");
            return;
        }

        //비밀번호 유효성 검사
        if (!isPassword(pw.value)) {
            alert("비밀번호는 8 ~ 30자 영문, 숫자, 특수문자를 최소 한가지씩 조합해야 합니다.");
            return;
        }

        if (addressVal === '') {
            alert("주소를 입력해주세요.");
            return;
        }

        fetch("http://localhost:9000/join", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                "id": id.value,
                "name": nm.value,
                "pw": pw.value,
                "mail": mail.value,
                "phone": phone.value,
                "postcode": postcodeVal,
                "address" : addressVal,
                "addressDetail" : detailAddressVal,
                "addressExtra" : extraAddressVal
            }),
        }).then(res => {
            console.log(res);
            return res.json();
        }).then(data => {
            console.log(data);
            if (data === 1) {
                alert("회원가입 성공!");
                location.href="/login";
            }
        }).catch(e=> {
            console.log(e);
            alert("회원가입에 실패했습니다. 휴대폰번호 혹은 메일을 확인 후, 다시 시도해 주세요.");
        });
    }
})


//아이디 중복 검사
if (url.includes("join")) {
    idchk.addEventListener("click", ()=> {

        if (id.value === '') {
            alert("아이디를 입력해 주세요");
            return;
        }

        fetch("http://localhost:9000/join/idchk", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: id.value
        }).then(res => {
            console.log(res);
            return res.json();
        }).then(data => {
            console.log(data);
            if (data === 1) { //아이디 중복일 때
                alert("이미 존재하는 아이디입니다.");
                id.value = "";
                joinBtn.classList.remove("pass"); //아이디 중복일 때 클래스 삭제
                return;
            } else { //아이디 중복 x
                if (isId(id.value)) { //아이디 validation
                    alert("사용가능한 아이디입니다.");
                    joinBtn.classList.add("pass"); //사용가능한 아이디일 때 클래스 부여
                } else { //중복x but 유효성 통과x
                    alert("아이디는 영문자로 시작하는 영문자 또는 숫자 6~20자 입니다.");
                    return;
                }
            }
        })
            .catch(e=> console.log(e));
    })
}


//아이디, 비밀번호 유효성 검사 함수

function isId(asValue) {
    let regExp = /^[a-z]+[a-z0-9]{5,19}$/g; // 영문자로 시작하는 영문자 또는 숫자 6~20자

    return regExp.test(asValue); // 형식에 맞는 경우 true 리턴
}

function isPassword(asValue) {
    let regExp = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,30}$/; // 8 ~ 30자 영문, 숫자, 특수문자를 최소 한가지씩 조합

    return regExp.test(asValue); // 형식에 맞는 경우 true 리턴
}