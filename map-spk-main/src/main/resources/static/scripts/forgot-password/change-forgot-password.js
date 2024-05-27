const base_url = window.location.origin + "/spk-app";
const forgotPasswordKey = new URLSearchParams(window.location.search).get('forgotPasswordKey');
const changeForgotPasswordUrl = base_url + "/api/forgot-account-or-password?forgotPasswordKey="+forgotPasswordKey;
const loginUrl = base_url + "/auth/login";

$('#viewNewPassword').click(function() {

    $(this).toggleClass("fa-eye fa-eye-slash");
    let input = $('#newPassword');
    if (input.attr("type") == "password") {
        input.attr("type", "text");
    } else {
        input.attr("type", "password");
    }
});

$('#viewConfirmPassword').click(function() {

    $(this).toggleClass("fa-eye fa-eye-slash");
    let input = $('#confirmPassword');
    if (input.attr("type") == "password") {
        input.attr("type", "text");
    } else {
        input.attr("type", "password");
    }
});

function onChangePassword(){

    let newPassword = document.getElementById("newPassword").value;
    let confirmPassword = document.getElementById("confirmPassword").value;
    let json = genChangePasswordJson(newPassword, confirmPassword);

    showLoadingSwal();
    console.log(json);

    $.ajax({
        type: "PATCH",
        url: changeForgotPasswordUrl,
        data: json,
        contentType: "application/json",
        dataType: "json",
        success: function (data){
            if (data.responseCode === 1031) {
                swalSuccess(data.responseMessage);
            } else {
                swalError(data.responseMessage);
            }
        },
        error: function (err) {
            swalError(err.message);
        }
    });

    return false;
}

function swalSuccess(message){

    Swal.fire({
        title: 'Success',
        type: "success",
        text: message,
        allowOutsideClick: false,
        confirmButtonText: 'Go To Login'
    }).then((result) =>{
        if (result.value){
            location.href = loginUrl;
        }
    });
}

function swalError(message){
    Swal.fire({
        title: 'Something went wrong',
        type: "error",
        text: message
    });
}

function genChangePasswordJson(newPassword, confirmPassword){

    return '{'
        + '"newPassword": "'+newPassword+'",'
        + '"confirmPassword": "'+confirmPassword+'"'
        +'}';

}

function showLoadingSwal(){
    Swal.fire({
        title: 'Processing',
        text: 'Please wait while changing password...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}
