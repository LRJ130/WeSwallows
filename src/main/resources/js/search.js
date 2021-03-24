var substance = localStorage.getItem('substance');
localStorage.removeItem("substance");


function showarticle() {
    location.href = "comment.html";
    window.localStorage.setItem('articlename', 'haha');
}

function turn() {
    if (0) { //判断条件：如果文章名等于用户名令牌
        // window.location.href = "mine.html";
        location.href = "mine.html";
        window.localStorage.setItem('user', 'haha');
    } else //如果未登录或者不是自己的文章
    {

        location.href = "others.html";
        window.localStorage.setItem('otherusers', 'haha');
    }
}

window.onload = function() {
    $.ajax({
        url: "",
        data: { "substance": substance },
        type: "POST",
        success: function(data) {
            console.log(data);
            if (i == 0) {
                var a = '<div style="background-color: rgb(224, 225, 226); width: 100%"><h1 style="text-align: center; color: rgb(90, 90, 90)">啥也没找到~</h1></div>';
                $("#firstcolumn").append(a);
            } //i为检索到的数量
            else {
                for (var i = 0; i < 6; i++) {
                    var result = template("template", data.item);
                    console.log(result);
                    $(".first").append(result);
                }
            }
        },
        error: function() {
            alert("请求失败");
        },
    });
}