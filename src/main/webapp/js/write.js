let titleElem = document.querySelector(".title");
let writerElem = document.querySelector(".writer");
let ctntElem = document.querySelector(".ctnt");
let rdtElem = document.querySelector(".rdt");


let saveBtn = document.querySelector(".save-btn");

rdtElem.value = new Date().toISOString().substring(0, 10);

//url 파라미터 받기
function getParameterByName(name) {
    const url = new URL(location.href);
    const params = url.searchParams;
    const results = params.get(name);
    return results;
}

//글쓰기 && 수정 ajax
saveBtn.addEventListener("click", (e)=> {
    e.preventDefault();

    let formData = new FormData(); // 업로드 할 파일 저장 객체

// input 태그의 id를 이용하여 input의 value 값(업로드 할 파일들)을 가져와서 저장
    let fileUpLoad = document.querySelectorAll("#file-upload")[0];

// formData에 파일 데이터 저장
    for (let i=0; i<fileUpLoad.files.length; i++) {
        formData.append("upLoadFile", fileUpLoad.files[i]);
        console.log(fileUpLoad.files[i]);
    }

    let files = [];

    formData.forEach()

    let iboard = getParameterByName("iboard");
    let fixElem = document.querySelector("input[name=fix]:checked");
    const url = new URL(location.href).href;

console.log(fixElem.value);
//수정일 때
if (url.includes("mod")) {
    fetch("http://localhost:9000/board/mod", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "iboard": iboard,
            "title": titleElem.value,
            "writer": writerElem.value,
            "ctnt": ctntElem.value,
            "rdt": rdtElem.value,
            "fix": fixElem.value
        }),
    }).then(res => {
        console.log(res);
        return res.json();
    }).then(data => {
        console.log(data)
        location.href="http://localhost:9000/board/list";
    }).catch(e=> console.log(e));
    //등록일 때
} else {
    fetch("http://localhost:9000/board/write", {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            "title": titleElem.value,
            "writer": writerElem.value,
            "ctnt": ctntElem.value,
            "rdt": rdtElem.value,
            "fix": fixElem.value
        }),
    }).then(res => {
        console.log(res);
        return res.json();
    }).then(data => {
        console.log(data)
        location.href="http://localhost:9000/board/list";
    }).catch(e=> console.log(e));
}
})

//취소
let cancelBtn = document.querySelector(".cancel");

cancelBtn.addEventListener("click", ()=> {
    location.href="http://localhost:9000/board/list";
})

//파일 업로드
let testBtn = document.querySelector(".test");

testBtn.addEventListener("click", ()=> {
//     let formData = new FormData(); // 업로드 할 파일 저장 객체
//
// // input 태그의 id를 이용하여 input의 value 값(업로드 할 파일들)을 가져와서 저장
//     let fileUpLoad = document.querySelectorAll("#file-upload")[0];
//
// // formData에 파일 데이터 저장
//     for (let i=0; i<fileUpLoad.files.length; i++) {
//         formData.append("upLoadFile", fileUpLoad.files[i]);
//         console.log(fileUpLoad.files[i]);
//     }
})

