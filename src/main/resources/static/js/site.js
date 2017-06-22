$(function () {
    //设置ajax全局错误处理
    $.ajaxSetup({
        error: function (jqXHR, textStatus, errorThrown) {
            switch (jqXHR.status) {
                case (500):
                    console.log("服务器内部错误");
                    break;
                case(401):
                    console.log("未登陆");
                    window.location.href = "/";
                    break;
                case(403):
                    console.log("无权限执行此操作");
                    msg("无权限执行此操作");
                    break;
                case(408):
                    console.log("请求超时");
                    break;
                default:
                    console.log("未知错误");
            }
        },
    })
});

function initPageScript() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    if (token && header) {
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    }

    if ($.fn && $.fn.select2 && $.fn.select2.defaults) {
        $.fn.select2.defaults.set("theme", "bootstrap");
        $.fn.select2.defaults.set("width", null);
        $.fn.select2.defaults.set("placeholder", '请选择...');
        $.fn.select2.defaults.set("placeholder", {id: '-1', text: '请选择...'});
        $.fn.select2.defaults.set("placeholderOption", "firset");
    }

    if ($.fn && $.fn.dataTable && $.fn.dataTable.defaults) {
        $.extend(true, $.fn.dataTable.defaults, {
            "searching": true,
            "ordering": true,
            "pageLength": 25,
            "pagingType": "full_numbers",
            "dom": '<"toolbar"><"pull-right"l>rt<"pull-left"i>p<"clear">',
            "language": {'url': '/components/dataTables/js/dataTables.lang-zh_CN.json'},
            "stateSave": false,
            "processing": true,
            "serverSide": true,
            "error.dt": function (e, settings, techNote, message) {
                console.log("An error has been reported by DataTables:", message);
            },
            "ajax": {
                "type": "get",
                "dataSrc": function (json) {
                    FastJson.format(json);
                    return json.data;
                }
            },
        });
        $.fn.dataTable.ext.errMode = 'none';//不显示任何错误信息
    }

    if ($.fn && $.fn.treeSelect && $.fn.treeSelect.defaults) {
        $.fn.treeSelect.defaults.adminUrl = ctxAdmin;
    }
}

function bindSelect(ctrlName, url, value, allowClear, changeMethod) {
    var control = $('#' + ctrlName);
    control.empty();
    $.ajax({
        type: "post",
        url: url,
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            createSelect(control, data, value, allowClear, changeMethod)
        }
    });
}
function bindSelect2Tree(ctrlName, url, value, allowClear) {
    var control = $('#' + ctrlName);
    control.empty();
    $.ajax({
        type: "post",
        url: url,
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            $.each(data, function (i, item) {
                var html = '<option value="' + item.id + '" parent="' + item.parent + '">' + item.text + '</option>';
                control.append(html);
            });
            createSelect2Tree(control, value, allowClear);
        }
    });
}
function createSelect(control, data, value, allowClear, changeMethod) {
    allowClear = allowClear || false;
    value = value || '';
    control.select2({
        allowClear: allowClear,
        language: 'zh-CN',
        data: data,
        escapeMarkup: function (m) {
            return m;
        },
    }).on("change", function (e) {
        if (typeof changeMethod === "function") {
            changeMethod();
        }
    });

    if (value.length > 0) {
        control.val(value).trigger("change");
    } else if (allowClear) {
        control.val("-1").trigger("change");
    } else {
        control.trigger("change");
    }
}
function createSelect2Tree(control, value, allowClear) {
    allowClear = allowClear || false;
    value = value || '';
    control.select2tree({
        allowClear: allowClear,
        language: 'zh-CN',
        escapeMarkup: function (m) {
            return m;
        },
    });
    if (value.length > 0) {
        control.val([value]).trigger('change');
    } else if (allowClear) {
        control.val("-1").trigger("change");
    }
}

