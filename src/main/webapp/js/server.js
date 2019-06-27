//获取项目地址
function getRootPath() {
    var currentPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = currentPath.indexOf(pathName);
    var path = currentPath.substring(0, pos);
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return path + projectName;
}

//返回年月日时分秒
function getFormatDate(value) {
    var date = new Date(value);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    if (hour < 10) {
        hour = "0" + hour;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    if (seconds < 10) {
        seconds = "0" + seconds;
    }
    return year + '-' + month + '-' + day + " " + hour + ":" + minutes + ":" + seconds;
}

//返回年月日
function getDate(value) {
    var date = new Date(value);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    return year + '-' + month + '-' + day ;
}