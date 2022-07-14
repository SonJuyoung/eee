let id = document.querySelector("#id");
let pw = document.querySelector("#pw");

let loginBtn = document.querySelector("#login-btn");

let loginSave = document.querySelector("#saveIdChk");

//아이디 기억 쿠키 설정
const setCookie = (cookie_name, value, days) => {
    let exdate = new Date();
    exdate.setDate(exdate.getDate() + days);
    const cookieValue = escape(value) + ((days == null) ? '' : '; expires=' + exdate.toUTCString());
    document.cookie = cookie_name + '=' + cookieValue;
}

const getCookie = cookie_name => {
    let x, y;
    const val = document.cookie.split(';');

    for (let i = 0; i < val.length; i++) {
        x = val[i].substr(0, val[i].indexOf('='));
        y = val[i].substr(val[i].indexOf('=') + 1);
        x = x.replace(/^\s+|\s+$/g, '');
        if (x === cookie_name) {
            return unescape(y);
        }
    }
}

// 아이디 기억 체크되어있는 경우 쿠키 설정
const chk = () => {
    if (loginSave.checked) {
        setCookie('c_userid', id.value, '100');
    } else {
        setCookie('c_userid', '', '100');
    }
}

loginBtn.addEventListener('click', () => {
    chk();
});

let id1 = getCookie('c_userid');
if (id1 === null || typeof id1 === 'undefined' || id1 === '') {
    id1 = '';
} else {
    id.value = id1;
    loginSave.checked = true;
}

//아이디 찾기

let findByName = document.querySelector(".findByName");
let findByPhone = document.querySelector(".findByPhone");
let findByMail = document.querySelector(".findByMail");

let findPwId = document.querySelector(".pw-findById");
let findPwName = document.querySelector(".pw-findByName");
let findPwPhone = document.querySelector(".pw-findByPhone");
let findPwMail = document.querySelector(".pw-findByMail");

let findUserId = document.querySelector(".findUserId");
let findUserPw = document.querySelector(".findUserPw");

findUserId.addEventListener("click", () => {

    console.log(findByName.value);
    console.log(findByPhone.value);
    console.log(findByMail.value);

    if (isNaN(findByPhone.value)) {
        let resultElem = document.querySelector(".result > h3");

        resultElem.innerText = `
            휴대폰 번호를 숫자로 입력해 주세요.
            `
        return;
    }

    fetch("http://localhost:9000/findId", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "name": findByName.value,
            "phone": findByPhone.value,
            "mail": findByMail.value
        }),
    }).then(res => {
        console.log(res);
        return res.json();
    }).then(data => {
        console.log(data);
        let resultElem = document.querySelector(".result > h3");

        resultElem.innerHTML = `
            찾으시는 아이디는 <strong style="color: tomato">${data}</strong> 입니다.
            `

    }).catch(e => {
        console.log(e);

        let resultElem = document.querySelector(".result > h3");

        resultElem.innerHTML = `
            <div>일치하는 아이디가 없습니다.</div> 
            <div>다시 확인해 주세요.</div>
            `
    });
})

//비밀번호 찾기

findUserPw.addEventListener("click", (e) => {
    e.preventDefault();

    console.log(findPwId.value);
    console.log(findPwName.value);
    console.log(findPwPhone.value);
    console.log(findPwMail.value);


    fetch("http://localhost:9000/findPw", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "id": findPwId.value,
            "name": findPwName.value,
            "phone": findPwPhone.value,
            "mail": findPwMail.value
        }),
    }).then(res => {
        console.log(res);
        return res.json();
    }).then(data => {
        console.log(data);
        if (data == 1) {
            let resultElem = document.querySelector(".resultPw");
            let pwSetElem = document.querySelector(".pw-set");
            // resultElem.innerHTML = `
            //     찾으시는 아이디는 <strong style="color: tomato">${data}</strong> 입니다.
            //     `

            pwSetElem.innerHTML = `
        <button type="button" class="btn btn-primary pw-change">비밀번호 변경</button>
        <button type="button" class="btn btn-danger" data-bs-dismiss="modal">닫기</button>
        `

            resultElem.innerHTML = `
<div>변경할 비밀번호</div>
        <input class="form-control" type="password" id="pw-set" name="password" placeholder="변경할 비밀번호를 입력해 주세요.">
        <div>비밀번호 확인</div>
        <input class="form-control" type="password" id="pw-chk" name="pwchk" placeholder="비밀번호를 다시 입력해 주세요.">
        `

            let pwChange = document.querySelector(".pw-change");

            pwChange.addEventListener("click", () => {

                let pwElem = document.querySelector("#pw-set");
                let pwChkElem = document.querySelector("#pw-chk");

                let pwVal = pwElem.value;
                let pwChkVal = pwChkElem.value;
                let idVal = findPwId.value;

                console.log(pwVal);

                if (pwVal === '' || pwChkVal === '') {
                    alert("비밀번호를 빠짐없이 입력해주세요.");
                    return;
                } else if (pwVal !== pwChkVal) {
                    alert("비밀번호를 다시 확인해 주세요.");
                    return;
                }

                fetch("http://localhost:9000/changePw", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        "id": idVal,
                        "pw": pwVal
                    }),
                }).then(res => {
                    console.log(res);
                    return res.json();
                }).then(data => {
                    console.log(data);
                    alert("비밀번호 변경 완료.");
                    location.href = "/login";
                })
            })
        } else {
            let resultPwElem = document.querySelector(".resultPw");

            resultPwElem.innerHTML = `
            <h3>일치하는 정보가 없습니다.</h3>
            `
        }
    })
        .catch(e => console.error(e));
})
