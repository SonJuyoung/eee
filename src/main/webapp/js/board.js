//상태 공지사항 or 일반 구분
let trElem = document.querySelectorAll("tr");

trElem.forEach((item) => {
    if (item.childNodes[1].textContent === '1') {
        item.childNodes[1].textContent = '공지사항'
    } else if (item.childNodes[1].textContent === '0') {
        item.childNodes[1].textContent = ''
    }
})

//검색
let searchOptionElem = document.querySelector(".search-option");
let searchTxtElem = document.querySelector(".search-text");
let searchBtnElem = document.querySelector(".search-btn");

//검색버튼 눌렀을 때 get방식으로 주소 이동
searchBtnElem.addEventListener("click", () => {
    let searchOptionVal = searchOptionElem.value;
    let searchTxt = searchTxtElem.value;

    location.href = "http://localhost:9000/board/list/search?" + `category=${searchOptionVal}` + '&' + `searchTxt=${searchTxt}`;
})

//검색했을 때 카테고리와 검색 텍스트 유지

if (getParameterByName('category')) {
    let searchOptionVal = searchOptionElem.value;
    let searchTxt = searchTxtElem.value;

    searchOptionVal = getParameterByName("category");
    searchTxt = getParameterByName("searchTxt");

    console.log(searchOptionVal);
    console.log(searchTxt)

    searchOptionElem.value = searchOptionVal;
    searchTxtElem.value = searchTxt;
}

//페이징

let pagingElem = document.querySelector(".pagination");
let totalCount = parseInt(document.querySelector(".count > h5").textContent.split("| ")[1]);

let defaultNum = 10; //한 페이지 공지사항 제외 글 수
let fixNum = document.querySelector(".fix-count").textContent;

let pagingNum = Math.ceil((totalCount - fixNum) / defaultNum);
//현재 페이지
let currnetPage = 1;

function makePagingNum() {

    //현재 페이지 설정
    if (getParameterByName("page")) { //파라미터로 page가 있는 경우
        currnetPage = parseInt(getParameterByName("page")) + 1; //스프링 pageable의 page가 0부터 시작하기 때문
    }

    pagingElem.innerHTML = '';

    if (pagingNum > 10) { //페이징넘버가 10을 초과하는 경우

        //현재 페이지가 defaultNum와 나누어 떨어질 때 페이징 정상적으로 처리하기 위해서
        //(나누어 떨어지면 아래 페이징 만드는 for문에서 0이 나와버려서 정상 작동 안됨)
        if (currnetPage % defaultNum === 0) {
            currnetPage = currnetPage - 0.1
        }
        //페이징 넘버가 10초과 했을 경우 화살표 생김
        pagingElem.innerHTML += `
    <li class="prev page-item"><a class="page-link"><</a></li>
        `
        //현재 페이지에 따라서 페이징넘버 다시 설정
        //(현재 페이지 4 -> 페이징넘버 1~10, 현재페이지 12 -> 페이징넘버 11~20)
        //(만약에 페이지 총 갯수가 15이면서 현재페이지가 12라면 페이징넘버 11~15)
        for (let i = Math.ceil((Math.floor(currnetPage / defaultNum)) * defaultNum) + 1; i <= ((Math.ceil(currnetPage / defaultNum)) * defaultNum < pagingNum ? (Math.ceil(currnetPage / defaultNum)) * defaultNum : pagingNum); i++) {
            pagingElem.innerHTML += `
    <li class="pageNum page-item"><a class="page-link">${i}</a></li>

    `
        }
        pagingElem.innerHTML += `
    <li class="next page-item"><a class="page-link">></a></li>
        `

        let prevBtn = document.querySelector(".prev");
        let nextBtn = document.querySelector(".next");

        // < 버튼 눌렀을 때 이전 페이징넘버의 최초 페이지로 이동
        // 현재 페이지 25일때, < 누르면 11페이지로 이동, 현재 페이지 7일때 1페이지로 이동
        prevBtn.addEventListener("click", () => {
            const url = new URL(location.href).href;

            let prevPage = Math.floor(currnetPage / defaultNum) - 1;

            if (url.includes("page")) {
                let url1 = url.split("page")[0];
                location.href = `${url1}` + `page=${prevPage}`
            } else if (url.includes("?")) {
                location.href = `${url}&` + `page=${prevPage}`
            } else {
                location.href = `${url}?` + `page=${prevPage}`
            }
        })

        // > 버튼 눌렀을 때 이후 페이징넘버의 최초 페이지로 이동
        // 현재 페이지 1일때, > 누르면 11페이지로 이동, 현재 페이지 11이고 총 15페이지 까지 있을 때, 15로 이동
        nextBtn.addEventListener("click", () => {
            const url = new URL(location.href).href;

            let nextPage = (Math.ceil(currnetPage / defaultNum)) * defaultNum < pagingNum ? ((Math.ceil(currnetPage / defaultNum)) * defaultNum) : pagingNum - 1;

            if (url.includes("page")) {
                let url1 = url.split("page")[0];
                location.href = `${url1}` + `page=${nextPage}`
            } else if (url.includes("?")) {
                location.href = `${url}&` + `page=${nextPage}`
            } else {
                location.href = `${url}?` + `page=${nextPage}`
            }
        })

    } else { // 총 페이지가 10 이하 일때 화살표 없이 번호만 생성
        for (let i = 1; i <= pagingNum; i++) {
            pagingElem.innerHTML += `
    <li class="pageNum page-item"><a class="page-link">${i}</a></li>
    `
        }
    }

    let pageNum = document.querySelectorAll(".pageNum");

    pageNum.forEach((item) => {
        const url = new URL(location.href).href;

        //현재 페이지 페이징넘버 강조
        if (item.textContent - 1 == getParameterByName("page")) { //url 파라미터 감지해서 해당 페이징넘버에 클래스 추가
            item.classList.add("active")
        } else if (currnetPage === 1) { // page 파라미터가 없는 최초 페이지(1페이지)일때 페이징넘버 1에 클래스 추가
            pageNum[0].classList.add("active")
        }
        item.addEventListener("click", () => { // 페이징넘버 클릭시 get방식으로 주소 이동
            if (url.includes("page")) {
                let url1 = url.split("page")[0];
                location.href = `${url1}` + `page=${item.textContent - 1}`
            } else if (url.includes("?")) {
                location.href = `${url}&` + `page=${item.textContent - 1}`
            } else {
                location.href = `${url}?` + `page=${item.textContent - 1}`
            }
        })
    })
}

makePagingNum();

//url 파라미터 받기
function getParameterByName(name) {
    const url = new URL(location.href);
    const params = url.searchParams;
    const results = params.get(name);
    return results;
}

//검색했을 때 페이징
const url = new URL(location.href);

if (getParameterByName("category")) {

    makePagingNum();
}

//로그아웃

let logoutBtn = document.querySelector(".logout-btn");

logoutBtn.addEventListener("click", () => {
    location.href = "http://localhost:9000/logout";
})

//클릭 시 디테일 이동

let titleElem = document.querySelectorAll(".table-body");

titleElem.forEach((item) => {

    let iboard = item.childNodes[3].textContent;

    item.childNodes[7].addEventListener("click", () => {
        location.href = "http://localhost:9000/board/detail?iboard=" + `${iboard}`;
    })
})

//프로필 사진 클릭 시 마이페이지 이동

let profileImg = document.querySelector(".profile-img");

profileImg.addEventListener("click", ()=> {
    location.href = "/mypage";
})