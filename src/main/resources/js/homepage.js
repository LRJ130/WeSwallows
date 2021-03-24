var tag = 1; //判断密码正误
// location.href = "mine.html";
// window.localStorage.setItem('user', 'haha');
window.onload = function() {
    $.ajax({
        url: '',
        data: {

        },
        type: "get",
        success: function(data) {
            console.log(data);
            /* 添加主页内容 */
            for (var i = 0; i < 6; i++) {
                var result = template('template', data.item);
                console.log(result);
                $('.clearfix').append(result);
            }
            /* 添加热门推荐 */
            document.getElementById(rightfloatedmeta).innerHTML = data;
            document.getElementById(uiavatarimage).innerHTML = data;
            document.getElementById(blurringdimmableimage).innerHTML = data;
            document.getElementById(tageone).innerHTML = data;
            document.getElementById(tagetwo).innerHTML = data;
            document.getElementById(like).innerHTML = data;
            /* 添加热门标签 */
            var a = '<a class=" ui tag label" onclick="showresults()">' + data + '</a>';
            for (var i = 0; i < 6; i++) { $('#labels').append(a); }
        },
        error: function() {
            alert('请求失败')
        }
    })
}

function turnmine() {
    var JWT = window.localStorage.getItem('JWT');
    if (JWT == 'Y') { window.location.href = "mine.html"; } else { alert('请先登录'); }
}

function showedit() {
    window.location.href = "editor.html";
}

function showaboutus() {

    $('.ui.first.basic.modal')
        .modal("show ");
}

function showresults() {
    location.href = "classify.html";
    window.localStorage.setItem('tagname', 'haha');
}

function check() {
    var a = document.getElementById("password").value;
    var b = document.getElementById("surepassword").value;
    if (a != b) {
        alert("两次密码不相同");
        tag = 0;
    } else { tag = 1; }
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
// window.localStorage.setItem('JWT', 'haha');
function showarticle() {
    location.href = "comment.html";
    window.localStorage.setItem('articlename', 'haha');
}