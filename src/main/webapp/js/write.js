let titleElem = document.querySelector(".title");
let writerElem = document.querySelector(".writer");
let ctntElem = document.querySelector(".ctnt");
let rdtElem = document.querySelector(".rdt");


let saveBtn = document.querySelector(".save-btn");

rdtElem.value = new Date().toISOString().substring(0, 10);

saveBtn.addEventListener("click", (e)=> {
    e.preventDefault();
    let fixElem = document.querySelector("input[name=fix]:checked");
console.log(fixElem.value);
    fetch("http://localhost:9000/board/write", {
        method: "POST",
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
})