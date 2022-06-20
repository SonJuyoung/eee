let consoleLog = false;  // console 출력 여부

/**
 * header.jsp 에 hidden 속성으로 contextPath를 먼저 지정한다.
 * <input type="hidden" id="contextPath" value="<%=request.getContextPath()%>">
 * @param url
 * @returns {contextPath}/url
 */
function makeApiUrl(url) {
    return $("#contextPath").val() + url;
}

function goPage(url) {
    location.href = makeApiUrl(url);
}

function requestList(pageNo, url) {
    document.frm.pageIndex.value = pageNo;
    document.frm.action = makeApiUrl(url);
    document.frm.submit();
}

/**
 * enterKey 입력 시 페이지 호출
 * @param event
 * @param url
 */
function pressSearchKey(event, url) {
    if (event.keyCode === 13) {
        requestList('1', url);
    }
}

/**
 * GET 방식 ajax 호출
 * @param url
 * @param callbackFunc
 * @param data
 * @param callbackFailFunc
 * @param ajaxAsync (default = true)
 */
function callAjaxGet(url, callbackFunc, data, callbackFailFunc, ajaxAsync) {
    if (ajaxAsync === undefined || ajaxAsync === '') {
        ajaxAsync = true;
    }
    let transferData = getTransferData(data);
    url = makeApiUrl(url);
    if (consoleLog) {
        console.log(url);
    }
    $.ajax({
        type: "get",
        async: ajaxAsync,
        url: url,
        data: transferData,
        dataType: "json",
        cache: false,
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        beforeSend: function (jqXHR) {
            $("#loading").show();
            jqXHR.setRequestHeader("AJAX", "true");
            if (localStorage.token) {
                jqXHR.setRequestHeader('Authorization', 'Bearer ' + localStorage.token);
            }
        },
        complete: function () {
            $("#loading").hide();
        },
        success: function (data, textStatus, jqXHR) {
            if (consoleLog) {
                console.log("Success=====S");
                logView(data);
                console.log("Success=====E");
            }
            if (isFunction(callbackFunc)) {
                window[callbackFunc](data);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (consoleLog) {
                console.log("error=====S");
                logView(jqXHR);
                console.log("error=====E");
            }
            if (isFunction(callbackFailFunc)) {
                window[callbackFailFunc](jqXHR);
            } else {
                handleErrorStatus(jqXHR);
            }
        }
    });
}

/**
 * POST 방식 ajax 호출
 * @param url
 * @param callbackFunc
 * @param data
 * @param callbackFailFunc
 * @param ajaxAsync ( default = true )
 */
function callAjaxPost(url, callbackFunc, data, callbackFailFunc, ajaxAsync) {
    if (ajaxAsync === undefined || ajaxAsync === '') {
        ajaxAsync = true;
    }
    let transferData = getTransferData(data);
    url = makeApiUrl(url);
    if (consoleLog) {
        console.log(url);
    }
    $.ajax({
        type: "post",
        async: ajaxAsync,
        url: url,
        data: transferData,
        dataType: "json",
        cache: false,
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        beforeSend: function (jqXHR) {
            $("#loading").show();
            jqXHR.setRequestHeader("AJAX", "true");
            if (localStorage.token) {
                jqXHR.setRequestHeader('Authorization', 'Bearer ' + localStorage.token);
            }
        },
        complete: function () {
            $("#loading").hide();
        },
        success: function (data, textStatus, jqXHR) {
            if (consoleLog) {
                console.log("Success=====S");
                logView(data);
                console.log("Success=====E");
            }
            if (isFunction(callbackFunc)) {
                window[callbackFunc](data);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR)
            console.log(textStatus)
            console.log(errorThrown)
            if (consoleLog) {
                console.log("error=====S");
                logView(jqXHR);
                console.log("error=====E");
            }
            if (isFunction(callbackFailFunc)) {
                window[callbackFailFunc](jqXHR.responseJSON);
            } else {
                handleErrorStatus(jqXHR);
            }
        }
    });
}

/**
 * multipart ajax call
 * @param url
 * @param callbackFunc
 * @param formId
 * @param callbackFailFunc
 */
function callByMultipart(url, callbackFunc, formId, callbackFailFunc = '') {

    if (consoleLog) {
        console.log(url);
    }
    url = makeApiUrl(url);

    $("#" + formId).ajaxForm({
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        processData: false,
        dataType: 'json',
        beforeSend: function (jqXHR) {
            jqXHR.setRequestHeader("AJAX", "true");
        },
        xhr: function () {
            var xhr = $.ajaxSettings.xhr();
            if ($(".progressBar:visible").length > 0) {
                xhr.upload.onprogress = function (e) {
                    var percent = e.loaded * 100 / e.total;
                    $(".progressBar:visible").val(percent); //개별 파일의 프로그레스바 진행
                };
            }
            return xhr;
        },
        complete: function (jqXHR, textStatus) {

        },
        success: function (data, textStatus, jqXHR) {
            if (consoleLog) {
                console.log("Success=====S");
                logView(data);
                console.log("Success=====E");
            }
            if (isFunction(callbackFunc)) {
                window[callbackFunc](data);
            }
        },
        error: function (jqXHR, textStatus) {
            if (consoleLog) {
                console.log("error=====S");
                logView(jqXHR);
                console.log("error=====E");
            }
            if (isFunction(callbackFailFunc)) {
                window[callbackFailFunc](jqXHR);
            } else {
                handleErrorStatus(jqXHR);
            }
        }
    });

    $("#" + formId).attr("action", url);
    $("#" + formId).submit();
}

function getTransferData(data) {
    var transferData = '';
    if (typeof (data) === 'object') {
        transferData = data;
    } else if (typeof (data) === 'string') {
        transferData = data !== "" ? $("#" + data).serialize() : "";
    }
    return transferData;
}

function logView(data) {
    console.log(JSON.stringify(data, null, 4));
}

function isFunction(functionName) {
    return (typeof window[functionName] === "function");
}

function handleErrorStatus(jqXHR, forbidden) {
    if (jqXHR.status === 401) {
        alert("세션이 만료되었습니다. 다시 로그인을 해주세요.");
        // document.location.href = makeApiUrl("/");
    } else if (jqXHR.status === 403) {
        //console.log(JSON.stringify(jqXHR.responseText,null,4));

    } else if (jqXHR.status === 404) {
        //console.log(JSON.stringify(jqXHR.responseText,null,4));
    } else if (jqXHR.status === 400) {
        //console.log(JSON.stringify(jqXHR.responseText,null,4));
    } else if (jqXHR.status === 405) {

    } else if (jqXHR.status === 500) {
        //console.log(JSON.stringify(jqXHR.responseText,null,4));
    } else {
        //console.log(JSON.stringify(jqXHR.responseText,null,4));
    }
}


/**
 * 전체 replace
 * @param str
 * @param searchStr
 * @param replaceStr
 * @returns {*}
 */
function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}

