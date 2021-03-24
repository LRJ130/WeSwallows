var tag = 1;

function check() {
    var a = document.getElementById("password").value;
    var b = document.getElementById("surepassword").value;
    if (a != b) {
        alert("两次密码不相同");
        tag = 0;
    } else { tag = 1; }
    /* if (a == b) {
        var c = document.getElementById("uiformsuccess");
        c.style.display = "block";
        var d = document.getElementById("uiformerror");
        c.style.display = "none";
    } else {
        var c = document.getElementById("uiformsuccess");
        c.style.display = "none";
        var d = document.getElementById("uiformerror");
        d.style.display = "block";
    } */
}

function getback() {
    window.location.href = "mine.html";
}

function sure() {
    if (tag == 0) { alert("两次密码不相同"); } else {
        var a = $("#username").val();
        var b = $("#signature").val();
        var c = $("#password").val();
        var d = $("#contect").val();
        var dd = $(".contect.dropdown").dropdown("get value");
        var e = $(".sex.dropdown").dropdown("get value");
        var f = $(".academy.dropdown").dropdown("get value");
        var g = $(".grade.dropdown").dropdown("get value");

        $.ajax({
            url: '',
            data: {

            },
            type: "POST",
            success: function() {
                $('.basic.modal')
                    .modal('setting', 'closable', false)
                    .modal('show');
            },
            error: function() {
                alert('请求失败')
            }
        })
    }
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