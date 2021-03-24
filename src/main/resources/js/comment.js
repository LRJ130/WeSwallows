var articlename = localStorage.getItem('articlename'); //得到这篇文章的主人
localStorage.removeItem("articlename");
var JWT = localStorage.getItem('JWT');
var user = localStorage.getItem('user');
window.onload = function() {
        $.ajax({
            url: '',
            data: {},
            success: function(data) { //嵌入作者内容
                document.getElementById('authorname').innerHTML = "bajcbja";
                document.getElementById("authoravatar").innerHTML = "images/youxiang4.jpg"; //插入图片不行
                document.getElementById("authordate").innerHTML = "12:50";
                document.getElementById("authorstyle").innerHTML = "hezhishiyigechuanshuo";
                document.getElementById("authorsays").innerHTML = "hezhishiyigechuanshuo";
                //嵌入评论
                if (pinglunshu == "0") {
                    var comeanddiscuss = "<div style='width: 100%;background-color: rgb(224, 225, 226)'><h4 style='text-align: center;color: rgb(90, 90, 90);'>抢沙发</h4></div>"
                    discuss.innerHTML += comeanddiscuss;
                } else {
                    //拼接字符串，不行模板引擎
                    if (JWT != 'Y') { //没登陆不显示功能区
                        for (var i = 0; i < item[i].length; i++) {

                            var discusses = "<div class='comment'><a class='avatar'><img src=" + discusses + "></a><div class='content'><a class='author'>" + Matt + "</a><div class='metadata'><span class='date'>" + 542 + "</span></div><div class='text'>" + Howartistic + "</div></div></div>"
                                // var discusses = "<div class='comment'><a class='avatar'><img src=" + discusses + "></a><div class='content'><a class='author'>" + Matt + "</a><div class='metadata'><span class='date'>" + 542 + "</span></div><div class='text'>" + Howartistic + "</div><div class='actions'></div></div></div>"
                            discuss.innerHTML += discusses;

                        }
                    } else { //登录的情况下
                        if (user == articlename) //查看是否是自己的文章
                        {
                            var button = "<button class='fluid ui button'style='margin-top:3%' οnclick='takeoff()'>删除</button>";
                            threeuibuttons.innerHTML += button;
                        }
                        for (var i = 0; i < item[i].length; i++) {
                            if (item中标记评论为自己的) {
                                var discusses = "<div class='comment'><a class='avatar'><img src=" + discusses + "></a><div class='content'><a class='author'>" + Matt + "</a><div class='metadata'><span class='date'>" + 542 + "</span></div><div class='text'>" + Howartistic + "</div><div class='actions'><a class='reply'onclick='love()'>赞(" + 3 + ")</a><a class='reply'onclick='hate()'>踩(" + 3 + ")</a><a class='reply'onclick='answer()'>回复</a><a class='reply'onclick='cutoff()'>删除</a></div></div></div>"
                                discuss.innerHTML += discusses;
                            } else { //item中的标记这条评论不是自己的
                                var discusses = "<div class='comment'><a class='avatar'><img src=" + discusses + "></a><div class='content'><a class='author'>" + Matt + "</a><div class='metadata'><span class='date'>" + 542 + "</span></div><div class='text'>" + Howartistic + "</div><div class='actions'><a class='reply'onclick='love()'>赞(" + 3 + ")</a><a class='reply'onclick='hate()'>踩(" + 3 + ")</a><a class='reply'onclick='answer()'>回复</a></div></div></div>"
                                discuss.innerHTML += discusses;
                            }
                        }
                    }
                }

            },
            error: function() {
                alert('请求失败')
            }
        });

    }
    /* 配置四个按钮的功能 */
function like() { //向后端发送点击信号，后端由Boolean判断是加是减，完成后返回具体数量
    $.ajax({
        url: '',
        data: {
            用户名: user, //发送一个用户名，后端自动检索其他信息
            内容的id: ''
        },
        type: 'POST',
        success: function(data) { //返回点赞数
            $('.id.like').innerText = "赞(" + data + ")";
        },
        error: function() {
            alert('请求失败')
        }
    })
}

