
let isFormValid = true;


function kiemTraEmail() {
    let email = $("#txtEmail").val();
    let pattern = /^[a-zA-Z0-9]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    if (pattern.test(email)) {
        $("#tbEmail").html("");
        return true;
    }
    else {
        $("#tbEmail").html("Email không chứ ký tự đặc biệt");
        return false;
    }
}
$("#txtEmail").blur(function (e) {
    kiemTraEmail();

});
function kiemTraTaiKhoan() {
    let tk = $("#txtAcc").val();
    let pattern = /^[a-zA-Z0-9]+$/;
    if (pattern.test(tk)) {
        $("#tbAcc").html("");
        return true;
    }
    else {
        $("#tbAcc").html("Tài khoản không chứa ký tự đặc biệt");
        return false;
    }
}
$("#txtAcc").blur(function (e) {
    kiemTraTaiKhoan();

});
function kiemTraMatKhau() {
    let pwd = $("#txtPwd").val();
    let pattern = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.])[A-Za-z\d@$!%*?&.]{8,}$/;
    if (pattern.test(pwd)) {
        $("#tbPwd").html("");
        return true;
    }
    else {
        $("#tbPwd").html("Mật khẩu phải chứa ít nhất 1 ký tự in hoa, 1 ký tự số, 1 ký tự đặc biệt và có ít nhất 8 ký tự");
        return false;
    }
}
$("#txtPwd").blur(function (e) {
    kiemTraMatKhau();
});
function kiemTraMatKhauNhapLai() {
    let pwd2 = $("#txtPwd2").val();
    let pwd = $("#txtPwd").val();
    if (pwd2 == pwd) {
        $("#tbPwd2").html("");
        return true;
    }
    else {
        $("#tbPwd2").html("Mật khẩu nhập lại không khớp!");
        return false;
    }
}
$("#txtPwd2").blur(function (e) {
    kiemTraMatKhauNhapLai();

});
function kiemTraNgaySinh() {
    let ns_val = $("#txtDate").val();
    let day, month, year;
    let ns = new Date(ns_val);
    day = ns.getDate();
    month = ns.getMonth() + 1;
    year = ns.getFullYear();
    let ns_String;
    if (month < 10) {
        if (day < 10)
            ns_String = "0" + day + "/" + "0" + month + "/" + year;
        else
            ns_String = day + "/" + "0" + month + "/" + year;
    }
    else
        if (day < 10)
            ns_String = "0" + day + "/" + month + "/" + year;
        else
            ns_String = day + "/" + month + "/" + year;
    // alert(ns_String);
    let pattern = /^(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\d\d$/;
    if (pattern.test(ns_String)) {
        $("#tbDate").html("");
        return true;
    }
    else {
        $("#tbDate").html("Ngày sinh không hợp lệ!");
        return false;
    }
}
$("#txtDate").blur(function (e) {
    kiemTraNgaySinh();

});
function kiemTraSDT(){
    let sdt = $("#txtPhone").val();
    let pattern = /^0[0-9]{9}$/;
    if(pattern.test(sdt)){
        $("#tbPhone").html("");
        return true;
    }
    else{
        $("#tbPhone").html("Số điện thoại không hợp lệ!");
        return false;
    }
}
$("#txtPhone").blur(function (e) { 
    kiemTraSDT();
    
});
function kiemTraDiaChi(){
    let dc = $("#txtAddress").val();
    let pattern = /^[a-zA-Z0-9\s,.'-]{3,100}$/;
    if(pattern.test(dc)){
        $("#tbAddress").html("");
        return true;
    }
    else{
        $("#tbAddress").html("Địa chỉ không hợp lệ!");
        return false;
    }
}
$("#txtAddress").blur(function (e) { 
    kiemTraDiaChi();
    
});


//$("#btndangky").click(function (e) { 
//   if(kiemTraEmail() && kiemTraTaiKhoan() && kiemTraMatKhau() && kiemTraMatKhauNhapLai() && kiemTraNgaySinh()
 //   && kiemTraSDT() && kiemTraDiaChi()){
//		console.log("ok");
//        showMessage();
//    }
//    else
//	    showError();
    
//});

$("#formRegister").change(function (e) {
		if(	kiemTraTaiKhoan() &&
		kiemTraMatKhau() &&
		kiemTraMatKhauNhapLai() &&
		kiemTraEmail() &&
		kiemTraNgaySinh() &&
		kiemTraSDT() &&
		kiemTraDiaChi()){
			$("#btndangky").prop("disabled", false);
		}else{
			$("#btndangky").prop("disabled", true);
		}	
});


function showMessage() {
    Swal.fire(
        'Đăng ký thành công',
        'Nhấn OK để tắt thông báo',
        'success'
      )
}
function showError() {
    Swal.fire(
        'Thông tin đăng ký chưa chính xác!',
        'Nhấn OK để tắt thông báo',
        'error'
      )
}
function openEye(class1, class2){
    $(class1).css("display", "inline-block");
    $(class2).css("display", "none");
}
function closeEye(class1, class2){
    $(class1).css("display", "none");
    $(class2).css("display", "inline-block");
}
function openClosePWD(id,class1, class2){
    let pwd = document.getElementById(id);   
    if(pwd.type == 'text'){
        pwd.type = 'password';
        openEye(class1,class2);
    }else{
        pwd.type = 'text';
        closeEye(class1,class2);
    }
}
$("#btn_eye").click(function (e) { 
    openClosePWD("txtPwd",".eyeO1",".eyeC1"); 
});
$("#btn_eye_2").click(function (e) { 
    openClosePWD("txtPwd2",".eyeO2",".eyeC2"); 
});

