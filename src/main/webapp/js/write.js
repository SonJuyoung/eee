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
saveBtn.addEventListener("click", (e) => {
    e.preventDefault();

    let iboard = getParameterByName("iboard");

    const formData = new FormData(); // 업로드 할 파일 저장 객체

// input 태그의 id를 이용하여 input의 value 값(업로드 할 파일들)을 가져와서 저장
    let fileUpload = document.querySelector("#file-upload");
    let files = fileUpload.files;
    const maxSize = 5242880; //5MB

    console.log(files);

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

            //첨부파일 있을 때 파일 추가
            if (files.length > 0) {
                //파일 용량 제한
                function checkExtension(fileSize) {
                    if (fileSize > maxSize) {
                        alert("파일 사이즈 초과");
                        return false;
                    }
                    return true;
                }

// formData에 파일 데이터 저장
                for (let i = 0; i < files.length; i++) {

                    if (!checkExtension(files[i].size)) {
                        return false;
                    }

                    formData.append("uploadFile", files[i]);
                    console.log(fileUpload.files[i]);
                }

                fetch("http://localhost:9000/board/uploadAjaxAction", {
                    method: 'POST',
                    processData: false,
                    body: formData
                }).then(res => {
                    return res.json();
                })
                    .then(data => {

                        console.log(data)

                        data.forEach((item)=> {
                            console.log(iboard);
                            console.log(item.fileNm);
                            console.log(item.uploadPath)
                            console.log(item.uuid)
                            fetch("http://localhost:9000/board/fileSave", {
                                method: 'POST',
                                headers: {
                                    "Content-Type": "application/json",
                                },
                                body: JSON.stringify({
                                    "iboard": iboard,
                                    "fileNm": item.fileNm,
                                    "uploadPath" : item.uploadPath,
                                    "uuid" : item.uuid
                                })
                            }).then(res => {
                                return res.json();
                            })
                                .then(data => console.log(data))
                                .catch(e => console.error(e))
                        })

                    })
                    .catch(e => console.error(e))
            }

            location.href = "http://localhost:9000/board/list";
        }).catch(e => console.log(e));
        //등록일 때
    } else {

        let iboard;

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
            iboard = data;
            console.log("등록하는 게시물 iboard : " + iboard);
//첨부파일 있을 때 파일 추가
            if (files.length > 0) {
                //파일 용량 제한
                function checkExtension(fileSize) {
                    if (fileSize > maxSize) {
                        alert("파일 사이즈 초과");
                        return false;
                    }
                    return true;
                }

// formData에 파일 데이터 저장
                for (let i = 0; i < files.length; i++) {

                    if (!checkExtension(files[i].size)) {
                        return false;
                    }

                    formData.append("uploadFile", files[i]);
                    console.log(fileUpload.files[i]);
                }

                fetch("http://localhost:9000/board/uploadAjaxAction", {
                    method: 'POST',
                    processData: false,
                    body: formData
                }).then(res => {
                    return res.json();
                })
                    .then(data => {

                        console.log(data)

                        data.forEach((item)=> {
                            console.log(iboard);
                            console.log(item.fileNm);
                            console.log(item.uploadPath)
                            console.log(item.uuid)
                            fetch("http://localhost:9000/board/fileSave", {
                                method: 'POST',
                                headers: {
                                    "Content-Type": "application/json",
                                },
                                body: JSON.stringify({
                                    "iboard": iboard,
                                    "fileNm": item.fileNm,
                                    "uploadPath" : item.uploadPath,
                                    "uuid" : item.uuid
                                })
                            }).then(res => {
                                return res.json();
                            })
                                .then(data => console.log(data))
                                .catch(e => console.error(e))
                        })

                    })
                    .catch(e => console.error(e))
            }

            location.href = "http://localhost:9000/board/list";
        }).catch(e => console.log(e));
    }
})

//취소
let cancelBtn = document.querySelector(".cancel");

cancelBtn.addEventListener("click", () => {
    location.href = "http://localhost:9000/board/list";
})

//파일 업로드
let testBtn = document.querySelector(".test");

testBtn.addEventListener("click", () => {
//     const formData = new FormData(); // 업로드 할 파일 저장 객체
//
// // input 태그의 id를 이용하여 input의 value 값(업로드 할 파일들)을 가져와서 저장
//     let fileUpload = document.querySelector("#file-upload");
//     let files = fileUpload.files;
//     console.log(files);
//
//     const maxSize = 5242880; //5MB
//
//     function checkExtension(fileSize){
//         if(fileSize > maxSize){
//             alert("파일 사이즈 초과");
//             return false;
//         }
//         return true;
//     }
//
// // formData에 파일 데이터 저장
//     for (let i=0; i<files.length; i++) {
//
//         if(!checkExtension(files[i].size)){
//             return false;
//         }
//
//         formData.append("uploadFile", files[i]);
//         console.log(fileUpload.files[i]);
//     }
//
//     fetch("http://localhost:9000/board/uploadAjaxAction", {
//         method: 'POST',
//         processData : false,
//         body: formData
//     }).then(res => {
//         return res.json();
//     })
//         .then(data => console.log(data))
//         .catch(e => console.error(e))

})

