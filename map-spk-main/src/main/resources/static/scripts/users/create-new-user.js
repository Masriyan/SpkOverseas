const base_url = window.location.origin + "/spk-app";
const postUserUrl = base_url +"/api/users";
const cekUserUrl = base_url +"/api/users/check-ad";
const errMessage = $('#errMessage');
const errMessageLayout = $('#errMessageLayout');
const sbu = $('#sbus');

$(document).ready(function() {

    //set active menu
    setMenuOpen('menuUsers');
    setSubMenuActive('menuAddNewUser');

    //Initialize Select2 Companies
    $('#companies').select2();
    $('#sbus').select2();
});

function onSubmitUserBtnClick(){

    let userName = document.getElementById("userName").value;
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let email = document.getElementById("email").value;
    let roleId = document.getElementById("role").value;
    let companies = $('#companies').select2('data');
    let sbus = $('#sbus').select2('data');
    //let sbuId = document.getElementById("sbu").value;

    //let json = genUserJson(userName, firstName, lastName, email, roleId, companies, sbuId);
    let json = genUserJson(userName, firstName, lastName, email, roleId, companies, sbus);

    if(!userName){
        errMessage.text("");
        errMessage.append("Username can not be empty.");
        errMessageLayout.fadeIn();
    }
    else if(!firstName){
        errMessage.text("");
        errMessage.append("First name should be filled.");
        errMessageLayout.fadeIn();
    }
    else if(!email){
        errMessage.text("");
        errMessage.append("Email should be filled.");
        errMessageLayout.fadeIn();
    }
    else if(!validateEmail(email)){
        errMessage.text("");
        errMessage.append("Invalid email!");
        errMessageLayout.fadeIn();
    }
    else if(companies.length == 0){
        errMessage.text("");
        errMessage.append("Please select company for user.");
        errMessageLayout.fadeIn();
    }
    else if(sbus.length == 0){
        errMessage.text("");
        errMessage.append("Please select sbu for user.");
        errMessageLayout.fadeIn();
    }
    else if(!roleId){
        errMessage.text("");
        errMessage.append("Please select role for user.");
        errMessageLayout.fadeIn();
    }
    else{
        errMessageLayout.fadeOut();
        //show loading message
        Swal.fire({
            title: 'Submit New User',
            text: 'Please wait while submitting user...',
            showConfirmButton: false,
            allowOutsideClick: false,
            imageWidth: 100,
            imageHeight: 100,
            imageUrl: base_url+'/images/loading.gif'
        });

        $.ajax({
            type: "POST",
            url: postUserUrl,
            contentType: "application/json",
            data: json,
            success: function (data) {
                console.log(data);

                if(data.responseCode == 1010){
                    Swal.fire({
                        title: data.responseMessage,
                        type: "success",
                        allowOutsideClick: false,
                        showCancelButton: true,
                        html: 'User is submitted',
                        confirmButtonText: 'View Users',
                        cancelButtonText: 'Add Another',
                        reverseButtons: true
                    }).then((result) =>{
                        if (result.value){
                            location.href = base_url + "/users";
                        }else if(result.dismiss === Swal.DismissReason.cancel){
                            location.href = base_url + "/users/create-new-user";
                        }
                    });
                }
                else{
                    Swal.fire({
                        title: "Unsuccessful Add User",
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
    }
    return false;
}

function onRoleChange(){

    Swal.fire({
        title: 'Fetching Sbu Data',
        text: 'Please wait...',
        showConfirmButton: false,
        allowOutsideClick: false
    });

    let companies = $('#companies').select2('data');
    //let companyId = document.getElementById("companies").value;
    let company = '';
    for(let i = 0; i < companies.length; i++){
        company += companies[i].id;

        if(i+1 != companies.length) company += ',';
    }

    setSbu(company);

    Swal.close();
}

function setSbu(companyId){
    let sbuUrl = base_url +"/api/sbu/listcomp?companyId=" + companyId;

    $.ajax({
        url: sbuUrl,
        type: "GET",
        async:false,
        success: function (data) {
            setSbuEmpty();

            for(let i=0; i<data.length; i++){
                sbu.append('<option value=' + data[i].sbuId + '>' + data[i].sbuCode + ' - ' + data[i].sbuDesc + '</option>');
            }
        }
    });
}

function setSbuEmpty(){
    sbu.empty();
    sbu.append('<option value=0>-Select Sbu-</option>');
}

function genUserJson(userName, firstName, lastName, email, roleId, companies, sbus){

    //set role to json
    let roleJson = '"roles": [{"roleId": "'+roleId+'"}]';

    //set companies to json
    let companyJson = '"companies": [';
    for(let i = 0; i <companies.length; i++){
        companyJson += '{"companyId": "'+companies[i].id +'"}'

        if(i+1 != companies.length) companyJson += ',';
    }
    companyJson += "]";

    //set sbu to json
    let sbuJson = '"sbus": [';
    for(let i = 0; i <sbus.length; i++){
        sbuJson += '{"sbuId": "'+sbus[i].id +'"}'

            if(i+1 != sbus.length) sbuJson += ',';
        }
    sbuJson += "]";

    //set json
    let json ='{'
        + '"userName": "'+userName+'",'
        + '"firstName": "'+firstName+'",'
        + '"lastName": "'+lastName+'",'
        + '"email": "'+email+'",'
        + roleJson + ','
        + companyJson + ','
        + sbuJson
        + '}';

    console.log(json);

    return json;
}

function onCheckUserBtnClick(){

    let userName = document.getElementById("userName").value;

    //let json = genUserJson(userName, firstName, lastName, email, roleId, companies, sbuId);
    let json = genCheckUnameJson(userName);

    if(!userName){
        errMessage.text("");
        errMessage.append("Username can not be empty.");
        errMessageLayout.fadeIn();
    }
    else{
        errMessageLayout.fadeOut();
        //show loading message
        Swal.fire({
            title: 'Checking Username on AD System',
            text: 'Please wait while checking username...',
            showConfirmButton: false,
            allowOutsideClick: false,
            imageWidth: 100,
            imageHeight: 100,
            imageUrl: base_url+'/images/loading.gif'
        });

        $.ajax({
            type: "POST",
            url: cekUserUrl,
            contentType: "application/json",
            data: json,
            success: function (data) {
                console.log(data);

                if(data.responseCode == 10121){
                    Swal.fire({
                        title: "Username Activated in AD",
                        type: "success",
                        allowOutsideClick: false,
                        confirmButtonText: 'Ok'
                    }).then((result) =>{
                        document.getElementById("userName").style.color = "green";
                    });
                }
                else{
                    Swal.fire({
                        title: "Username is not Activated in AD",
                        text: data.responseMessage,
                        type: "warning",
                        allowOutsideClick: false,
                        confirmButtonText: 'Ok'
                    }).then((result) =>{
                        document.getElementById("userName").style.color = "red";
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
    }
    return false;
}

function genCheckUnameJson(uname){
    return '{'
        + '"Username": "'+uname+'"'
        +'}';
}