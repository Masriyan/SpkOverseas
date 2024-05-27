const base_url = window.location.origin + "/spk-app";
const login_url = base_url + '/auth/login';
const current_url = window.location.href;
const url = new URL(current_url);
const activationUrl = base_url+'/api/users/activation?activationKey='+url.searchParams.get("registrationKey");

$('#viewPassword').click(function() {

    $(this).toggleClass("fa-eye fa-eye-slash");
    let input = $('#password');
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

function onActivateUser() {
    let json = genActivateJson();
    showLoadingActivation();

    $.ajax({
        type: "PUT",
        url: activationUrl,
        contentType: 'application/json',
        data: json,
        success: function (data) {
            if(data.responseCode == 1012){
                Swal.fire({
                    title: "User is Activated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        location.href = login_url;
                    }
                });
            }
            else{
                Swal.fire({
                    title: "User Activation is Failed",
                    text: data.responseMessage,
                    type: "warning",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                });
            }
        },
        error: function (x) {
            Swal.fire({
                title: "Something went wrong :(",
                text: e.message,
                type: "warning",
                allowOutsideClick: false,
                confirmButtonText: 'Ok'
            });
        }
    });
    return false;
}

function showLoadingActivation(){
    Swal.fire({
        title: 'Activating User',
        text: 'Please wait while activating...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });

}

function genActivateJson(){
    let password = document.getElementById('password').value;
    let confirmPassword = document.getElementById('confirmPassword').value;

    return '{'
        + '"password": "'+password+'",'
        + '"confirmPassword": "'+confirmPassword+'"'
        +'}';
}