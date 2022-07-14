let toListBtn = document.querySelector(".toList");
let prevBtn = document.querySelector(".prev");
let nextBtn = document.querySelector(".next");
let userChkElem = document.querySelector(".user-chk");

let hiddenBtns = document.querySelectorAll(".hidden");

let modBtn = document.querySelector(".mod");
let delBtn = document.querySelector(".del");

toListBtn.addEventListener("click", ()=> {
    location.href = 'http://localhost:9000/board/list'
})

//로그인 아이디와 게시글 아이디가 같을 때 버튼 노출
if (userChkElem.textContent === 'same') {
    hiddenBtns.forEach((item)=> {
        item.classList.remove("hidden");
    })
}

//url 파라미터 받기
function getParameterByName(name) {
    const url = new URL(location.href);
    const params = url.searchParams;
    const results = params.get(name);
    return results;
}

//로그인 아이디와 게시글 아이디가 같을 때 게시글 삭제
delBtn.addEventListener("click", ()=> {
    let iboard = getParameterByName("iboard");

    let delChk = confirm("게시글을 삭제 하시겠습니까?");

    if (delChk === false) {
        return;
    } else {
        // 작성자가 아닌 유저가 get방식으로 직접 삭제 시도할 때 자바에서 감지 후 삭제하지 않고 board/list로 보냄
        fetch(`http://localhost:9000/board/delete?iboard=` + `${iboard}`)
            .then(data => {
                console.log(data);
                location.href="http://localhost:9000/board/list";
            }).catch(e=> {
            console.error(e);
        })
    }
})

//수정 페이지 이동
modBtn.addEventListener("click", ()=> {
    let iboard = getParameterByName("iboard");
    location.href="http://localhost:9000/board/mod?iboard=" + `${iboard}`;
})

//이전 글, 다음 글 버튼
prevBtn.addEventListener("click", ()=> {
if (document.querySelector(".prevIboard").textContent === 'first') {
    return alert("처음 글 입니다.");
} else {
    let prevIboard = document.querySelector(".prevIboard").textContent;
    location.href = "http://localhost:9000/board/detail?iboard=" + prevIboard;
}
})

nextBtn.addEventListener("click", ()=> {
    if (document.querySelector(".nextIboard").textContent === 'last') {
        return alert("마지막 글 입니다.");
    } else {
        let nextIboard = document.querySelector(".nextIboard").textContent;
        location.href = "http://localhost:9000/board/detail?iboard=" + nextIboard;
    }
})

//파일 다운로드

let fileNmElem = document.querySelectorAll(".attached-file > span")
let fileNmList = [];

fileNmElem.forEach((item)=> {

    let fileNm = item.textContent.replaceAll("\\", "/");
    fileNmList.push(fileNm);

    item.addEventListener("click", (e)=> {
        e.preventDefault();

        location.href = "http://localhost:9000/board/download?fileName="+ fileNm;
    })

    //첨부파일 이름에서 경로를 없앰
    item.textContent = item.textContent.substring(item.textContent.lastIndexOf("\\")+1);
})

//댓글 달기

let replyCtntElem = document.querySelector(".reply-ctnt");
let replyBtnElem = document.querySelector(".reply-btn");
let loginUserPk = document.querySelector(".loginUserPk").textContent;

replyBtnElem.addEventListener("click", ()=> {

    if (replyCtntElem.value.length > 200) {
        alert("댓글은 200자 이내로 작성해 주세요.");
        return;
    }

    let data = {
        "iboard" : parseInt(getParameterByName("iboard")),
        "ctnt" : replyCtntElem.value,
        "iuser" : loginUserPk
    }
    console.log(data);

    fetch("/board/reply", {
        method : 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body : JSON.stringify(data)
    }).then(res => {
        console.log(res);
        return res.json();
        }
    ).then(data => {
        console.log(data);
        alert("댓글달기 성공");
        window.location.reload();
    }).catch(e => {
        console.error(e);
    })
})

//댓글 삭제
let replyDelBtnElem = document.querySelectorAll(".reply-del-btn");

replyDelBtnElem.forEach((item)=> {
    item.addEventListener("click", ()=> {
        let ireplyElem = item.parentElement.parentElement;
        let ireply = item.parentElement.parentElement.dataset.set;
        let iboard = getParameterByName("iboard");

        let delChk = confirm("댓글을 삭제하시겠습니까?");

        if (delChk === false) {
            return;
        } else {
            fetch("/board/reply/delete", {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    'iboard': iboard,
                    'ireply': ireply
                })
            }).then(res => {
                console.log(res);
                return res.json();
            }).then(data => {
                console.log(data);
                ireplyElem.remove();
            }).catch(e=> {
                console.error(e);
            })
        }
    })
})

//댓글 삭제 버튼

// let replyBodyElem = document.querySelectorAll(".table-body");
//
// replyBodyElem.forEach((item)=> {
//     if (userChkElem)
// })