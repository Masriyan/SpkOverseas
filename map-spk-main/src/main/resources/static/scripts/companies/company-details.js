const base_url =  window.location.origin + "/spk-app";
const compid = document.getElementById("compid").innerHTML;
const sbuid = document.getElementById("sbucomp").innerHTML;
const sbuno = document.getElementById("sbucomp").getAttribute("value");
//const sbudesc = document.getElementById("sbucomp").textContent;
const get_rules_url = base_url + "/api/rule/dist/";
const get_all_countries_url = base_url + "/api/countries/dist/";
const sbu_url = base_url + "/api/sbu/"+sbuno+"/edit";
const comp_name_url = base_url + "/api/companies/"+compid+"/compname";
const country_url = base_url + "/api/companies/"+compid+"/country";
const npwp_url = base_url + "/api/companies/"+compid+"/npwp";
const add1_url = base_url + "/api/companies/"+compid+"/add1";
const add2_url = base_url + "/api/companies/"+compid+"/add2";
const coa_url = base_url + "/api/companies/"+compid+"/coa";
const uploadLetterHeadUrl = base_url + "/api/companies/letter-head";
const headerimg_url = base_url + "/api/companies/"+compid+"/headimg";
const rule_url = base_url + "/api/sbu/"+sbuid+"/rule";

var sbu,rules,countryList;
var letterHead = new FormData();

$(document).ready(function() {
    getAllRule();
    getAllCountry();
});

function getAllRule(){
    $.ajax({
        type: "GET",
        url: get_rules_url,
        success(data){
            for(let i = 0; i < data.length; i ++){
                rules += '<option value="'+data[i].approvalRulesId+'">'+data[i].approvalRulesId+'</option>'
            }
        }
    });
}

function getAllCountry(){
    $.ajax({
        type: "GET",
        url: get_all_countries_url,
        success(data){
            console.log(data);
            for(let i = 0; i < data.length; i ++){
                countryList += '<option value="'+data[i].countryId+','+data[i].countryName+'">'+data[i].countryName+'</option>'
            }
        }
    });
}

function onClickChangeCompName(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Input ' + compid +' new Company Name',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'text',
        inputPlaceholder: 'Enter New Company Name',
        reverseButtons: true,
        inputAttributes:{
            maxlength: 225
        }
    }).then((email) => {
        if(email.value){

            let json = genChangeCompNameJson(email.value);
            //debug(comp_name_url,json);
            changeCompName(json);
        }
    });
}

function genChangeCompNameJson(email){

    return '{'
        + '"companyName": "'+email+'"'
        +'}';
}