/**
 * input 필드에 오직 숫자만 입력
 * 사용방법 : oninput="this.value = onlyNumber(this.value);"
 * @param value
 * @returns {*}
 */
function onlyNumber(value) {
    var tempValue = parseInt(value.replace(/,/gi, ""));
    return String(tempValue).replace(/[^0-9]/g, '').replace(/(\..*)\./g, '$1');
}

// 영문만
function onlyEnglish(value) {
    return value.replace(/[^a-zA-Z]/g, '').replace(/(\..*)\./g, '$1');
}

// 한글만
function onlyKorea(value) {
    return value.replace(/([^가-힣ㄱ-ㅎㅏ-ㅣ\x20])/g, '').replace(/(\..*)\./g, '$1');
}

// 영문만 제외
function exceptEnglish(value) {
    return value.replace(/[a-zA-Z]/g, '').replace(/(\..*)\./g, '$1');
}

// 한글만 제외
function exceptKorea(value) {
    return value.replace(/([가-힣ㄱ-ㅎㅏ-ㅣ\x20])/g, '').replace(/(\..*)\./g, '$1');
}

//검색 초기화
function searchInit() {
    $("#search").find('input[type=text]').each(function () {
        $(this).val('');
    });
    $('#search select').each(function () {
        $(this).find('option:eq(0)').prop("selected", true);
    });
}


