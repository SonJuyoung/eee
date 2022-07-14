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

const url = new URL(location.href).href;

let originDelFileName = [];
let delFileName = [];

let attacedFileElems = document.querySelectorAll(".attached-file > span");

//원래 첨부파일명 배열에 담기
attacedFileElems.forEach((item)=> {
    originDelFileName.push(item.textContent);
})

if (url.includes("mod")) {

    let iboard = getParameterByName("iboard");

    //첨부파일 삭제
    let fileDelBtnElems = document.querySelectorAll(".file-del-btn");

    //수정 페이지에서 파일 삭제 누를 시 화면에서만 첨부파일 없애고 저장 버튼 누를 때 실제 삭제
    // fileDelBtnElems.forEach((item)=> {
    //     item.addEventListener("click", ()=> {
    //         confirm("업로드 된 파일을 삭제하시겠습니까?");
    //         //배열에 삭제할 파일명 저장
    //         delFileName.push(item.previousElementSibling.textContent);
    //         //화면에서 지움
    //         item.parentElement.remove();
    //     })
    // })

    for (let i=0; i<fileDelBtnElems.length; i++) {
        fileDelBtnElems[i].addEventListener("click", ()=> {
            let delChk = confirm("업로드 된 파일을 삭제하시겠습니까?");

            if (delChk === false) {
                return;
            } else {
                delFileName.push(originDelFileName[i]);
                //화면에서 지움
                fileDelBtnElems[i].parentElement.remove();
            }
        })
    }

    attacedFileElems.forEach((item)=> {
        item.textContent = item.textContent.substring(item.textContent.lastIndexOf("\\")+1);
    })
}

//글쓰기 && 수정 ajax
saveBtn.addEventListener("click", (e) => {
    e.preventDefault();

    if (titleElem.value === '') {
        alert("제목을 입력하세요");
        return;
    } else if (ctntElem.value === '') {
        alert("내용을 입력하세요");
        return;
    }

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

            if (files.length > 5) {
                alert("첨부파일은 최대 5개 입니다.");
                return;
            }
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

                    formData.append("uploadFile", files[i], files[i].name + '_' + iboard);
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
                                .then(data => {
                                    console.log(data);
                                })
                                .catch(e => console.error(e))
                        })

                    })
                    .catch(e => console.error(e))
            }
            //실제 첨부파일 삭제 적용
            if (delFileName.length > 0) {
                delFileName.forEach((item)=> {
                    fetch("http://localhost:9000/board/fileDelete", {
                        method : "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body : JSON.stringify({
                            "iboard" : iboard,
                            "fileNm" : item
                        })
                    }).then(res => {
                        console.log(res);
                        return res.json();
                    }).then(data => {
                        console.log(data);
                    }).catch(e=> {
                        console.error(e);
                    })
                })
            }
            alert("수정 완료!");
            location.href = "http://localhost:9000/board/list";
        }).catch(e => console.log(e));

        //등록일 때
    } else {
        //첨부파일 갯수 검사
        if (files.length > 5) {
            alert("첨부파일은 최대 5개 입니다.");
            return;
        }

        //파일 업로드
        //첨부파일 있을 때 파일 검사
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
                    return;
                }

                formData.append("uploadFile", files[i], files[i].name + '_' + 0);
                console.log(fileUpload.files[i]);
            }}

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

            //파일 업로드
            //첨부파일 있을 때 파일 추가
            if (files.length > 0) {

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
                                .then(data => {
                                    console.log(data);
                                })
                                .catch(e => console.error(e))
                        })
                        alert("글 작성 완료!");
                        location.href = "/board/list";
                    })
                    .catch(e => console.error(e))
            } else {
                alert("글 작성 완료!");
                location.href = "/board/list";
            }
        }).catch(e => console.log(e));
    }
})

//취소
let cancelBtn = document.querySelector(".cancel");

cancelBtn.addEventListener("click", () => {
    location.href = "http://localhost:9000/board/list";
})