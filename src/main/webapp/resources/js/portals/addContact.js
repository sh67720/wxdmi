/**
 * Created by shanghai on 2017/10/27.
 */
contact_commit = function () {
    var clientCompanyName = document.getElementById("mcompany").value;
    var clientName = document.getElementById("mname").value;
    var clientTel = document.getElementById("mtel").value;
    var clientCity = document.getElementById("mcity").value;
    var requirement = document.getElementById("mcons").value;
    if(clientCompanyName == "" || clientCompanyName == undefined || clientCompanyName.trim().length ==0){
        alert("请填写您的公司名称！")
        return false;
    }
    if(clientName == "" || clientName == undefined || clientName.trim().length ==0){
        alert("请填写您的姓名！");
        return false;
    }
    if(clientTel == "" || clientTel == undefined || clientTel.trim().length ==0){
        alert("请填写您的联系电话！")
        return false;
    }
    if(clientCity == "" || clientCity == undefined || clientCity.trim().length ==0){
        alert("请填写您的所在城市！")
        return false;
    }
    if(requirement == "" || requirement == undefined || requirement.trim().length ==0){
        alert("请填写您的项目需求！")
        return false;
    }
    alert("恭喜您提交成功！")
    return true;
}