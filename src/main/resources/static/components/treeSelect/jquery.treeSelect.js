!(function ($, window, document, undefined) {

    var htmls = {
        'inputGroup': '<div class="input-group"></div>',
        'input': '<input type="text" data-msg-required="" class="form-control"/>',
        'inputButton': '<span class="input-group-btn"><button type="button" class="btn btn-sm btn-success"><i class="fa fa-search"></i></button></span>',
        'labelError': ' <label class="error" for="${id}Name" style="display:none"></label>',
        'palert': '<div class="p-alert"><button id="btn-ok">确定</button></div>',
        'pconfim': '<div class="p-confim"><button class="btn-ok">确定</button> <button class="btn-cancel">取消</button></div>'
    };

    //私有  默认变量
    var myPluginName = "treeSelect",
        defaults = {
            id: "parent",
            name: "parent.id",
            value: "",
            labelName: "parent.name",
            labelValue: "",
            parentId: "",
            title: "",
            adminUrl: "",
            baseUrl: "/tag/treeSelect",
            url: "",
            divCss: "",
            inputCss: "",
            inputStyle: "",
            placeholder: "",
            allowInput: false,
            allowClear: false,
            disabled: false,
            hideBtn: false,
            model: "",
            checked: false,
            extId: "",
            isAll: false,
            notAllowSelectParent: false,
            notAllowSelectRoot: false,
            selectScopeModel: false,
        };
    var btns = new Array;
    var uri = '';
    //私有 构造插件的函数
    var ctorPlugin = function (element, options) {
        this.$element = $(element);
        this.settings = $.extend({}, defaults, options);
        this.name = myPluginName;
        this.$parent = $(element.closest('div'));
        this.init();
        this.$input = $(htmls.input);
        this.$inputBtn = $(htmls.inputButton);
        this.$uri = '';
        this.createdom(element);
        this.clickmethod();
    };
    //原型链上添加方法

    ctorPlugin.prototype = {
        init: function () {
            uri = this.settings.adminUrl + this.settings.baseUrl +
                "?url=" + encodeURIComponent(this.settings.url);
            btns.push('确定');
            btns.push('关闭');
            if (this.settings.allowClear) {
                btns.push('清除');
            }
            this.$element.val(this.settings.value);
        },
        createdom: function (ele) {
            this.$element.attr('type', 'hidden');
            var $inputGroup = $(htmls.inputGroup);
            if (this.settings.divCss.length > 0)
                $inputGroup.addClass(this.settings.divCss);
            this.$input.attr('id', this.settings.id + 'Name')
                .attr('name', this.settings.labelName)
                .addClass(this.settings.inputCss)
                .attr('placeholder', this.settings.placeholder)
                .attr('value', this.settings.labelValue)
                .attr('style', this.settings.inputStyle);
            if (!this.settings.allowInput)
                this.$input.attr('readonly', 'readonly');
            this.$inputBtn.attr('id', this.settings.id + 'Btn');
            if (this.settings.disabled) {
                this.$inputBtn.addClass('disabled');
            }
            if (this.settings.hideBtn) {
                this.$inputBtn.addClass('hide')
            }
            var $labelErr = $(htmls.labelError).attr('id', this.settings.id + 'Name-error')
                .attr('for', this.settings.id + "Name");
            $inputGroup.append(this.$input).append(this.$inputBtn).append($labelErr);
            this.$parent.append($inputGroup);
        },
        clickmethod: function () {
            var that = this;
            that.$input.on('click', function () {
                that.show();
            });
            that.$inputBtn.on('click', function () {
                that.show();
            });
        },
        show: function () {
            var that = this;
            if (that.$inputBtn.hasClass("disabled")) {
                return true;
            }
            var parentId = that.$element.val() || '';
            uri = uri + "&model=" + that.settings.model
                + "&checked=" + that.settings.checked
                + "&extId=" + that.settings.extId
                + "&parent=" + parentId
                + "&selectIds=" + that.$element.val()
                + "&isAll=" + that.settings.isAll;

            top.layer.open({
                type: 2,
                area: ['300px', '420px'],
                title: "选择" + that.settings.title,
                ajaxData: {selectIds: parentId},
                content: uri,
                btn: btns
                , yes: function (index, layero) { //或者使用btn1
                    var tree = layero.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
                    var ids = [], names = [], nodes = [];
                    if (that.settings.checked) {
                        nodes = tree.getCheckedNodes(true);
                    } else {
                        nodes = tree.getSelectedNodes();
                    }
                    for (var i = 0; i < nodes.length; i++) {
                        if (that.settings.checked && that.settings.notAllowSelectParent) {
                            if (nodes[i].isParent) {
                                continue; // 如果为复选框选择，则过滤掉父节点
                            }
                        }
                        if (that.settings.notAllowSelectRoot && nodes[i].level == 0) {
                            top.layer.msg("不能选择根节点（" + nodes[i].name + "）请重新选择。");
                            return false;
                        }
                        if (that.settings.notAllowSelectParent && nodes[i].isParent) {
                            top.layer.msg("不能选择父节点（" + nodes[i].name + "）请重新选择。");
                            return false;
                        }
                        if (that.settings.model.length > 0 && that.settings.selectScopeModel) {
                            if (nodes[i].model == "") {
                                top.layer.msg("不能选择公共模型（" + nodes[i].name + "）请重新选择。");
                                return false;
                            } else if (nodes[i].model != that.settings.model) {
                                top.layer.msg("不能选择当前栏目以外的栏目模型，请重新选择。");
                                return false;
                            }
                        }
                        ids.push(nodes[i].id);
                        names.push(nodes[i].name);//
                        break; // 如果为非复选框选择，则返回第一个选择
                    }
                    that.$element.val(ids.join(",").replace(/u_/ig, "")).change();
                    that.$input.val(names.join(","));
                    that.$input.focus();
                    top.layer.close(index);
                }, cancel: function (index) { //或者使用btn2
                    //按钮【按钮二】的回调
                }, btn3: function (index, layero) {
                    //按钮【按钮三】的回调
                    that.$element.val("");
                    that.$input.val("");
                    top.layer.close(index);
                }
            });
        }
    };


    //将插件包装起来，利用模块模式，防止多次实例出现

    $.fn[myPluginName] = function (options) {
        return this.each(function () {
            var myPluginNameStr = "plugin_" + myPluginName;

            if (!$.data(this, myPluginNameStr)) {

                $.data(this, myPluginNameStr, new ctorPlugin(this, options));
            }
        });
    };

})(jQuery, window, document);