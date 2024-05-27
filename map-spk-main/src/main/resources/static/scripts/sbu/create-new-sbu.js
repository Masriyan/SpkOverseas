const base_url = window.location.origin + "/spk-app";
const postSbuUrl = base_url +"/api/sbu";
const listSbuUrl = base_url + "/sbu"
const newSbuUrl = base_url + "/sbu/create-new-sbu";

$(document).ready(function(){
    //set active menu
    setMenuOpen('menuSbus');
    setSubMenuActive('menuAddNewSbu');
});

function onSubmitSbu(){

    let sbuCode = document.getElementById('sbuCode').value;
    let sbuName = document.getElementById('sbuName').value;
    let companyId = document.getElementById('companyid').value;
    let approvalRule = document.getElementById('approvalRule').value;

    //let letterHead = new FormData();

    //loading swal
    Swal.fire({
        title: 'Submitting New Sbu',
        text: 'Please wait while submitting sbu...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });

    let json = genSbuJson(sbuCode, sbuName, companyId, approvalRule);

    postSbu(json);
}

function postSbu(json){

    $.ajax({
        type: "POST",
        url: postSbuUrl,
        contentType: "application/json",
        data: json,
        async: false,
        success: function (data) {
            console.log(data);

            if(data.responseCode == 1040){
                Swal.fire({
                    title: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    showCancelButton: true,
                    html: 'Company <b>'+data.sbu.sbuDesc+'</b> is submitted',
                    confirmButtonText: 'View Sbu',
                    cancelButtonText: 'Add Another',
                    reverseButtons: true
                }).then((result) =>{
                    if (result.value){
                        location.href = listSbuUrl;
                    }else if(result.dismiss === Swal.DismissReason.cancel){
                        location.href = newSbuUrl;
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessful Submit Sbu",
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

function genSbuJson(sbuCode, sbuName, companyId, approvalRule){

    return '{ '
        + '"sbuCode" : "'+sbuCode+'",'
        + '"sbuDesc" : "'+sbuName+'",'
        + '"companyId" : "'+companyId+'",'
        + '"approvalRulesId" : "'+approvalRule+'"'
        + '}';
}
