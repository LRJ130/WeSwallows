function specific() {
    window.location.href = "comment.html";
}

window.onload = function() {
    $.ajax({
        url: '',
        data: {
            "otherusers": "" //向后端发送用户名字获取其他用户的主页
        },
        type: "get",
        success: function() {
            localStorage.removeItem("otherusers"); //成功之后删除这一条令牌
            for (var i = 0; i < 6; i++) {
                var result = template('template');
                console.log(result);
                $('.s6').append(result);
            }
        },
        error: function() {
            alert('请求失败')
        }
    })
}