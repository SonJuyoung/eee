/**
 * sweetalert 공통함수
 * @param title 알림 메시지
 * @param additionalWork 추가작업(함수 or redirect URL
 */

// 알림 - alert (아이콘 - i)
function alertInfo(title, additionalWork) {
    let icon = 'info';
    if(additionalWork !== undefined && additionalWork !== '') {
        runAdditionalWork(icon, title, additionalWork);
    } else {
        Swal.fire(getSwalObject(icon, title));
    }
}
// 성공 - alert (아이콘 - 체크)
function alertSuccess(title, additionalWork) {
    let icon = 'success';
    if(additionalWork !== undefined && additionalWork !== '') {
        runAdditionalWork(icon, title, additionalWork);
    } else {
        Swal.fire(getSwalObject(icon, title));
    }
}
// 에러 - alert (아이콘 - X)
function alertError(title, additionalWork) {
    let icon = 'error';
    if(additionalWork !== undefined && additionalWork !== '') {
        runAdditionalWork(icon, title, additionalWork);
    } else {
        Swal.fire(getSwalObject(icon, title));
    }
}
// 경고 - alert (아이콘 - !)
function alertWarning(title, additionalWork) {
    let icon = 'warning';
    if(additionalWork !== undefined && additionalWork !== '') {
        runAdditionalWork(icon, title, additionalWork);
    } else {
        Swal.fire(getSwalObject(icon, title));
    }
}
// 의문 - alert (아이콘 - ?)
function alertQuest(title, additionalWork) {
    let icon = 'question';
    if(additionalWork !== undefined && additionalWork !== '') {
        runAdditionalWork(icon, title, additionalWork);
    } else {
        Swal.fire(getSwalObject(icon, title));
    }
}

// sweet alert confirm 함수
// icon = (success = 체크, info = i, error = X, warning = !, question = ?)
// denyTF = 취소 버튼 유무 (true = 취소버튼 생성, false = 취소버튼 숨김)
// msg = 메시지 내용
// confirmFunc = 확인시 함수
function swConfirm(icon, denyTF , msg, confirmFunc, deniedFunc) {
    Swal.fire({
        icon: icon,
        title: msg,
        showDenyButton: denyTF,
        showCancelButton: false,
        confirmButtonText: '확인',
        denyButtonText: `취소`,
        allowOutsideClick: false,
        allowEscapeKey: false
    }).then((result) => {
        if (result.isConfirmed) {
            if (isFunction(confirmFunc)) {
                window[confirmFunc]();
            } else if (typeof confirmFunc === 'function') {
                confirmFunc();
            }
        }
        if (result.isDenied) {
            if (isFunction(deniedFunc)) {
                window[deniedFunc]();
            } else if (typeof deniedFunc === 'function') {
                deniedFunc();
            }
            else {
                return false;
            }
        }
    })
}

function getSwalObject(icon, title) {
    return {
        icon: icon,
        confirmButtonText: '확인',
        title: title,
        text: '',
        allowOutsideClick: false,
        allowEscapeKey: false
    }
}

function runAdditionalWork(icon, title, additionalWork) {
    if (isFunction(additionalWork)) {
        Swal.fire(getSwalObject(icon, title)).then(function () {
            window[additionalWork]();
        });
    } else if (typeof additionalWork === 'function') {
        Swal.fire(getSwalObject(icon, title)).then(function () {
            additionalWork();
        });
    } else if (typeof additionalWork === 'string') {
        Swal.fire(getSwalObject(icon, title)).then(function () {
            goPage(additionalWork)
        });
    }
}