function bindZtree(treeName, url, loadedfunction, autoselect, pid, checkbox) {
    var control = $('#' + treeName);
    checkbox = checkbox || false;
    autoselect = autoselect || false;
    pid = pid || '';
    var setting = {
        view: {selectedMulti: false, fontCss: {"font-size": "14px"}},
        check: {enable: checkbox},
        data: {simpleData: {enable: true}},
        callback: {
            onClick: function (event, treeId, treeNode, chickFlag) {
                if (typeof loadedfunction === "function") {
                    loadedfunction(treeNode.id, treeNode.name);
                } else {
                    alert(treeNode.id + "," + treeNode.name);
                }
            },
        }
    }

    $.ajax({
        async: true,
        cache: false,
        type: 'get',
        dataType: 'json',
        url: url,
        success: function (data) {
            var treeObj = $.fn.zTree.init(control, setting, data);
            if (typeof loadedfunction === "function" && autoselect == true) {
                var node = null;
                if (pid.length > 0) {
                    node = treeObj.getNodeByParam('id', pid);
                } else {
                    node = treeObj.getNodesByFilter(function (node) {
                        return node.level == 0
                    }, true);
                }
                if (node != null) {
                    treeObj.selectNode(node);
                    loadedfunction(node.id, node.name);
                }
            }
        }
    });
}
function bindJstree(treeName, url, checkbox, loadedfunction) {
    var control = $('#' + treeName);
    control.unbind('loaded.jstree').bind('loaded.jstree', loadedfunction);
    control.data('jstree', false); //清空数据， 必须
    var isCheck = arguments[2] || false;
    if (isCheck) {
        $.getJSON(url, function (data) {
            control.jstree({
                'plugins': ['checkbox'],
                'checkbox': {cascade: "down", three_state: true},
                'core': {
                    'data': data,
                    'themes': {
                        'responsive': false,
                        'name': 'proton',
                    },
                    'strings': {
                        'Loading ...': '数据正在加载中...'
                    }
                }
            });
            control.jstree(true).get_all_checked = function (full) {
                var tmp = new Array;
                for (var i in this._model.data) {
                    if (this.is_undetermined(i) || this.is_checked(i)) {
                        tmp.push(full ? this._model.data[i] : i);
                    }
                }
                return tmp;
            }
        });
    } else {
        $.getJSON(url, function (data) {
            control.jstree({
                'core': {
                    'data': data,
                    'multiple': false,
                    'themes': {
                        'responsive': false,
                        'name': 'proton',
                    },
                    'strings': {
                        'Loading ...': '数据正在加载中...'
                    }
                }
            });
        });
    }
}

function openDialog(title, url, width, height, callback) {
    top.layer.open({
        type: 2,
        area: [width, height],
        title: title,
        maxmin: true,
        content: url,
        btn: ['确定', '关闭'],
        yes: function (index, layero) {
            var body = top.layer.getChildFrame('body', index);
            var iframeWin = layero.find('iframe')[0];
            if (iframeWin.contentWindow.doSubmit(callback)) {
                try {
                    closeLoading();
                    top.layer.close(index);
                } catch (e) {
                    setTimeout(function () {
                        top.layer.close(index)
                    }, 100)
                }
            }
        },
        cancel: function (index) {

        }
    });
}

function openDialogView(title, url, width, height) {
    top.layer.open({
        type: 2,
        area: [width, height],
        title: title,
        maxmin: true,
        content: url,
        btn: ['关闭'],
        cancel: function (index) {

        }
    })
}

function msg(msg) {
    top.layer.msg(msg, {icon: 1});
}
function err(msg) {
    closeLoading();
    top.layer.msg(msg, {icon: 2});
}
function confirmx(msg, callback) {
    top.layer.confirm(msg, {icon: 3, title: '系统提示'}, function (index) {
        if (typeof callback == "function") {
            callback();
        }
        top.layer.close(index);
    });
}
function loading() {
    top.layer.load(1, {
        shade: [0.1, '#fff']
    });
}

function closeAll() {
    try {
        top.layer.closeAll();
    } catch (e) {
        setTimeout(function () {
            top.layer.closeAll()
        }, 100)
    }
}
function closeLoading() {
    top.layer.closeAll('loading');
}
function closeMsg() {
    top.layer.closeAll('msg');
}

