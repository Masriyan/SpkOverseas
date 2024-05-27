const base_url = window.location.origin + "/spk-app";
const putAppRuleUrl = base_url +"/api/rule";
const listAppRuleUrl = base_url + "/rules"
const detailAppRuleUrl = base_url + "/rules";
const oldrule = $('rule');
const errMessage = $('#errMessage');
const errMessageLayout = $('#errMessageLayout');
const divusr = $('#divusrlvl');

$(document).ready(function(){
    //set active menu
    setMenuOpen('menuRules');
    setSubMenuActive('menuListOfRules');
});

function onUpdateRuleBtnClick(){

    let assetId = document.getElementById('assettype').value;
    let poTyp = document.getElementById('potype').value;
    let minAmt = document.getElementById('minamount').value;
    let maxAmt = document.getElementById('maxamount').value;
    let apprlvl = document.getElementById('levuser').value;
    let apprrole = document.getElementById('levuser').value;

    //loading swal
    Swal.fire({
        title: 'Updatting Approval Rule',
        text: 'Please wait while Updating Approval Rule...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });

    let json = genRuleJson(appId, assetId, poTyp, minAmt, maxAmt, apprlvl, apprrole);
    console.log(json);

    postRule(json);
}

function genRuleJson(appId, assetId, poTyp, minAmt, maxAmt, apprlvl, apprrole){

    let appLvRole = '"appruleList": [';

    appLvRole += '{"approvalLevel": "'+apprlvl+'","roleId": "'+apprrole+'"}'

    appLvRole += "]";

    return '{ '
        + '"approvalRulesId" : "'+appId+'",'
        + '"assetTypeId" : "'+assetId+'",'
        + '"poType" : "'+poTyp+'",'
        + '"minAmount" : "'+minAmt+'",'
        + '"maxAmount" : "'+maxAmt+'",'
        + appLvRole
        + '}';
}

function postRule(json){

    $.ajax({
        type: "PUT",
        url: putAppRuleUrl,
        contentType: "application/json",
        data: json,
        async: false,
        success: function (data) {

            if(data.responseCode == 1052){
                Swal.fire({
                    title: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    showCancelButton: true,
                    html: 'Rule is Updatted',
                    confirmButtonText: 'View Approval Rules',
                    reverseButtons: true
                }).then((result) =>{
                    if (result.value){
                        location.href = listAppRuleUrl;
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Update Approval Rule Failed",
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