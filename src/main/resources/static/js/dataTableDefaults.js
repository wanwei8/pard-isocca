$.extend(true, $.fn.dataTable.defaults, {
    "searching": true,
    "ordering": true,
    "pageLength": 25,
    "pagingType": "full_numbers",
    "dom": '<"pull-right"l>rt<"pull-left"i>p<"clear">',
    "language": {'url': '/components/dataTables/js/dataTables.lang-zh_CN.json'},
    "stateSave": false,
    "processing": true,
});

function splitColumns(columnStr) {
    var columns = [];
    var colArr = columnStr.split(',');

    for (var i = 0; i < colArr.length; i++) {
        var obj = {};
        obj['data'] = colArr[i].trim();
        columns.push(obj);
    }
    return columns;
}

function makeTableOperatorButton(bView, bEdit, bDel, bChild, bAdd) {
    var html = '<div class="btn-group" >';

    if (bView) {
        html += '<a href="javascript:;" class="btn btn-xs btn-success" title="查看">';
        html += '<i class="ace-icon fa fa-search-plus bigger-120"></i>查看';
        html += '</a>';
    }
    if (bEdit) {
        html += '<a href="javascript:;" class="btn btn-xs btn-info" title="修改">';
        html += '<i class="ace-icon fa fa-pencil bigger-120"></i>修改';
        html += '</a>';
    }
    if (bDel) {
        html += '<a href="javascript:;" class="btn btn-xs btn-danger" title="删除">';
        html += '<i class="ace-icon fa fa-trash-o bigger-120"></i>删除';
        html += '</a>';
    }
    if (bChild) {
        html += '<a href="javascript:;" class="btn btn-xs btn-primary" title="添加下级">';
        html += '<i class="ace-icon fa fa-plus bigger-120"></i>添加下级';
        html += '</a>';
    }
    if (bAdd) {
        html += '<a href="javascript:;" class="btn btn-xs btn-primary" title="添加下级">';
        html += '<i class="ace-icon fa fa-plus bigger-120"></i>添加键值';
        html += '</a>';
    }

    html += '</div>';

    return html;
}