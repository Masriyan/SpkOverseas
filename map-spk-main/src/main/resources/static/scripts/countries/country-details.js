const base_url =  window.location.origin + "/spk-app";
const countryId = document.getElementById("countryId").innerHTML;
const country_code_url = base_url + "/api/countries/"+countryId+"/countrycode";
const country_name_url = base_url + "/api/countries/"+countryId+"/countryname";
const change_label_url = base_url + "/api/countries/"+countryId;

$(document).ready(function() {

});

function onClickChangeCountryCode(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Input new Country Code',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'text',
        inputPlaceholder: 'Enter New Country Code',
        reverseButtons: true,
        inputAttributes:{
            maxlength: 225
        }
    }).then((data) => {
        if(data.value){
            let json =  '{'
                      + '"countryCode": "'+data.value+'"'
                      +'}';
            changeCountryCode(json);
        }
    });
}

function changeCountryCode(json){

    showChangeCountryCodeLoading();
    let jsonObject = JSON.parse(json);

    $.ajax({
        type: "PATCH",
        url: country_code_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1062){
                Swal.fire({
                    title: "Country Code is Changed",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("countryCode").innerText = jsonObject["countryCode"];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully change Country Code",
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

function showChangeCountryCodeLoading(){
    Swal.fire({
        title: 'Changing Country Code',
        text: 'Please wait while changing Country Code...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}


function onClickChangeCountryName(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Input new Country Name',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'text',
        inputPlaceholder: 'Enter New Country Name',
        reverseButtons: true,
        inputAttributes:{
            maxlength: 225
        }
    }).then((data) => {
        if(data.value){
            let json =  '{'
                      + '"countryName": "'+data.value+'"'
                      +'}';
            changeCountryName(json);
        }
    });
}

function changeCountryName(json){

    showChangeCountryNameLoading();
    let jsonObject = JSON.parse(json);

    $.ajax({
        type: "PATCH",
        url: country_name_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1063){
                Swal.fire({
                    title: "Country Name is Changed",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("countryName").innerText = jsonObject["countryName"];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully change Country Name",
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

function showChangeCountryNameLoading(){
    Swal.fire({
        title: 'Changing Country Name',
        text: 'Please wait while changing Country Name...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickChangeLabel(LabelName){
    Swal.fire({
        title: 'Confirmation',
        text: 'Input new '+LabelName,
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'text',
        inputPlaceholder: 'Enter New '+LabelName,
        reverseButtons: true,
        inputAttributes:{
            maxlength: 225
        }
    }).then((data) => {
        if(data.value){
            let json =  '{'
                      + '"'+LabelName+'": "'+data.value+'"'
                      +'}';
            changeLabel(json,LabelName);
        }
    });
}

function showChangelabelLoading(labelName){
    Swal.fire({
        title: 'Changing '+ labelName,
        text: 'Please wait while changing ' + labelName + '...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function changeLabel(json,labelName){

    showChangelabelLoading(labelName);
    let jsonObject = JSON.parse(json);

    $.ajax({
        type: "PATCH",
        url: change_label_url+"/"+labelName,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1064){
                Swal.fire({
                    title: labelName+" is Changed",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById(labelName).innerText = jsonObject[labelName];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully change "+labelName,
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