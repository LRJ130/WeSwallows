function showaboutus() {
    $('.ui.first.modal')
        .modal("show ");
}

function showarticle() {
    location.href = "comment.html";
    window.localStorage.setItem('articlename', 'haha');
}

function showedit() {
    window.location.href = "editor.html";
}

function editmine() {
    window.location.href = "editmine.html";
}

function showsearch() {
    window.location.href = "classify.html";
}

function shownews() {
    window.location.href = "message.html";
}

function logout() {
    window.location.href = "homepage.html";
    localStorage.clear();
}

function payattention() {

}
window.onload = function() {
    var names = localStorage.getItem('user');
    console.log(names);

    $.ajax({
        url: '',
        data: {
            user: names //向后端发送名字作为索引标签，接受返回的内容
        },
        type: "POST",
        success: function() {
            document.getElementById("avatar").src = "images/3.jpg";
            document.getElementById("like").innerHTML = '<i class="heart icon"></i>' + "3";
            document.getElementById("comment").innerHTML = '<i class="comment icon"></i>' + "3";
            document.getElementById("article").innerHTML = '<i class="file icon"></i>' + "3";
            document.getElementById("activity").innerHTML = '<i class="certificate icon"></i>' + "3";
            document.getElementById("ranking").innerHTML = '<i class="flag icon"></i>' + "3";
            document.getElementById("watch").innerHTML = '<i class="eye icon"></i>' + "3";
            if (i == 0) {
                var a = '<div style="background-color: rgb(224, 225, 226); width: 100%"><h1 style="text-align: center; color: rgb(90, 90, 90)">啥也没找到~</h1></div>';
                $("#s10").append(a);
            } else {
                for (var i = 0; i < 6; i++) {
                    var result = template('template');
                    console.log(result);
                    $('.s10').append(result);
                }
            }
        },
        error: function() {
            alert('请求失败')
        }
    })
}