function logout() {
    layer.confirm('您是否要注销当前用户？', {
        btn: ['是', '否'], icon: 3, title: '提示'
    }, function () {
        $('#logoutForm').submit();
    }, function (index) {
        top.layer.close(index);
    });
}

function makeTableViewButton() {
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-success" title="查看" id="viewrow">';
    html += '<i class="ace-icon fa fa-search-plus bigger-120"></i>查看';
    html += '</a>';
    return html;
}
function makeTableEditButton() {
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-info" title="修改" id="editrow">';
    html += '<i class="ace-icon fa fa-pencil bigger-120"></i>修改';
    html += '</a>';
    return html
}
function makeTableDelButton() {
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-danger" title="删除" id="delrow">';
    html += '<i class="ace-icon fa fa-trash-o bigger-120"></i>删除';
    html += '</a>';
    return html;
}
function makeTableAddButton(title) {
    title = title || "添加下级";
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-primary" title="' + title + '" id="addrow">';
    html += '<i class="ace-icon glyphicons glyphicons-plus bigger-120"></i>' + title;
    html += '</a>';
    return html;
}
function makeTableViewSmbtn() {
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-success" title="查看" id="viewrow">';
    html += '<i class="ace-icon glyphicons glyphicons-list-alt bigger-120"></i>';
    html += '</a>';
    return html;
}
function makeTableEditSmbtn() {
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-info" title="修改" id="editrow">';
    html += '<i class="ace-icon glyphicons glyphicons-edit bigger-120"></i>';
    html += '</a>';
    return html
}
function makeTableDelSmbtn() {
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-danger" title="删除" id="delrow">';
    html += '<i class="ace-icon glyphicons glyphicons-remove-2 bigger-120"></i>';
    html += '</a>';
    return html;
}
function makeTableAddSmbtn(title) {
    title = title || "添加下级";
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-primary" title="' + title + '" id="addrow">';
    html += '<i class="ace-icon glyphicons glyphicons-plus bigger-120"></i>';
    html += '</a>';
    return html;
}
function makeTableAuthSmbtn() {
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-warning" title="权限设置" id="authrow">';
    html += '<i class="ace-icon glyphicons glyphicons-flowchart bigger-120"></i>';
    html += '</a>';
    return html;
}
function makeTableUserSmbtn() {
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-primary" title="分配用户" id="userrow">';
    html += '<i class="ace-icon glyphicons glyphicons-user bigger-120"></i>';
    html += '</a>';
    return html;
}
function makeTableAssignSmbtn() {
    var html = '';
    html += '<a href="javascript:;" class="btn btn-xs btn-primary" title="分配角色" id="rolerow">';
    html += '<i class="ace-icon glyphicons glyphicons-group bigger-120"></i>';
    html += '</a>';
    return html;
}
function createToolbarAddBtn() {
    var html = '<button class="btn btn-white btn-info btn-bold" data-toggle="tooltip" type="button" ';
    html += 'data-placement="left" onclick="add()" title="添加">';
    html += '<i class="ace-icon glyphicons glyphicons-plus"></i>添加';
    html += '</button>';
    return html;
}
function createToolbarRefBtn() {
    var html = '<button class="btn btn-white btn-info btn-bold" data-toggle="tooltip" type="button" ';
    html += 'data-placement="left" onclick="refresh()" title="刷新">';
    html += '<i class="ace-icon glyphicons glyphicons-refresh"></i>刷新';
    html += '</button>';
    return html;
}
function createToolbarSaveSortBtn() {
    var html = '<button class="btn btn-white btn-info btn-bold" data-toggle="tooltip" type="button" ';
    html += 'data-placement="left" onclick="updateSort()" title="保存排序">';
    html += '<i class="ace-icon glyphicons glyphicons-floppy-disk"></i>保存排序';
    html += '</button>';
    return html;
}

// 获取URL地址参数
function getQueryString(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    if (!url || url == "") {
        url = window.location.search;
    } else {
        url = url.substring(url.indexOf("?"));
    }
    r = url.substr(1).match(reg)
    if (r != null) return unescape(r[2]);
    return null;
}

