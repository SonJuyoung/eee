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

    if (pw.value !== pwchk.value) {
        alert("비밀번호를 확인해 주세요.");
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
            return;
        } else {
            alert("사용가능한 아이디입니다.");
        }
    })
        .catch(e=> console.log(e));
})