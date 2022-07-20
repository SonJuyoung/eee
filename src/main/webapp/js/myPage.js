let imgUpload = document.querySelector(".user-img");
let profileImg = document.querySelector(".profile-img");

profileImg.addEventListener("click", ()=> {
    imgUpload.click();
})

imgUpload.addEventListener("change", getImgFile);

function getImgFile(e) {
    const img = e.target.files;
    const formData = new FormData(); // 업로드 할 파일 저장 객체
    let fileNm;

    let iuser = document.querySelector(".mypage-info > h5").dataset.set;

    formData.append("uploadFile", img[0]);
    console.log(img);
    //파일 타입 검사
    [...img].forEach((item)=> {
        if (!item.type.match("image/.*")) {
            alert("이미지 파일을 업로드 해주세요.");
            return;
        }
    })
    //파일 선택 x -> 리턴
    if (img.length === 0) {
        console.log("프로필 사진 없음")
        return;
    }

    fetch("http://localhost:9000/profileImgFile", {
        method: 'POST',
        processData: false,
        body: formData
    }).then(res => {
        console.log(res);
        return res.text();
    }).then(data => {
        data = data.split("webapp\\")[1];
        console.log(data);
        fileNm = data;
        fetch("http://localhost:9000/profileImg", {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                'iuser' : iuser,
                'fileNm' : data
            })
        }).then(res => {
            console.log(res);
            return res.json();
        }).then(data => {
            console.log(data);
            profileImg.src = fileNm;
        }).catch(e=> {
            console.error(e);
        })
    }).catch(e => {
        console.error(e);
    })
}