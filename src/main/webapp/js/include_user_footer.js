$(function () {
    $.get("user_footer.html",function (data) {
        $("#user_footer").html(data);
    });
});