function changeCompName(json){

    showChangeCompNameLoading();
    let jsonObject = JSON.parse(json);

    $.ajax({
        type: "PATCH",
        url: comp_name_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1022){
                Swal.fire({
                    title: "Company Name is Changed",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("namecomp").innerText = jsonObject["companyName"];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully change Company Name",
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

function showChangeCompNameLoading(){
    Swal.fire({
        title: 'Changing Company Name',
        text: 'Please wait while changing Company Name...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickChangeNPWP(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Input ' + compid +' new NPWP',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'text',
        inputPlaceholder: 'Ex: 02.505.007.1-073.000',
                reverseButtons: true,
                inputAttributes:{
                    maxlength: 225
                }
    }).then((text) => {
        if(text.value){

            let json = genChangeNPWPJson(text.value);
            //debug(npwp_url,json);
            changeNPWP(json);
        }
    });
}

function genChangeNPWPJson(role){
    return '{'
        + '"npwp": "'+role+'"'
        +'}';
}

function changeNPWP(json1){

    showChangeNPWPLoading();
    let jsonObject = JSON.parse(json1);

    $.ajax({
         type: "PATCH",
         url: npwp_url,
         contentType: "application/json",
         //data : JSON.stringify(json1),
         data: json1,
         success: function (data) {

            if(data.responseCode == 1023){
                Swal.fire({
                    title: "NPWP is Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("npwpcomp").innerText = jsonObject["npwp"];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully Update NPWP",
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

function showChangeNPWPLoading(){
    Swal.fire({
        title: 'Updating NPWP',
        text: 'Please wait while updating NPWP...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickChangeAdd1(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Input ' + compid +' new Address1',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'text',
        inputPlaceholder: 'Enter New Address1',
        reverseButtons: true,
            inputAttributes:{
                maxlength: 225
            }
    }).then((text) => {
            if(text.value){

            let json = genChangeAdd1Json(text.value);
            //debug(add1_url,json);
            changeAdd1(json);
        }
    });
}

function genChangeAdd1Json(role){

    return '{'
        + '"address1": "'+role+'"'
        +'}';
}

function changeAdd1(json2){

    showChangeAdd1Loading();
    let jsonObject = JSON.parse(json2);

    $.ajax({
         type: "PATCH",
         url: add1_url,
         contentType: "application/json",
         data: json2,
         //data : JSON.stringify(json2),
         //processData: false,
         //dataType: 'json',
         success: function (data) {

            if(data.responseCode == 1023){
                Swal.fire({
                    title: "Address1 is Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("add1comp").innerText = jsonObject["address1"];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully Update Address1",
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

function showChangeAdd1Loading(){
    Swal.fire({
        title: 'Updating Address Company',
        text: 'Please wait while updating Address Company...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickChangeAdd2(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Input ' + compid +' new Address2',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'text',
        inputPlaceholder: 'Enter New Address2',
        reverseButtons: true,
            inputAttributes:{
                maxlength: 225
            }
        }).then((text) => {
            if(text.value){

            let json = genChangeAdd2Json(text.value);
            //debug(add2_url,json);
            changeAdd2(json);
        }
    });
}

function genChangeAdd2Json(role){
    return '{'
        + '"address2": "'+role+'"'
        +'}';
}

function changeAdd2(json3){

    showChangeAdd2Loading();
    let jsonObject = JSON.parse(json3);

    $.ajax({
        type: "PATCH",
        url: add2_url,
        contentType: "application/json",
        data: json3,
        success: function (data) {

            if(data.responseCode == 1024){
                Swal.fire({
                    title: "Address2 is Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("add2comp").innerText = jsonObject["address2"];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully Update Address2",
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

function showChangeAdd2Loading(){
    Swal.fire({
        title: 'Updating Address Company',
        text: 'Please wait while updating Address Company...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickChangeCoa(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Input ' + compid +' new Coa',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'text',
        inputPlaceholder: 'Enter New COA',
        reverseButtons: true,
            inputAttributes:{
                maxlength: 225
            }
    }).then((text) => {
        if(text.value){

            let json = genChangeCoaJson(text.value);
            //debug(coa_url,json);
            changeCoa(json);
        }
    });
}

function genChangeCoaJson(role){
    return '{'
        + '"coa": "'+role+'"'
        +'}';
}

function changeCoa(json4){

    showChangeCoaLoading();
    let jsonObject = JSON.parse(json4);

    $.ajax({
        type: "PATCH",
        url: coa_url,
        contentType: "application/json",
        data: json4,
        success: function (data) {

            if(data.responseCode == 1025){
                Swal.fire({
                    title: "Coa is Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("coacomp").innerText = jsonObject["coa"];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully Update Coa",
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

function showChangeCoaLoading(){
    Swal.fire({
        title: 'Updating Coa',
        text: 'Please wait while updating Coa...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickChangeHeaderImg(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Select ' + compid +' new Header Image',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'file',
        inputPlaceholder: 'Select new Header Image',
        reverseButtons: true,
        inputValidator: (value) => {
            if (!value){
                return 'Please select a Header Image!'
            }
        }
    }).then((result) => {
        if(result.value){

            let roleId = result.value;
            uploadLetterHeadImg(roleId);
            //let json = genChangeHeadImgJson(roleId);
            //changeHeadImg(json);
        }
    });
}

function uploadLetterHeadImg(roleId){

    letterHead.append("letterHead", roleId[0].files[0]);
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: uploadLetterHeadUrl,
            data: letterHead,
            processData: false, //prevent jQuery from automatically transforming the data into a query string
            contentType: false,
            cache: false,
            timeout: 600000,
            async: false,
            success: function (data) {
                //success upload letter head file to server.
                let uploadedLetterHead = data.toString();
                console.log("SUCCESS : ", data);
                let json = genChangeHeadImgJson(uploadedLetterHead);
                changeHeadImg(json);
            },
            error: function (e) {
                console.log("ERROR : ", e);
                Swal.fire({
                    title: "Something went wrong :(",
                    text: e,
                    type: "warning",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                });
            }
        })

}

function genChangeHeadImgJson(uploadedLetterHead){
    return '{'
        + '"headerImage": "'+uploadedLetterHead+'"'
        +'}';
}

function changeHeadImg(json5){

    showChangeHeadImgLoading();
    let jsonObject = JSON.parse(json5);

    $.ajax({
        type: "PATCH",
        url: headerimg_url,
        contentType: "application/json",
        data: json5,
        success: function (data) {

            if(data.responseCode == 1026){
                Swal.fire({
                    title: "Header Image is Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("imageView").innerText = jsonObject["headerImage"];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully Update Header Image",
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

function showChangeHeadImgLoading(){
    Swal.fire({
        title: 'Updating Header Image',
        text: 'Please wait while updating Header Image...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickChangeRule(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Select ' + compid +' new Rule',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        //input: 'select',
        html:
            `<div class="form-group" style="margin-top:15px">
                <select id="newRule">
                    `+rules+`
                </select>
            </div>`,
        inputPlaceholder: 'Select new Rule',
        reverseButtons: true,
        inputValidator: (value) => {
            if (!value){
                return 'Please select a rule!'
            }
        }
    }).then((result) => {
        if(result.value){

            let newSbu = $('#newRule').val();

            let roleId = result.value;
            let json = genChangeRuleJson(newSbu);
            //debug(rule_url,json);
            changeRule(json);
        }
    });
}

function genChangeRuleJson(role){
    return '{'
        + '"approvalRulesId": "'+role+'"'
        +'}';
}

function changeRule(json6){

    showChangeRuleLoading();
    let jsonObject = JSON.parse(json6);

    $.ajax({
        type: "PATCH",
        url: rule_url,
        contentType: "application/json",
        data: json6,
        success: function (data) {

            if(data.responseCode == 1042){
                Swal.fire({
                    title: "Rule is Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("rule").innerText = jsonObject["approvalRulesId"];
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully Update Rule",
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

function showChangeRuleLoading(){
    Swal.fire({
        title: 'Updating Rule',
        text: 'Please wait while updating Rule...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickViewImage(){
    var imageview = document.getElementById("imageView").innerHTML;

    window.open(base_url+"/companies/img/" + imageview +"/", '_blank');
}

function onClickChangeSbu(){
    var resp = sbuid.split("-");
    var sbucd = resp[0];
    var sbudsc = resp[1];

    Swal.fire({
        title: 'Update Sbu Code & Sbu Description',
        text: 'Update ' + compid +' Sbu',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        html:
            `<input type="text" id="swal-input1" class="swal2-input" value=`+sbucd+`>
             <input type="text" id="swal-input2" class="swal2-input" value=`+sbudsc+`>`,
        reverseButtons: true,
        inputValidator: (value) => {
            if (!value){
                return 'Please input a sbu !'
            }
        }
    }).then((result) => {
        if(result.value){

            let roleupd = document.getElementById('swal-input1').value+' - '+
                         document.getElementById('swal-input2').value;
            let json = genChangeSbuJson(roleupd);
            changeSbu(json, roleupd);
            //debug(sbu_url,sbuno);
        }
    });
}

function genChangeSbuJson(role){

    var split = role.split("-");
    var sbucde = split[0];
    var sbudes = split[1];

    return '{'
        + '"sbuCode": "'+sbucde+'",'
        + '"sbuDesc": "'+sbudes+'"'
        +'}';
}

function changeSbu(json, roleName){

    showChangeSbuLoading();
    $.ajax({
        type: "PATCH",
        url: sbu_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1043){
                Swal.fire({
                    title: "Sbu is Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("sbucomp").innerText = roleName;
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully Update Sbu",
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

function showChangeSbuLoading(){
    Swal.fire({
        title: 'Updating Sbu',
        text: 'Please wait while updating Sbu...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function debug(url,json){

    Swal.fire({
            title: url,
            text: json,
            type: "success",
            allowOutsideClick: true,
            confirmButtonText: 'Ok'
    })

}

function onClickChangeCountry(){
    Swal.fire({
        title: 'Confirmation',
        text: 'Select ' + compid +' new Country',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        //input: 'select',
        html:
            `<div class="form-group" style="margin-top:15px">
                <select id="newCountry">
                    `+countryList+`
                </select>
            </div>`,
        inputPlaceholder: 'Select new Country',
        reverseButtons: true,
        inputValidator: (value) => {
            if (!value){
                return 'Please select a Country!'
            }
        }
    }).then((result) => {
        if(result.value){
            let newCountry = $('#newCountry').val();
            const myArray = newCountry.split(",");
            let json = genChangeCountryJson(myArray[0]);
            changeCountry(json,myArray[1]);
        }
    });
}

function genChangeCountryJson(email){

    return '{'
        + '"countryId": '+email+'}';
}

function changeCountry(json,nameCoutry){

    showChangeCuntryLoading();
    let jsonObject = JSON.parse(json);

    $.ajax({
        type: "PATCH",
        url: country_url,
        contentType: "application/json",
        data: json,
        success: function (data) {

            if(data.responseCode == 1027){
                Swal.fire({
                    title: "Country is Changed",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        document.getElementById("country").innerText = nameCoutry;
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessfully change Country",
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

function showChangeCuntryLoading(){
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