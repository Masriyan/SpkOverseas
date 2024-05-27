const base_url = window.location.origin + "/spk-app";
const uploadLetterHeadUrl = base_url + "/api/companies/letter-head";
const postCompanyUrl = base_url +"/api/companies";
const listCompaniesUrl = base_url + "/companies"
const newCompanyUrl = base_url + "/companies/create-new-company";

$(document).ready(function(){
    //set active menu
    setMenuOpen('menuCompanies');
    setSubMenuActive('menuAddNewCompany');
});

function onSubmitCompany(){

    let companyId = document.getElementById('companyId').value;
    let processingGroup = document.getElementById('processingGroup').value;
    let companyName = document.getElementById('companyName').value;
    let sbuCode = document.getElementById('sbuCode').value;
    let sbuDesc = document.getElementById('sbuDesc').value;
    let coa = document.getElementById('coa').value;
    let npwp = document.getElementById('npwp').value;
    let address1 = document.getElementById('address1').value;
    let address2 = document.getElementById('address2').value;
    let city = document.getElementById('city').value;
    let approvalRule = document.getElementById('approvalRule').value;
    let countryData = document.getElementById('countryData').value;
    let letterHead = new FormData();

    //loading swal
    Swal.fire({
        title: 'Submitting New Company',
        text: 'Please wait while submitting company...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });

    //upload letter head first
    letterHead.append("letterHead", $("#letterHead")[0].files[0]);
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

            let json = genCompanyJson(companyId, processingGroup, companyName,sbuCode,sbuDesc, coa, npwp, address1, address2, city, approvalRule, uploadedLetterHead, countryData);

            postCompany(json);
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
    });

    return false;
}

function postCompany(json){

    $.ajax({
        type: "POST",
        url: postCompanyUrl,
        contentType: "application/json",
        data: json,
        async: false,
        success: function (data) {
            console.log(data);

            if(data.responseCode == 1020){
                Swal.fire({
                    title: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    showCancelButton: true,
                    html: 'Company <b>'+data.company.companyName+'</b> is submitted',
                    confirmButtonText: 'View Companies',
                    cancelButtonText: 'Add Another',
                    reverseButtons: true
                }).then((result) =>{
                    if (result.value){
                        location.href = listCompaniesUrl;
                    }else if(result.dismiss === Swal.DismissReason.cancel){
                        location.href = newCompanyUrl;
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessful Submit Company",
                    text: data.responseMessage,
                    type: "warning",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                });
            }
        },
        error: function (err) {
            Swal.fire({
                title: "Something went wrong :(",
                text: "Please try again...",
                type: "warning",
                allowOutsideClick: false,
                confirmButtonText: 'Ok'
            })
        }
    });
}

function genCompanyJson(companyId, processingGroup, companyName,sbuCode,sbuDesc, coa, npwp, address1, address2, city, approvalRule, letterHead,countryData){

    return '{ '
        + '"companyId" : "'+companyId+'",'
        + '"processingGroup" : "'+processingGroup+'",'
        + '"companyName" : "'+companyName+'",'
        + '"sbuCode" : "'+sbuCode+'",'
        + '"sbuDesc" : "'+sbuDesc+'",'
        + '"coa" : "'+coa+'",'
        + '"npwp" : "'+npwp+'",'
        + '"address1" : "'+address1+'",'
        + '"address2" : "'+address2+'",'
        + '"city" : "'+city+'",'
        + '"approvalRulesId" : "'+approvalRule+'",'
        + '"countryData" : '+countryData+','
        // + '"approvalRulesId" : "RULE0000",'
        + '"headerImage" : "'+letterHead+'"'
        + '}';
}
