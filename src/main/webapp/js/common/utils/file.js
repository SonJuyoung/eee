function getFileNames(files) {
    let names = [];
    for(let i=0; i<files.length; i++) {
        names.push(files[i].name)
    }
    return names;
}

function previewImage(file) {
    if (!file.type.startsWith('image/')) {
        return false;
    }

    const img = document.createElement("img");
    img.classList.add("obj");
    img.file = file;
    const reader = new FileReader();

    reader.onload = (function (aImg) {
        return function (e) {
            aImg.src = e.target.result;
        };
    })(img);

    reader.readAsDataURL(file);

    return img;
}