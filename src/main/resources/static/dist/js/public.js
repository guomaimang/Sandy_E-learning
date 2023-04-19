
function isNull(obj) {
    if (obj == null || obj == undefined || obj.trim() == "") {
        return true;
    }
    return false;
}


function validLength(obj, length) {
    if (obj.trim().length < length) {
        return true;
    }
    return false;
}

function validUserName(userName) {
    var pattern = /^[A-Za-z0-9]+([_\.][A-Za-z0-9]+)*@([A-Za-z0-9\-]+\.)+[A-Za-z]{2,6}$/;
    if (pattern.test(userName.trim())) {
        return (true);
    } else {
        return (false);
    }
}


function validPassword(password) {
    var pattern = /^[a-zA-Z0-9]{6,20}$/;
    if (pattern.test(password.trim())) {
        return (true);
    } else {
        return (false);
    }
}

<!-- 正则验证 end-->

function login() {
    var userName = $("#userName").val();
    var password = $("#password").val();
    if (isNull(userName)) {
        showErrorInfo("Please enter your username!");
        return;
    }
    if (!validUserName(userName)) {
        showErrorInfo("Please enter the correct user name!");
        return;
    }
    if (isNull(password)) {
        showErrorInfo("Please enter your password!");
        return;
    }
    if (!validPassword(password)) {
        showErrorInfo("Please enter the correct password!");
        return;
    }
    var data = {"userName": userName, "password": password}
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "users/login",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        success: function (result) {
            if (result.resultCode == 200) {
                $('.alert-danger').css("display", "none");
                setCookie("token", result.data.userToken);
                setCookie("userName", result.data.userName);
                setCookie("isStudent", result.data.isStudent);
                window.location.href = "/";
            }
            ;
            if (result.resultCode == 500) {
                showErrorInfo("Login failed! Please check account and password!");
                return;
            }
        },
        error: function () {
            $('.alert-danger').css("display", "none");
            showErrorInfo("Interface exception, please contact the administrator!");
            return;
        }
    });
}

function logout(){
    delCookie("token");
    delCookie("userName");
    delCookie("isStudent");
    window.location.href = "/login.html";
}

function oauth2Login() {
    const queryParams = new URLSearchParams(window.location.search);
    const code = queryParams.get("code");
    var data = {"code": code}

    $.ajax({
        type: "POST",
        dataType: "json",
        url: "oauth2/callback",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),

        success: function (result) {
            if (result.resultCode == 200) {
                setCookie("token", result.data.userToken);
                setCookie("userName", result.data.userName);
                setCookie("isStudent", result.data.isStudent);
                window.location.href = "/";
            }
            ;
            if (result.resultCode == 406) {
                alert("Login failed! The account is not authenticated!")
                window.location.href = "/login.html";
                return;
            }
            if (result.resultCode == 500) {
                alert("Server Error!")
                window.location.href = "/login.html";
                return;
            }
        },
        error: function () {
            alert("Interface exception, please contact the administrator!")
            return;
        }
    });
}



/**
 * 写入cookie
 *
 * @param name
 * @param value
 */
function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=/";

}


function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}


function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}


function checkCookie() {
    if (getCookie("token") == null) {
        swal("Not logged in", {
            icon: "error",
        });
        window.location.href = "login.html";
    }
}


function checkResultCode(code) {
    if (code == 402) {
        window.location.href = "login.html";
    }
}


function showErrorInfo(info) {
    $('.alert-danger').css("display", "block");
    $('.alert-danger').html(info);
}


function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        swal("Please select one record!", {
            icon: "error",
        });
        return;
    }
    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        swal("Please select one record!", {
            icon: "error",
        });
        return;
    }
    return selectedIDs[0];
}


function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        swal("Please select one record!", {
            icon: "error",
        });
        return;
    }
    return grid.getGridParam("selarrrow");
}