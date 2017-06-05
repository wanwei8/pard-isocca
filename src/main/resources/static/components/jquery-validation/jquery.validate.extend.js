(function (factory) {
    if (typeof define === "function" && define.amd) {
        define(["jquery", "../jquery.validate"], factory);
    } else if (typeof module === "object" && module.exports) {
        module.exports = factory(require("jquery"));
    } else {
        factory(jQuery);
    }
}(function ($) {

    /*
     * Translated default messages for the jQuery validation plugin.
     * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
     */
    $.extend($.validator.messages, {
        required: "这是必填字段",
        remote: "请修正此字段",
        email: "请输入有效的电子邮件地址",
        url: "请输入有效的网址",
        date: "请输入有效的日期",
        dateISO: "请输入有效的日期 (YYYY-MM-DD)",
        number: "请输入有效的数字",
        digits: "只能输入数字",
        creditcard: "请输入有效的信用卡号码",
        equalTo: "你的输入不相同",
        extension: "请输入有效的后缀",
        maxlength: $.validator.format("最多可以输入 {0} 个字符"),
        minlength: $.validator.format("最少要输入 {0} 个字符"),
        rangelength: $.validator.format("请输入长度在 {0} 到 {1} 之间的字符串"),
        range: $.validator.format("请输入范围在 {0} 到 {1} 之间的数值"),
        max: $.validator.format("请输入不大于 {0} 的数值"),
        min: $.validator.format("请输入不小于 {0} 的数值"),
        integer: "只能输入整数",
    });

    jQuery.validator.setDefaults({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        errorPlacement: function (error, element) {
            if (element.is('input[input=checkbox]') || element.is('input[type=radio]')) {
                var controls = element.closest('div[class*="col-"]');
                if (controls.find(':checkbox,:radio').length > 1) controls.append(error);
                else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
            } else if (element.is('.select2')) {
                error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
            } else if (element.is('.chosen-select')) {
                error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
            } else {
                error.insertAfter(element.parent());
            }

        },
        highlight: function (element) {
            var formGroup = $(element).closest('.form-group');
            formGroup.removeClass('has-info').addClass('has-error has-feedback');
            if (formGroup.find('.form-control') > 0 && formGroup.find('span.form-control-feedback').length == 0) {
                $(element).after('<span class="ace-icon glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>');
            }
        },
        success: function (element) {
            var formGroup = $(element).closest('.form-group');
            var ele = formGroup.find('span.form-control-feedback');
            ele.remove();
            formGroup.removeClass('has-error');
            $(element).remove();
            // el.after('<span class="ace-icon glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>');
            // label.closest('.form-group').removeClass('has-error').addClass('has-feedback has-success');
            // label.remove();
        }
    });

    jQuery.validator.addMethod("abc", function (value, element) {
        var chrnum = /^([a-zA-Z0-9_-]+)$/;
        return this.optional(element) || (chrnum.test(value));
    }, "只能输入字母、数字、下划线");

    return $;
}));


