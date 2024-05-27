const base_url = window.location.origin + "/spk-app";
const forgotPasswordUrl = base_url + "/api/forgot-account-or-password";
const loginUrl = base_url + "/auth/login";

function onSubmitBtnClick(){

    let email = document.getElementById("email").value;
    let json = genJson(email);

    showLoading();
    makeForgotPasswordRequest(json);

    return false;
}

function makeForgotPasswordRequest(json){

    $.ajax({
        type: "POST",
        url: forgotPasswordUrl,
        contentType: "application/json",
        dataType: "json",
        data: json,
        success: function(data){
            console.log(data);

            switch (data.responseCode) {
                case 1030: //success
                    swalSuccess(data.responseMessage);
                    break;
                case 4024: //user not found
                    swalUserNotFound(data.responseMessage);
                    break;
                default: //else
                    swalError(data.responseMessage);
                    break;
            }
        },
        error: function (err) {
            swalError(err.message);
        }
    });
}

function showLoading(){

    Swal.fire({
        title: 'Loading',
        text: 'Please wait while processing...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });

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

function swalUserNotFound(message){
    Swal.fire({
        title: 'User not found',
        type: "warning",
        text: message
    });
}

function swalError(message){
    Swal.fire({
        title: 'Something went wrong',
        type: "error",
        text: message
    });
}

function genJson(email){

    return '{"email" : "' + email + '"}';
}