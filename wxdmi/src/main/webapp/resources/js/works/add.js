/**
 * Created by user on 2017/11/2.
 */
worksadd_commit = function() {
    var name = $("#name").val();
    var serviceContent = $("#serviceContent").val();
    var industryTypes = $("#industryTypes").val();
    var categoryTypes = $("#categoryTypes").val();
    var brandIntroduction = $("#brandIntroduction").val();
    var remark = $("#remark").val();
    var inHomepage = $('[name="inHomepage"]:checked').val();

    if(name == "" || name == undefined || name.trim().length ==0){
        alert("名称不能为空");
        return false;
    }
    if(serviceContent == "" || serviceContent == undefined || serviceContent.trim().length ==0){
        alert("服务内容不能为空");
        return false;
    }
    if(categoryTypes == "" || categoryTypes == undefined){
        alert("类别不能为空");
        return false;
    }
    if(industryTypes == "" || industryTypes == undefined){
        alert("所属行业不能为空");
        return false;
    }

    var data = {
        name : name,
        serviceContent : serviceContent,
        industry : industryTypes,
        category : categoryTypes,
        brandIntroduction : brandIntroduction,
        remark : remark,
        homepageShow : inHomepage
    };

    $.ajax({
        url: common.getUrl("/works/add"),
        type: "POST",
        async: false,
        data: data,
        success:function(msg) {
            alert("添加成功");
            top.location.href = common.getUrl("/works/list");
        },
        error:function () {
            alert("添加异常");
        }
    });
}

worksedit_commit = function(id) {
    var name = $("#name").val();
    var serviceContent = $("#serviceContent").val();
    var industryTypes = $("#industryTypes").val();
    var categoryTypes = $("#categoryTypes").val();
    var brandIntroduction = $("#brandIntroduction").val();
    var remark = $("#remark").val();
    var inHomepage = $('[name="inHomepage"]:checked').val();

    if(name == "" || name == undefined || name.trim().length ==0){
        alert("名称不能为空");
        return false;
    }
    if(serviceContent == "" || serviceContent == undefined || serviceContent.trim().length ==0){
        alert("服务内容不能为空");
        return false;
    }
    if(categoryTypes == "" || categoryTypes == undefined){
        alert("类别不能为空");
        return false;
    }
    if(industryTypes == "" || industryTypes == undefined){
        alert("所属行业不能为空");
        return false;
    }

    var data = {
        id : id,
        name : name,
        serviceContent : serviceContent,
        industry : industryTypes,
        category : categoryTypes,
        brandIntroduction : brandIntroduction,
        remark : remark,
        homepageShow : inHomepage
    };

    $.ajax({
        url: common.getUrl("/works/edit"),
        type: "POST",
        async: false,
        data: data,
        success:function(msg) {
            alert("修改成功");
            top.location.href = common.getUrl("/works/list");
        },
        error:function () {
            alert("修改异常");
        }
    });
}
