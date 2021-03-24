var tagname = localStorage.getItem('tagname');

function turnmine() {
    var JWT = window.localStorage.getItem('JWT');
    if (JWT == 'Y') { window.location.href = "mine.html"; } else { alert('请先登录'); }
}
window.onload = function() {
    if (tagname == null) { //没有点击标签就默认加载六条
        $.ajax({
            url: '',
            data: {

            },
            type: "get",
            success: function(data) {
                console.log(data);

                for (var i = 0; i < 6; i++) {
                    var result = template('templatetwo', data.item);
                    console.log(result);
                    $('.results').append(result);
                }

            },
            error: function() {
                alert('请求失败')
            }
        })
    } else {
        $.ajax({
            url: '',
            data: {
                "tagname": tagname
            },
            type: "POST",
            success: function(data) { //返回这个标签的数量，全部加载完
                console.log(data);

                for (var i = 0; i < 6; i++) {
                    var result = template('templatetwo', data.item);
                    console.log(result);
                    $('.results').append(result);
                }

            },
            error: function() {
                alert('请求失败')
            }
        })
        localStorage.removeItem("tagname");
    }
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

function showarticle() {
    location.href = "comment.html";
    window.localStorage.setItem('articlename', 'haha');
}

function loading() {
    for (var i = 0; i < 6; i++) {
        var result = template('templatetwo');
        console.log(result);
        $('.results').append(result);
    }
    $.ajax({
        url: '',
        data: {

        },
        success: function(data) {
            console.log(data);
            for (var i = 0; i < 6; i++) {
                var result = template('templatetwo');
                console.log(result);
                $('.results').append(result);
            }

        },
        error: function() {
            alert('请求失败')
        }
    })
}