//图片点击事件
function changeCheckCode() {
    $("#imgCode")[0].src="user/getCheckCode?time="+new Date().getTime()+'&&email='+ $("#email").val();
}

function checkUserName() {
    //1.获取用户名值
    var username = $("#userName").val();
    //2.定义正则
    var reg_username = /^\w{8,16}$/;

    //3.判断，给出提示信息
    var flag = reg_username.test(username);
    if(flag){
        //用户名合法
        $("#userName").css("border","");
    }
    else{
        //用户名非法,加一个红色边框
        $("#userName").css("border","1px solid red");
    }

    return flag;
}

function checkPassword(){
    //1.获取用户名值
    var password = $("#password").val();
    //2.定义正则
    var reg_password = /^\w{8,16}$/;

    //3.判断，给出提示信息
    var flag = reg_password.test(password);
    if(flag){
        //用户名合法
        $("#password").css("border","");
    }
    else{
        //用户名非法,加一个红色边框
        $("#password").css("border","1px solid red");
    }

    return flag;
}

function checkEmail() {
    //1.获取邮箱
    var email = $("#email").val();
    //2.定义正则
    var reg_email = /^\w+@\w+\.\w+$/;

    //3.判断
    var flag = reg_email.test(email);
    if(flag){
        $("#email").css("border","");
        $("#btnImg").click(changeCheckCode);
    }
    else{
        $("#email").css("border","1px solid red");
        $("#btnImg").unbind("click");
    }

    return flag;
}

function checkCheckcode(){
    var checkcode = $("#checkcode").val();
    var flag = false;

    if (checkcode == null){
        flag = false;
    }
    else{
        flag = true;
    }

    return flag;
}

$(function(){
    $("#registe").submit(function () {
        if (checkUserName() && checkPassword() && checkEmail() && checkCheckcode()){
            $.post("user/signUp",$(this).serialize(),function (data) {
                if (data.flag){
                    alert("注册成功");
                    location.href = "user_sign_in.html";
                }
                else{
                    alert(data.errorMsg);
                }
            });
        }
        return false;
    });

    $("#userName").blur(checkUserName);
    $("#password").blur(checkPassword);
    $("#email").blur(checkEmail);
    $("#activecode").blur(checkCheckcode);
});
