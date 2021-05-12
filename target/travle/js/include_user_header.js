function pageName() {
    var a = location.href;
    var b = a.split("/");
    var c = b.slice(b.length-1, b.length).toString(String).split(".");
    return c.slice(0, 1);
}

$(function () {
    $.get("user_header.html",function (data) {
        $("#user_header").html(data);
        $("#"+pageName()).addClass("active");
    });
});