function disliake() {
    $.ajax({
        url: '',
        data: {
            用户名: user, //发送一个用户名，后端自动检索其他信息
            内容的id: ''
        },
        type: 'POST',
        success: function(data) { //返回点赞数
            $('.id.dislike').innerText = "踩(" + data + ")";
        },
        error: function() {
            alert('请求失败')
        }
    })
}

function reply() { //回复这篇文章的主人
    var texts = document.getElementById('textarea').value;
    var discusses = "<div class='comment'><a class='avatar'><img src='images/youxiang.jpg'></a><div class='content'><a class='author'>" + texts + "</a><div class='metadata'><span class='date'>" + texts + "</span></div><div class='text'>回复" + articlename + texts + "</div><div class='actions'><a class='reply' onclick='love()'>赞(" + texts + ")</a><a class='reply'onclick='hate()'>踩(" + texts + ")</a><a class='reply'onclick='answer()'>回复</a><a class='reply'onclick='cutoff()'>删除</a></div></div></div>"
    discuss.innerHTML += discusses;
    var date = new Date();
    var year = date.getFullYear(); //获取当前年份
    var mon = date.getMonth() + 1; //获取当前月份
    var day = date.getDate(); //获取当前日
    var h = date.getHours(); //获取小时
    console.log(h);
    var m = date.getMinutes(); //获取分钟
    var time =
        "<p>" +
        year +
        "年" +
        mon +
        "月" +
        day +
        "日" +
        h +
        "时" +
        m +
        "分</p>";
    $.ajax({ //传入所有用户信息，如果只传一个后端检索出来也行
        url: '',
        data: {
            问题的id: '',
            头像: '',
            昵称: '',
            时间: time,
            内容: texts,
        },
        success: function(data) { //返回这个问题的id，用户名
            alert('回复成功')
        },
        error: function() {
            alert('请求失败')
        }
    })
}


function takeoff() {
    $.ajax({
        url: '',
        data: {
            问题的id: ''
        },
        success: function(data) {
            alert('删除成功');
            window.history.go(-1);
        },
        error: function() {
            alert('请求失败')
        }
    })
}
/* 配置每个评论按钮的功能 */
function love() {
    $.ajax({
        url: '',
        data: {
            用户名: user, //发送一个用户名，后端自动检索其他信息
            内容的id: ''
        },
        type: 'POST', //有后端来判断是奇数次还是偶数次点赞
        success: function(data) { //返回点赞数
            $('.id.love').innerText = "赞(" + data + ")";
        },
        error: function() {
            alert('请求失败')
        }
    })
}

function hate() {
    $.ajax({
        url: '',
        data: {
            用户名: user, //发送一个用户名，后端自动检索其他信息
            内容的id: ''
        },
        type: 'POST',
        success: function(data) { //返回点赞数
            $('.id.hate').innerText = "踩(" + data + ")";
        },
        error: function() {
            alert('请求失败')
        }
    })
}


function answer() {
    var commentname = '';

}

function cutoff() {
    alert('?')
    $.ajax({
        url: '',
        data: {
            问题的id: ''
        },
        success: function(data) {
            alert('删除成功')
        },
        error: function() {
            alert('请求失败')
        }
    })
}

layui.use('upload', function() {
    var upload = layui.upload;
    var uploadInst = upload.render({
        elem: '#imagesupload',
        url: 'https://httpbin.org/post', //改成您自己的上传接口
        multiple: true,
        before: function(obj) {
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result) {
                $('#viewimages').append('<img src="' + result + '" alt="' + file.name + '" class="layui-upload-img">')
            });
        },
        done: function(res) {
            //上传完毕回调
        },
        error: function() {
            //请求异常回调
        }
    });
});