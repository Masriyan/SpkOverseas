const action = 'changePassword';
const loggedInUsername = document.getElementById('loggedInUserName').innerText;
const base_url = window.location.origin + "/spk-app";
const changePasswordUrl = base_url+'/api/users/'+loggedInUsername +'/';

$(document).ready(function(){
    //set active menu
    setSubMenuActive('menuChangePassword');
});

$('#viewCurrentPassword').click(function() {

    $(this).toggleClass("fa-eye fa-eye-slash");
    let input = $('#currentPassword');
    if (input.attr("type") == "password") {
        input.attr("type", "text");
    } else {
        input.attr("type", "password");
    }
});

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

function onChangePasswordSubmit(){

    Swal.fire({
        title: 'Updating Password',
        text: 'Please wait while updating...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });

    let json = genChangePasswordJson();

    console.log(changePasswordUrl);

    $.ajax({
        type: "PUT",
        url: changePasswordUrl,
        contentType: 'application/json',
        data: json,
        success: function (data) {
            if(data.responseCode == 1011){
                Swal.fire({
                    title: "Password Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        location.href = base_url;
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessful Update Password",
                    text: data.responseMessage,
                    type: "warning",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                });
            }
        },
        error: function (e) {
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

function genChangePasswordJson(){

    let currentPassword = document.getElementById('currentPassword').value;
    let newPassword = document.getElementById('newPassword').value;
    let confirmPassword = document.getElementById('confirmPassword').value;

    return '{'
        + '"action": "'+action+'",'
        + '"currentPassword": "'+currentPassword+'",'
        + '"newPassword": "'+newPassword+'",'
        + '"confirmPassword": "'+confirmPassword+'"'
        +'}';

}