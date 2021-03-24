window.onload = function() { //四类消息：点赞文章，点赞评论和评论评论，点赞评论;其他：添加好友，好友动态，与我相关

    $.ajax({
        url: '',
        data: {

        },
        type: "get",
        success: function(tag) {
            if (tag == '1') { $('.feed').append('<div class="s4 event"><div class="label"><img src="' + touxiang + '"></div><div class="content"><div class="summary"><a class="user">' + youhuming + '</a>赞了你的内容<a class="user"herf="">' + neirong + '</a><div class="date">' + shijian + '</div></div></div></div>'); }
            if (tag == '2') { $('.feed').append('<div class="s4 event"><div class="label"><img src="' + touxiang + '"></div><div class="content"><div class="summary"><a class="user">' + youhuming + '</a>评论了你的内容<a class="user"herf="">' + neirong + '</a><div class="date">' + shijian + '</div></div></div></div>'); }
            if (tag == '2') { $('.feed').append('<div class="s4 event"><div class="label"><img src="' + touxiang + '"></div><div class="content"><div class="summary"><a class="user">' + youhuming + '</a>赞了你的评论<div class="date">' + shijian + '</div></div></div></div>'); }
            if (tag == '2') { $('.feed').append('<div class="s4 event"><div class="label"><img src="' + touxiang + '"></div><div class="content"><div class="summary"><a class="user">' + youhuming + '</a>赞了你的内容<div class="date">' + shijian + '</div></div></div></div>'); }
        },
        error: function() {
            alert('请求失败')
        }
    })
}