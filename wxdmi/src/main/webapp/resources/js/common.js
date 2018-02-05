var common = {
    phoneRegexp : /^0{0,1}(13[0-9]|15[0-9]|18[0-9]|14[5|6|7]|17[0-9])[0-9]{8}$/i,
    infoDialogDivId : "#infoDialogTemplate",
    changePassDialogDivId : "#changePassDialog",
    menuUrl : "/backend/getMenus",
    changePassUrl : "/backend/changePass",
    showTime : 1000 * 30,
    getUrl:function(url){
        return baseUrl + url;
    },
    prevPage:function(){
        history.go(-1);
    }
};


/**
 * 初始化加载函数
 */
$(function(){
    $('.selectpicker').selectpicker({noneSelectedText:'请选择...'});
    $('.selectpicker').selectpicker('val',$('.selectpicker').attr('value'));

    //common.loadMenus();
    common.initChangePass();
    $('input, textarea').placeholder();
    $(common.infoDialogDivId).hide();
    $("div.message-place").fadeOut(common.showTime);

    common.screenSetting();

    $(".prevPage").unbind("click").bind("click", function(){
        common.prevPage();
    });
});


/*************************  begin private function *************************************/

/**
 * todo 时区不同, 减8个小时
 * todo datetimepicker使用
 * @param nS
 * @returns {string}
 */
common.getLocalTime = function(nS) {
    return new Date(nS - 1000 * 60 * 60 * 8);
};
common.initChangePass = function(){
    $('#changePassDialog').on('hidden.bs.modal', function () {
        $("#oldPassword").val("");
        $("#newPassword").val("");
        $("#confirmNewPassword").val("");
        $("#modalErrorMessageBlock").attr("class", "").html("");
    });

    $("#changePasswordBtn").click(function () {
        var oldPassword = $.trim($("#oldPassword").val());
        var newPassword = $.trim($("#newPassword").val());
        var confirmNewPassword = $.trim($("#confirmNewPassword").val());

        var params = {
            oldPassword: oldPassword,
            newPassword: newPassword,
            confirmNewPassword: confirmNewPassword
        };

        $.ajax({
            url: common.getUrl(common.changePassUrl),
            type: "POST",
            data: params,
            dataType: "json",
            timeout: 4000,
            success: function (data) {
                if (data && data.status) {
                    if (confirm("密码修改成功")) {
                        $('#changePassDialog').modal('hide');
                    }
                } else {
                    var $ul = $("<ul style='margin-top: 0;'></ul>");
                    var message = data.msg;
                    if (message == 'bean validation error') {
                        $ul.html(data.data);
                    } else {
                        $("<li></li>").text(message).appendTo($ul);
                    }

                    $("#modalErrorMessageBlock").addClass("alert alert-danger").html("").append($ul);
                }
            },
            error: function (result) {
                alert("error");
            }
        });
    });
}




/**
 * 显示修改密码框
 */
common.changePass = function(){
    $(common.changePassDialogDivId).modal('show');
}
/**
 * 公共校验form表单校验类
 * @param formId
 * @param fields
 * @param submitHandler
 */
common.formValidation = function(formId, fields, submitHandler){
    if(submitHandler == undefined){
        submitHandler = function(validator, form, submitButton) {
            $(form).submit();
        }
    }
    $(formId).bootstrapValidator({
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        submitHandler: submitHandler,
        fields: fields
    });
}

/**
 * 加载左侧菜单
 */
common.loadMenus = function(){
    if(needLoadMenus != undefined && needLoadMenus){
        $.ajax({
            url: common.getUrl(common.menuUrl),
            type: "GET",
            dataType: "json",
            success: function (data) {
                if(data) {
                    var navObj = $("#side-menu");
                    var currUrl = window.location.href;

                    $(data).each(function(){
                        var menuName = this.name;
                        var childs = this.childs;
                        var menuUrl = common.getUrl(this.url);
                        var $li = $("<li></li>");

                        if(childs && childs.length > 0){
                            var isActive = false;
                            $("<a></a>").attr({"href":"javascription:void(0);"})
                                .html('<i class=""></i> '+ menuName +'<span class="fa arrow"></span>').appendTo($li);
                            var $ul = $("<ul></ul>").attr("class", "nav nav-second-level");

                            $(childs).each(function(){
                                var $childLi = $("<li></li>");
                                var $childLiA = $("<a></a>").attr("href", common.getUrl(this.url)).text(this.name);

                                if (currUrl.search(this.url) != -1) {
                                    $childLiA.attr("class", "active");
                                    isActive = true;
                                }

                                $childLiA.appendTo($childLi);
                                $childLi.appendTo($ul);
                            });
                            if(isActive){
                                $li.attr("class", "active");
                            }
                            $ul.appendTo($li);
                            $li.appendTo(navObj);
                        }else{
                            var $tempA = $("<a></a>").attr("href", menuUrl).text(menuName);
                            if (currUrl.search(menuUrl) != -1) {
                                $tempA.attr("class", "active");
                            }
                            $tempA.appendTo($li);
                            $li.appendTo(navObj);
                        }
                    });
                    $('#side-menu').metisMenu();
                }
            }
        });
    }
}

/**
 * 调用公共info弹出框
 * @param url
 * @param obj
 */
common.dialog = function extracted(title, url) {
    $(common.infoDialogDivId + " .modal-title").html(title);
    $(common.infoDialogDivId + " .modal-body").empty();
    $(common.infoDialogDivId + " .modal-body").load(common.getUrl(url), function (responseTxt, statusTxt, xhr) {
        if (statusTxt == "error") {
            alert("服务端未响应！");
        }
    });
    $(common.infoDialogDivId).modal('show');
}
/*
*  Loads the correct sidebar on window load, collapses the sidebar on window resize.
*  Sets the min-height of #page-wrapper to window size
*/
common.screenSetting = function(){
    $(window).bind("load resize", function() {
        topOffset = 50;
        width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
        if (width < 768) {
            $('div.navbar-collapse').addClass('collapse');
            topOffset = 100; // 2-row-menu
        } else {
            $('div.navbar-collapse').removeClass('collapse');
        }

        height = ((this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height) - 1;
        height = height - topOffset;
        if (height < 1) height = 1;
        if (height > topOffset) {
            $("#page-wrapper").css("min-height", (height) + "px");
        }
    });

    var url = window.location;
    var element = $('ul.nav a').filter(function() {
        return this.href == url || url.href.indexOf(this.href) == 0;
    }).addClass('active').parent().parent().addClass('in').parent();
    if (element.is('li')) {
        element.addClass('active');
    }
}

/**
 * 调用公共info弹出框
 * @param url
 * @param obj
 */
common.htmlDialog = function extracted(title, html) {
    $(common.infoDialogDivId + " .modal-title").html(title);
    $(common.infoDialogDivId + " .modal-body").empty();
    $(common.infoDialogDivId + " .modal-body").html(html);
    $(common.infoDialogDivId).modal('show');
}
