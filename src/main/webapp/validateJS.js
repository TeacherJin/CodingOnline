//该函数用于判断两次输入的密码是否相同
function validate() {
    var studentPassword1 = document.getElementById("studentPassword1").value;
    var studentPassword2 = document.getElementById("studentPassword2").value;
    if (studentPassword1 == studentPassword2) {
        document.getElementById("prompt").innerHTML = "<font color='green'>两次密码相同</font>";
        document.getElementById("submit").disabled = false;
    } else {
        document.getElementById("prompt").innerHTML = "<font color='red'>两次密码不相同</font>";
        document.getElementById("submit").disabled = true;
    }
}
