const base_url = window.location.origin + "/spk-app";
const postAppRuleUrl = base_url +"/api/rule";
const listAppRuleUrl = base_url + "/rules"
const newSbuUrl = base_url + "/rules/create-new-app-rule";
const errMessage = $('#errMessage');
const errMessageLayout = $('#errMessageLayout');
const divusr = $('#divusrlvl');

$(document).ready(function(){
    //set active menu
    setMenuOpen('menuRules');
    setSubMenuActive('menuAddNewRule');

    console.log('Start Here');

    hidedivid();
    hideRolv();
});

function onSubmitRuleBtnClick(){

    let choid = document.getElementById("choid").value;
    let appId = "";

    if(choid == 1){
        appId = document.getElementById('eksid').value;
    }
    else if(choid == 2){
        appId = document.getElementById('newId').value;
    }

    let assetId = document.getElementById('assettype').value;
    let poTyp = document.getElementById('potype').value;
    let minAmt = document.getElementById('minamount').value;
    let maxAmt = document.getElementById('maxamount').value;
    let apprlvl = document.getElementById('levuser').value;
    let countryData = document.getElementById('countryData').value;
    //loading swal
    Swal.fire({
        title: 'Submitting New Approval Rule',
        text: 'Please wait while submitting Approval '+appId+'...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });

    let json = genRuleJson(appId, assetId, poTyp, minAmt, maxAmt, apprlvl,countryData);
    console.log(json);

    //debug(postAppRuleUrl,json);

    postRule(json);
}

function genRuleJson(appId, assetId, poTyp, minAmt, maxAmt, apprlvl,countryData){

    let appLvRole = '"appruleList": [';

    let i = 0;
    for(i = 1; i <= apprlvl; i++){
        let idrole = document.getElementById('roleid'+i+'').value;
        appLvRole += '{"approvalLevel": "'+i+'","roleId": "'+idrole+'"}'
        if(i != apprlvl) appLvRole += ',';
    }
    appLvRole += "]";

    return '{ '
        + '"approvalRulesId" : "'+appId+'",'
        + '"assetTypeId" : "'+assetId+'",'
        + '"poType" : "'+poTyp+'",'
        + '"minAmount" : "'+minAmt+'",'
        + '"maxAmount" : "'+maxAmt+'",'
        + '"countryId" : '+countryData+','
        + appLvRole
        + '}';
}

function postRule(json){

    $.ajax({
        type: "POST",
        url: postAppRuleUrl,
        contentType: "application/json",
        data: json,
        async: false,
        success: function (data) {
            console.log(data);

            if(data.responseCode == 1050){
                Swal.fire({
                    title: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    showCancelButton: true,
                    html: 'New Rule is Submitted',
                    confirmButtonText: 'View Approval Rules',
                    cancelButtonText: 'Add Another',
                    reverseButtons: true
                }).then((result) =>{
                    if (result.value){
                        location.href = listAppRuleUrl;
                    }else if(result.dismiss === Swal.DismissReason.cancel){
                        location.href = newSbuUrl;
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Submit Approval Rule Failed",
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

function onIdChange(){

    hidedivid();

    Swal.fire({
        title: 'Fetching ID Option',
        text: 'Please wait...',
        showConfirmButton: false,
        allowOutsideClick: false
    });

    let choid = document.getElementById("choid").value;

    if(choid == 1){
        document.getElementById('diveksid').style.display = 'block';
    }
    else if(choid == 2){
        document.getElementById('divnewId').style.display = 'block';
    }

    Swal.close();

}

function onLevelChange(){

    hideRolv();

	Swal.fire({
        title: 'Fetching Level User',
        text: 'Please wait...',
        showConfirmButton: false,
        allowOutsideClick: false
    });

    let lvlct = document.getElementById("levuser").value;

    if(lvlct){
	    setdivusr(lvlct);
	}

	Swal.close();

}

function setdivusr(lvlct){

    for(i=1; i<= lvlct ; i++){
        document.getElementById('divusrlvl'+i+'').style.display = "block";
    }

}

function hideRolv(){

    document.getElementsByName('rollev')[0].style.display = 'none';
    document.getElementsByName('rollev')[1].style.display = 'none';
    document.getElementsByName('rollev')[2].style.display = 'none';
    document.getElementsByName('rollev')[3].style.display = 'none';
    document.getElementsByName('rollev')[4].style.display = 'none';

}

function hidedivid(){

    document.getElementById('diveksid').style.display = 'none';
    document.getElementById('divnewId').style.display = 'none';

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