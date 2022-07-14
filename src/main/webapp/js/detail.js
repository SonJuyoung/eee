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

    confirm("게시글을 삭제 하시겠습니까?");

    // 작성자가 아닌 유저가 get방식으로 직접 삭제 시도할 때 자바에서 감지 후 삭제하지 않고 board/list로 보냄
    fetch(`http://localhost:9000/board/delete?iboard=` + `${iboard}`)
        .then(data => {
            console.log(data);
            location.href="http://localhost:9000/board/list";
        }).catch(e=> {
            console.error(e);
    })
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

