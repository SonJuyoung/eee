let id = document.querySelector("#id");
let pw = document.querySelector("#pw");

let loginBtn = document.querySelector("#login-btn");

loginBtn.addEventListener("click", (e)=> {

    e.preventDefault();

    fetch("http://localhost:9000/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "id": id.value,
            "pw": pw.value
        }),
    }).then(res => {
        console.log(res);
        return res.json();
    }).then(data => {
        console.log(data)
        if (data === 1) {
            location.href = "http://localhost:9000/board/list"
        } else {
            alert("아이디 혹은 비밀번호를 확인해주세요.");
        }
    }).catch(e=> console.log(e));
})

//아이디 기억 쿠키 설정

let loginSave = document.querySelector("#saveIdChk");

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

const chk = () => {
    if(loginSave.checked) {
        setCookie('c_userid', id.value, '100');
    } else {
        setCookie('c_userid', '', '100');
    }
}

loginBtn.addEventListener('click', ()=> {
    chk();
});

let id1 = getCookie('c_userid');
if(id1 === null || typeof id1 === 'undefined' || id1 === ''){
    id1 = '';
} else {
    id.value = id1;
    loginSave.checked = true;
}