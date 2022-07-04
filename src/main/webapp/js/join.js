let id = document.querySelector("#id");
let nm = document.querySelector("#name");
let pw = document.querySelector("#pw");
let pwchk = document.querySelector("#pwchk");
let phone = document.querySelector("#phone");
let mail = document.querySelector("#mail");

let joinBtn = document.querySelector(".join-bottom > button");

joinBtn.addEventListener("click", (e)=> {
    e.preventDefault();
    if (pw.value !== pwchk) {
        alert("비밀번호를 확인해 주세요.");
        return;
    }
    fetch("http://localhost:9000/api/join", {
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