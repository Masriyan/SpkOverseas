const base_url =  window.location.origin + "/spk-app";
const user_name = document.getElementById("userName").innerHTML;
const full_name = document.getElementById("fullName").innerHTML;
const email_url = base_url + "/api/users/"+user_name +"/email";
const role_url = base_url + "/api/users/"+user_name +"/role";
const get_roles_url = base_url + "/api/roles";
const get_company_url = base_url + "/api/users/"+user_name +"/companies";
const del_sbu_url = base_url + "/api/users/"+user_name +"/sbus";
const company_url = base_url + "/api/users/"+user_name +"/companies";
const sbu_url = base_url + "/api/users/"+user_name +"/sbus";
const get_companies_url = base_url + "/api/companies/all";
const get_sbus_url = base_url + "/api/sbu/listsbusn?username="+user_name;

var roles, companies, sbus;

$(document).ready(function() {
    getAllRole();
    getAllCompany();
    getAllSbu();
});

function getAllRole(){

    roles= new Map();
    $.ajax({
        type: "GET",
        url: get_roles_url,
        success(data){
            for(let i = 0; i < data.length; i ++){
                roles.set(data[i].roleId, data[i].roleName);
            }
        }
    });
}

function getAllCompany(){

    $.ajax({
        type: "GET",
        url: get_companies_url,
        success(data){
            for(let i = 0; i < data.length; i ++){
                companies += '<option value="'+data[i].companyId+'">'+data[i].companyId+' - '+data[i].companyName+'</option>'
            }
        }
    });
}

function getAllSbu(){

    $.ajax({
        type: "GET",
        url: get_sbus_url,
        success(data){
            for(let i = 0; i < data.length; i ++){
                sbus += '<option value="'+data[i].sbuId+'">'+data[i].sbuCode+' - '+data[i].sbuDesc+'</option>'
            }
        }
    });
}

function onClickChangePassword(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Input ' + full_name +' new email address',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'email',
        inputPlaceholder: 'Enter New Email',
        reverseButtons: true,
        inputAttributes:{
            maxlength: 225
        }
    }).then((email) => {
        if(email.value){

            let json = genChangeEmailJson(email.value);
            changeEmail(json);
        }
    });
}

function changeEmail(json){

    showChangeEmailLoading();
    let jsonObject = JSON.parse(json);

    $.ajax({
        type: "PATCH",
        url: email_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1014){
                Swal.fire({
                    title: "Email is Changed",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("email").innerText = jsonObject["email"];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully change email",
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
                text: "Please try again...",
                type: "warning",
                allowOutsideClick: false,
                confirmButtonText: 'Ok'
            });
        }
    });
}

function onClickChangeRole(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Select ' + full_name +' new role',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'select',
        inputOptions: roles,
        inputPlaceholder: 'Select new role',
        reverseButtons: true,
        inputValidator: (value) => {
            if (!value){
                return 'Please select a role!'
            }
        }
    }).then((result) => {
        if(result.value){

            let roleId = result.value;
            let roleName = roles.get(parseInt(roleId));
            let json = genChangeRoleJson(roleId);
            changeRole(json, roleName);
        }
    });
}

function changeRole(json, roleName){

    showChangeRoleLoading();
    $.ajax({
        type: "PATCH",
        url: role_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1015){
                Swal.fire({
                    title: "Role is Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("role").innerText = roleName;
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully Update Role",
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
                text: "Please try again...",
                type: "warning",
                allowOutsideClick: false,
                confirmButtonText: 'Ok'
            });
        }
    });
}

function genChangeEmailJson(email){
    return '{'
        + '"email": "'+email+'"'
        +'}';
}

function genChangeRoleJson(role){
    return '{'
        + '"roleId": '+role+''
        +'}';
}

function genRemoveCompanyJson(companyId){
    return '{'
        + '"companyId": "'+companyId+'"'
        +'}';
}

function genRemoveSbuJson(sbuId){
    return '{'
        + '"sbuId": "'+sbuId+'"'
        +'}';
}

