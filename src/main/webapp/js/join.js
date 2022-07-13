let id = document.querySelector("#id");
let nm = document.querySelector("#name");
let pw = document.querySelector("#pw");
let pwchk = document.querySelector("#pwchk");
let phone = document.querySelector("#phone");
let mail = document.querySelector("#mail");

let idchk = document.querySelector(".idchk");

let joinBtn = document.querySelector(".join-bottom > button");

joinBtn.addEventListener("click", (e)=> {

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
        alert("비밀번호는 8 ~ 20자 영문, 숫자, 특수문자를 최소 한가지씩 조합해야 합니다.");
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
            "phone": phone.value
        }),
    }).then(res => {
        console.log(res)
    }).then(data => {
        console.log(data);
        location.href="/login"
    }).catch(e=> console.log(e));
})

idchk.addEventListener("click", (e)=> {

    e.preventDefault();

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
        if (data === 1) {
            alert("이미 존재하는 아이디입니다.");
            id.value = "";
            joinBtn.classList.remove("pass");
            return;
        } else {
            if (isId(id.value)) {
                alert("사용가능한 아이디입니다.");
                joinBtn.classList.add("pass");
            } else {
                alert("아이디는 영문자로 시작하는 영문자 또는 숫자 6~20자 입니다.");
                return;
            }
        }
    })
        .catch(e=> console.log(e));
})

//아이디, 비밀번호 유효성 검사

function isId(asValue) {
    let regExp = /^[a-z]+[a-z0-9]{5,19}$/g;

    return regExp.test(asValue); // 형식에 맞는 경우 true 리턴
}

function isPassword(asValue) {
    let regExp = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,20}$/;

    return regExp.test(asValue); // 형식에 맞는 경우 true 리턴
}