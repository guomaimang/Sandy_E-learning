var editorD;

$(function () {

    $('.alert-danger').css("display", "none");

    const E = window.wangEditor;
    editorD = new E('#wangEditor')
    editorD.config.height = 400
    editorD.config.uploadImgServer = 'images/upload'
    editorD.config.uploadFileName = 'file'
    editorD.config.uploadImgMaxSize = 2 * 1024 * 1024
    editorD.config.uploadImgMaxLength = 1
    editorD.config.showLinkImg = false
    editorD.config.uploadImgHooks = {
        success: function (xhr) {
            console.log('success', xhr)
        },
        fail: function (xhr, editor, resData) {
            console.log('fail', resData)
        },
        error: function (xhr, editor, resData) {
            console.log('error', xhr, resData)
        },
        timeout: function (xhr) {
            console.log('timeout')
        },
        customInsert: function (insertImgFn, result) {
            if (result != null && result.resultCode == 200) {
                // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
                insertImgFn(result.data)
            } else {
                alert("error");
            }
        }
    }
    editorD.create();

    $('#articleModal').modal('hide');

    $("#jqGrid").jqGrid({
        url: 'articles/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden: true},
            {label: 'Title', name: 'title', index: 'title', width: 240},
            {label: 'Author', name: 'author', index: 'author', width: 120},
            {label: 'PublishTme', name: 'createTime', index: 'createTime', width: 120},
            {label: 'UpdateTime', name: 'updateTime', index: 'updateTime', width: 120}
        ],
        height: 560,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: 'Information reading in progress...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        },
    });
    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});

$('#saveButton').click(function () {
    if (validObject()) {
        //ajax
        var id = $("#articleId").val();
        var title = $("#articleName").val();
        var author = $("#author").val();
        var content = editorD.txt.html();
        var data = {"title": title, "content": content, "author": getCookie("userName")};
        var url = 'articles/add';
        var method = 'POST';
        if (id > 0) {
            data = {"id": id, "title": title, "content": content, "author": getCookie("userName")};
            url = 'articles/update';
            method = 'PUT';
        }
        $.ajax({
            type: method,
            dataType: "json",
            url: url,//url
            contentType: "application/json; charset=utf-8",
            beforeSend: function (request) {
                //设置header值
                request.setRequestHeader("token", getCookie("token"));
            },
            data: JSON.stringify(data),
            success: function (result) {
                checkResultCode(result.resultCode);
                if (result.resultCode == 200) {
                    $('#articleModal').modal('hide');
                    swal("Save successfully", {
                        icon: "success",
                    });
                    reload();
                }
                else {
                    $('#articleModal').modal('hide');
                    swal("Failed to save", {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("Operation failure", {
                    icon: "error",
                });
            }
        });

    }
});

function articleAdd() {
    reset();
    $('.modal-title').html('Add');
    $('#articleModal').modal('show');
}

function articleEdit() {
    reset();
    $('.modal-title').html('Edit');

    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    $.get("articles/info/" + id, function (r) {
        if (r.resultCode == 200 && r.data != null) {
            $('#articleId').val(r.data.id);
            $('#articleName').val(r.data.title);
            $('#articleAuthor').val(r.data.author);
            editorD.txt.html(r.data.content);
        }
    });
    //显示modal
    $('#articleModal').modal('show');
}

/**
 * 数据验证
 */
function validObject() {
    var articleName = $('#articleName').val();
    if (isNull(articleName)) {
        showErrorInfo("The title can not be blank!");
        return false;
    }
    if (!validLength(articleName, 120)) {
        showErrorInfo("Title characters cannot be larger than 120!");
        return false;
    }
    var articleAuthor = $('#articleAuthor').val();
    if (isNull(articleAuthor)) {
        showErrorInfo("Author cannot be empty!");
        return false;
    }
    var ariticleContent = editorD.txt.html();
    if (isNull(ariticleContent) || ariticleContent == 'Please enter...') {
        showErrorInfo("Content cannot be empty!");
        return false;
    }
    if (!validLength(ariticleContent, 3000)) {
        showErrorInfo("Content characters cannot be larger than 3000!");
        return false;
    }
    return true;
}

/**
 * 重置
 */
function reset() {
    $('.alert-danger').css("display", "none");
    $('#articleId').val(0);
    $('#articleName').val('');
    $('#articleAuthor').val('');
    editorD.txt.html('');
}

function deleteArticle() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "Confirmation Box",
        text: "Confirm to delete data??",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
        if (flag) {
            $.ajax({
                type: "DELETE",
                url: "articles/delete",
                contentType: "application/json",
                beforeSend: function (request) {
                    request.setRequestHeader("token", getCookie("token"));
                },
                data: JSON.stringify(ids),
                success: function (r) {
                    checkResultCode(r.resultCode);
                    if (r.resultCode == 200) {
                        swal("Deleted successfully", {
                            icon: "success",
                        });
                        $("#jqGrid").trigger("reloadGrid");
                    } else {
                        swal(r.message, {
                            icon: "error",
                        });
                    }
                }
            });
        }
    });
}


function reload() {
    reset();
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}