function showChangeEmailLoading(){
    Swal.fire({
        title: 'Changing Email',
        text: 'Please wait while changing email...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function showAddNewCompanyLoading(){
    Swal.fire({
        title: 'Adding New Companies',
        text: 'Please wait while adding new companies...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function showAddNewSbuLoading(){
    Swal.fire({
        title: 'Adding New Sbus',
        text: 'Please wait while adding new sbus...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function showChangeRoleLoading(){
    Swal.fire({
        title: 'Updating Role',
        text: 'Please wait while updating email...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function showRemoveCompanyLoading(){
    Swal.fire({
        title: 'Removing Company',
        text: 'Please wait while removing company...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function showRemoveSbuLoading(){
    Swal.fire({
        title: 'Removing Sbu',
        text: 'Please wait while removing Sbu...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickDeleteCompany(companyId, companyname){

    Swal.fire({
        title: 'Remove Confirmation',
        text: 'Are you sure want to remove ' + companyname +' from this user?',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, remove it!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true
    }).then((result) => {
        if (result.value) {
            Swal.fire({
                title: 'Removing Company Access',
                text: 'Please wait while removing ' + companyname + ' from this user...',
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });

            let json = genRemoveCompanyJson(companyId);
            removeCompany(json, companyId);
        }
    });

}

function removeCompany(json, companyId){
    showRemoveCompanyLoading();

    $.ajax({
        type: "DELETE",
        url: get_company_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1016){
                Swal.fire({
                    title: "Company is Removed",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        //document.getElementById("email").innerText = jsonObject["email"];
                        location.reload();
                    }
                });

                //remove li element from ul
                let li = document.getElementById(companyId);
                li.parentNode.removeChild(li);

            }
            else{
                Swal.fire({
                    title: "Unsuccessfully remove company",
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
                text: "Please try again...",
                type: "warning",
                allowOutsideClick: false,
                confirmButtonText: 'Ok'
            });
        }
    });
}

function onClickAddCompanies(){

    Swal.fire({
        title: 'Company',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, add it!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true,
        html:
            `<div class="form-group" style="margin-top:15px">
                <select multiple="multiple"  id="newCompany">
                    `+companies+`
                </select>
            </div>`,
        onOpen: () =>{
            $('#newCompany').select2({
                placeholder: 'Input Company',
                minimumResultsForSearch: 15,
                width: '100%',
                language: "it"
            });
        },
        preConfirm: () =>{
            let newCompany = $('#newCompany').val();

            if(newCompany.length === 0) {
                return Swal.showValidationMessage("Please enter a company...");
            }

            return newCompany;
        }
    }).then((result) =>{
        if(result.value){
            let json  = genNewCompaniesJson(result.value);
            addNewCompany(json);
        }
    });
}

function addNewCompany(json){

    showAddNewCompanyLoading();

    $.ajax({
        type: "POST",
        url: company_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1017){
                Swal.fire({
                    title: "Success",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        location.reload();
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully add new company",
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
                text: "Please try again...",
                type: "warning",
                allowOutsideClick: false,
                confirmButtonText: 'Ok'
            });
        }
    });
}

function genNewCompaniesJson(companies){

    let json = '[';

    for(let i = 0; i <companies.length; i++){
        json += '{"companyId": "'+companies[i] +'"}'

        if(i+1 != companies.length) json += ',';
    }
    json += "]";
    return json;
}

function genNewSbusJson(sbus){

    let json = '[';

    for(let i = 0; i <sbus.length; i++){
        json += '{"sbuId": "'+sbus[i] +'"}'

        if(i+1 != sbus.length) json += ',';
    }
    json += "]";
    return json;
}

function onClickDeleteSbu(sbuId, sbuDesc){

    Swal.fire({
        title: 'Remove Confirmation',
        text: 'Are you sure want to remove Sbu ' + sbuDesc +' from this user?',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, remove it!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true
    }).then((result) => {
        if (result.value) {
            Swal.fire({
                title: 'Removing Sbu Access',
                text: 'Please wait while removing ' + sbuDesc + ' from this user...',
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });

            let json = genRemoveSbuJson(sbuId);
            removeSbu(json, sbuId);
        }
    });

}

function removeSbu(json, sbuId){
    showRemoveSbuLoading();

    $.ajax({
        type: "DELETE",
        url: del_sbu_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1019){
                Swal.fire({
                    title: "Sbu is Removed",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        //document.getElementById("email").innerText = jsonObject["email"];
                        location.reload();
                    }
                });

                //remove li element from ul
                let li = document.getElementById("sbu"+sbuId);
                li.parentNode.removeChild(li);

            }
            else{
                Swal.fire({
                    title: "Unsuccessfully remove sbu",
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
                text: "Please try again...",
                type: "warning",
                allowOutsideClick: false,
                confirmButtonText: 'Ok'
            });
        }
    });
}

function onClickAddSbus(){

    Swal.fire({
        title: 'Sbu',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, add it!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true,
        html:
            `<div class="form-group" style="margin-top:15px">
                <select multiple="multiple"  id="newSbu">
                    `+sbus+`
                </select>
            </div>`,
        onOpen: () =>{
            $('#newSbu').select2({
                placeholder: 'Input Sbu',
                minimumResultsForSearch: 15,
                width: '100%',
                language: "it"
            });
        },
        preConfirm: () =>{
            let newSbu = $('#newSbu').val();

            if(newSbu.length === 0) {
                return Swal.showValidationMessage("Please enter a sbu...");
            }

            return newSbu;
        }
    }).then((result) =>{
        if(result.value){
            let json  = genNewSbusJson(result.value);
            addNewSbu(json);
        }
    });
}

function addNewSbu(json){

    showAddNewSbuLoading();

    $.ajax({
        type: "POST",
        url: sbu_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1018){
                Swal.fire({
                    title: "Success",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        location.reload();
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully add new sbu",
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
                text: "Please try again...",
                type: "warning",
                allowOutsideClick: false,
                confirmButtonText: 'Ok'
            });
        }
    });
}