/**
 * escape
 * @param str
 * @returns {*}
 */
function strConv(str) {
    str = str.replace(/&lt;/gi, "<");
    str = str.replace(/&gt;/gi, ">");
    str = str.replace(/&quot;/gi, "\"");
    //str = str.replace(/&nbsp;/gi," ");
    str = str.replace(/&amp;/gi, "&");
    str = str.replace(/&amp;#034;/gi, "\"");
    str = str.replace(/&#034;/gi, "\"");
    return str;
}


/**
 * checkbox 전체 선택
 * @param targetName target checkbox name
 */
function checkAllCheckbox(targetName) {
    const checkboxes = document.getElementsByName(targetName);
    let isChecked = false;
    for (let i = 0, n = checkboxes.length; i < n; i++) {
        if (checkboxes[i].disabled) {
            continue;
        }
        if (!checkboxes[i].checked) {
            isChecked = true;
            break;
        }
    }
    for (let i = 0, n = checkboxes.length; i < n; i++) {
        if (checkboxes[i].disabled) {
            continue;
        }
        checkboxes[i].checked = isChecked;
    }
}

/**
 * target checkbox의 상태를 확인하여 main checkbox 값을 변경
 * @param targetName target checkbox name
 * @param mainCheckerID 전체 선택 기능을 가진 checkbox id
 */
function modifyAllCheckerStatus(targetName, mainCheckerID) {
    const checkboxes = document.getElementsByName(targetName);
    let isAllSelected = true;

    for (let i = 0; i < checkboxes.length; i++) {
        if (!checkboxes[i].checked) {
            isAllSelected = false;
        }
    }
    document.getElementById(mainCheckerID).checked = isAllSelected;
}

/**
 * 형제 태그
 * @param t
 * @returns {*[]}
 */
function siblings(t) {
    let children = t.parentElement.children;
    let tempArr = [];

    for (let i = 0; i < children.length; i++) {
        tempArr.push(children[i]);
    }

    return tempArr.filter(function (e) {
        return e != t;
    });
}

/**
 * 형제 태그 중 특정 태그 찾기
 * @param obj
 * @returns {any}
 * @param info 입력정보 : tagName(대문자), type, id, name
 * @example info {"tagName":"INPUT", "type":"text"}
 */
function findTag(obj, info) {
    let keys = Object.keys(info);
    return obj.find(function (element) {
        let count = 0;
        for (let i = 0; i < keys.length; i++) {
            if (element[keys[i]] === info[keys[i]]) {
                count++;
            }
        }
        return count === keys.length;
    });
}

/**
 * tag 삭제
 * @param obj
 */
function deleteTag(obj) {
    obj.parentElement.removeChild(obj);
}

/**
 * 파일 확장자
 * @returns {string}
 * @param fileObj
 */
function getFileExt(fileObj) {
    // return fileObj.split('.').pop().toLowerCase();
    return getFileInfo(fileObj, 'ext');
}

/**
 * 파일 이름
 * @param fileObj
 * @returns {*|string|string}
 */
function getFileName(fileObj) {
    return getFileInfo(fileObj, 'name');
}

/**
 * 파일 이름.확장자
 * @param fileObj
 * @returns {*|string|string}
 */
function getFileNameAndExt(fileObj) {
    return getFileInfo(fileObj, 'all');
}

function getFileInfo(fileObj, flag) {
    let pathHeader = fileObj.lastIndexOf('\\');
    let pathMiddle = fileObj.lastIndexOf('.');
    let pathEnd = fileObj.length;
    let fileName = fileObj.substring(pathHeader + 1, pathMiddle);
    let extName = fileObj.substring(pathMiddle + 1, pathEnd);
    if (flag === 'ext') {
        return extName;
    } else if (flag === 'name') {
        return fileName;
    } else if (flag === 'all') {
        return fileName + '.' + extName;
    }
    return '';
}

function fileDownload(atchFileId, fileSn) {
    window.location.href = makeApiUrl('/cmm/fms/FileDown.do?atchFileId=' + atchFileId + '&fileSn=' + fileSn);
}

/**
 * API 호출 시 데이터 전달 함수
 * @param {Object} id Form 아이디
 * @param {Object} name 파라메타 명
 * @param {Object} value 파라메타 값
 */
function formData(id, name, value) {
    if (id === '') {
        return false;
    }
    var objMethod = document.createElement("input");
    objMethod.type = "hidden";
    objMethod.name = name;
    objMethod.value = value;
    if (typeof (id) === 'string') {
        document.getElementById(id).insertBefore(objMethod, null);
    } else {
        id.insertBefore(objMethod, null);
    }
}

/**
 * API 호출 시 데이터 전달 Form 삭제 함수
 * @param {Object} id Form 아이디
 * @param {Object} name 파라메타 명
 */
function formDataDelete(id, name) {
    $("#" + id + " input[name=\"" + name + "\"]").remove();
}

/**
 * API 호출 시 데이터 전달 Form 전체 삭제 함수
 * @param {Object} id Form 아이디
 */
function formDataDeleteAll(id) {
    $("#" + id).children().remove();
}

/**
 * form 데이터 를 json object 로 변환하는 함수
 * @param formId
 * @returns {{}}
 */
function convertFormDataToJson(formId) {
    let data = {};
    let array = $('#' + formId).serializeArray()
    for (let i = 0; i < array.length; i++) {
        data[array[i].name] = array[i].value;
    }
    return data;
}

function addComMa(str) {
    return str.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function removeComMa(str) {
    return str.replace(/,/g, "");
}

var iv = [48, 81, 80, 97, 71, 107, 80, 109, 80, 70, 55, 83, 97, 102, 76, 110];

function AesText(text) {
    var textBytes = aesjs.utils.utf8.toBytes(text);
    var mod = textBytes.length % 16;
    if (mod % 16 != 0) {
        text += ''.padStart(16 - mod);
        textBytes = aesjs.utils.utf8.toBytes(text);
    }
    var aesCbc = new aesjs.ModeOfOperation.cbc(aesjs.utils.utf8.toBytes(l_Key), iv);
    var encryptedBytes = aesCbc.encrypt(textBytes);
    var encryptedHex = aesjs.utils.hex.fromBytes(encryptedBytes);
    return encryptedHex;
}


function AesDText(text) {
    var encryptedBytes = aesjs.utils.hex.toBytes(text);

    var aesCbc = new aesjs.ModeOfOperation.cbc(aesjs.utils.utf8.toBytes(l_Key), iv);
    var decryptedBytes = aesCbc.decrypt(encryptedBytes);
    var decryptedText = aesjs.utils.utf8.fromBytes(decryptedBytes).trim();

    return decryptedText;
}

function showLoading() {
    $('#loading').remove();
    $('body').append($('<div id="loading" class="loading"></div>'));
}

function hideLoading() {
    $(".loading").fadeOut();
}

function setLocationHash(pageNo) {
    window.location.hash = '#' + pageNo;
}

function getPageNoFromHash() {
    if (document.location.hash) {
        return Number(document.location.hash.replace("#", ""));
    }
    return 1;
}

function historyBack(callbackFailFunc) {
    window.onpopstate = function (event) {
        if (document.location.hash) {
            if (isFunction(callbackFailFunc)) {
                window[callbackFailFunc]();
            } else if (typeof callbackFailFunc === 'function') {
                callbackFailFunc();
            }
        } else {
            history.back();
        }
    }
}

function setSessionObject(key, obj) {
    if (('sessionStorage' in window) && window['sessionStorage'] !== null && typeof obj === 'object') {
        let objKeys = Object.keys(obj);
        for (let i = 0; i < objKeys.length; i++) {
            let objKey = objKeys[i];
            sessionStorage.setItem(key + "_" + objKey, obj[objKey]);
        }
    }
}

function getSessionItem(key, objKeys) {
    if (('sessionStorage' in window) && window['sessionStorage'] !== null && Array.isArray(objKeys)) {
        let obj = {};
        for (let i = 0; i < objKeys.length; i++) {
            let objKey = objKeys[i];
            obj[objKey] = sessionStorage.getItem(key + "_" + objKey);
        }
        return obj;
    }
}