// 获取字典标签
function getDictLabel(data, value, defaultValue) {
    for (var i = 0; i < data.length; i++) {
        var row = data[i];
        if (row.value == value) {
            return row.label;
        }
    }
    return defaultValue;
}
//日期格式化 new Date(Date.parse(data)).Format("yyyy-MM-dd hh:mm:ss");
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(),  //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmr = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

var FastJson = {
    isArray: function (a) {
        return "object" == typeof a
            && "[object array]" == Object.prototype.toString.call(a)
                .toLowerCase();
    },
    isObject: function (a) {
        return "object" == typeof a
            && "[object object]" == Object.prototype.toString.call(a)
                .toLowerCase();
    },
    format: function (a) {
        if (null == a)
            return null;
        "string" == typeof a && (a = eval("(" + a + ")"));
        return this._format(a, a, null, null, null);
    },
    _randomId: function () {
        return "randomId_" + parseInt(1E9 * Math.random());
    },
    _getJsonValue: function (a, c) {
        var d = this._randomId(), b;
        b = "" + ("function " + d + "(root){") + ("return root." + c + ";");
        b += "}";
        b += "";
        var e = document.createElement("script");
        e.id = d;
        e.text = b;
        document.body.appendChild(e);
        d = window[d](a);
        e.parentNode.removeChild(e);
        return d;
    },
    _format: function (a, c, d, b, e) {
        d || (d = "");
        if (this.isObject(c)) {
            if (c.$ref) {
                var g = c.$ref;
                0 == g.indexOf("$.")
                && (b[e] = this._getJsonValue(a, g.substring(2)));
                return
            }
            for (var f in c)
                b = d, "" != b && (b += "."), g = c[f], b += f, this
                    ._format(a, g, b, c, f);
        } else if (this.isArray(c))
            for (f in c)
                b = d, g = c[f], b = b + "[" + f + "]", this._format(a, g,
                    b, c, f);
        return a;
    }
};
function bindImageUpload(ctrName, maxFileCount, uri) {
    var options = {
        language: 'zh',
        uploadUrl: uri,
        deleteUrl: '/upfile/delete',
        allowedFileExtensions: ['jpeg', 'jpg', 'png', 'gif', 'bmp'],
        allowedFileTypes: ['image'],
        showUpload: false,
        showCaption: false,
        showRemove: false,
        maxFileCount: maxFileCount,
        autoReplace: true,
        initialPreviewShowDelete: true,
        browseClass: "btn btn-primary",
        previewFileIcon: "<i class='ace fa fa-file'></i>",
        browseIcon: "<i class='ace fa fa-folder-open'></i>",
        removeIcon: "<i class='ace fa fa-trash-o'></i>",
        layoutTemplates: {
            actionDelete: '',
            actionUpload: ''
        }
    };
    var control = $('#' + ctrName);
    var imageUrl = control.attr("value");
    if (!isEmpty(imageUrl)) {
        var op = $.extend({
            initialPreview: [
                "<img src='" + imageUrl + "' class='file-preview-image kv-preview-data rotate-43106 is-landscape-gt4' style='width: auto;height: 160px;' alt='image' title='image'/>",
            ]
        }, options);
        control.fileinput(op);
    } else {
        control.fileinput(options);
    }
    control.on("filebatchselected", function (event, files) {
        control.fileinput("upload");
    }).on("fileuploaded", function (event, data) {
        var response = data.response;
        if (response.success) {
            control.attr("value", response.data[0]);
        } else {
            err(response.message);
        }
    }).on('fileremoved', function (event, id, index) {
        console.log('id = ' + id + ', index = ' + index);
        control.attr("value", "");
    }).on('filedeleted', function (event, key, jqXHR, data) {
        console.log('Key = ' + key);
        control.attr("value", "");
    });
}

function isEmpty(str) {
    if (!str && typeof str != "undefined" && str != 0) {
        return true;
    }
    if (str.replace(/(^s*)|(s*$)/g, "").length == 0) {
        return true;
    }
    return false;
}