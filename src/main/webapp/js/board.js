//상태 고정 or 일반 구분
let trElem = document.querySelectorAll("tr");

let statusElem;

trElem.forEach((item)=> {
    if (item.childNodes[1].textContent === '1') {
        item.childNodes[1].textContent = '고정'
    } else if (item.childNodes[1].textContent === '0'){
        item.childNodes[1].textContent = ''
    }
})

//검색
let searchOptionElem = document.querySelector(".search-option");
let searchTxtElem = document.querySelector(".search-text");
let searchBtnElem = document.querySelector(".search-btn");

searchBtnElem.addEventListener("click", ()=> {
    let searchOptionVal = searchOptionElem.value;
    let searchTxt = searchTxtElem.value;

    // fetch("http://localhost:9000/board/list/search?"+`category=${searchOptionVal}`+'&' + `searchTxt=${searchTxt}`)
    //     .then(res => {
    //     console.log(res);
    //     return res.json();
    // }).then(data => {
    //     console.log(data)
    //     location.href="http://localhost:9000/board/list/search"+`category=${searchOptionVal}`+'&' + `searchTxt=${searchTxt}`;
    // }).catch(e=> console.log(e));
    location.href="http://localhost:9000/board/list/search?"+`category=${searchOptionVal}`+'&' + `searchTxt=${searchTxt}`;
})

//페이징

let pagingElem = document.querySelector(".pagination");
let totalCount = parseInt(document.querySelector(".count > h5").textContent.split("| ")[1]);

let defaultNum = 10;
let fixNum = document.querySelector(".fix-count").textContent;

let pagingNum = Math.ceil((totalCount-fixNum)/defaultNum);

function makePagingNum() {
    pagingElem.innerHTML = '';
    for (let i=1; i<=pagingNum; i++) {
        pagingElem.innerHTML += `
    <p class="pageNum">${i}</p>
    `
    }

    let pageNum = document.querySelectorAll(".pageNum");

    pageNum.forEach((item)=> {
        const url = new URL(location.href).href;

        if (item.textContent - 1 == getParameterByName("page")) {
            item.classList.add("bold")
        }
    item.addEventListener("click", ()=> {
        if (url.includes("page")) {
                let url1 = url.split("page")[0];
                location.href = `${url1}` + `page=${item.textContent - 1}`
        } else if (url.includes("?")) {
            location.href = `${url}&` + `page=${item.textContent - 1}`
        }
        else {
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

logoutBtn.addEventListener("click", ()=> {
    location.href="http://localhost:9000/logout";
})

//클릭 시 디테일 이동

let titleElem = document.querySelectorAll(".table-body");

titleElem.forEach((item)=> {

    let iboard = item.childNodes[3].textContent;

    item.childNodes[7].addEventListener("click", ()=> {
        location.href = "http://localhost:9000/board/detail?iboard=" + `${iboard}`;
    })
})
