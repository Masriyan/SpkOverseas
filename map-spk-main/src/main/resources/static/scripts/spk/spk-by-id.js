const base_url = window.location.origin + "/spk-app";
const updateSpkUrl = base_url +"/api/surat-perintah-kerja";
const spkId = document.getElementById("spkId").innerHTML;
const asset_class_url = base_url + "/api/asset-class";
const total_gr = document.getElementById("totalGr").innerHTML;
var assetClasses;

$(document).ready(function () {
    getSameTypeAssetClass();
});

function cancelSpk() {
    let status = 'CANCELED';

    Swal
        .fire({
            title: 'Cancel SPK',
            type: "warning",
            text: 'Are you sure want to cancel SPK ' + spkId + '? Once canceled, you won\'t be able to revert this.',
            input: 'textarea',
            confirmButtonColor: '#dd4b39',
            confirmButtonText: 'Yes, cancel it!',
            cancelButtonColor: '#aaa',
            cancelButtonText: 'Back',
            reverseButtons: true,
            inputPlaceholder: 'Type your message here...',
            inputAttributes: {
                'aria-label': 'Type your message here'
            },
            inputValidator: (value) => {
                if (!value) {
                    return 'You need to write something!'
                }
            },
            showCancelButton: true,
            inputAttributes:{
                maxlength: 255
            }
        })
        .then((result) =>{
            if (result.value) {
                Swal.fire({
                    title: 'Canceling Work Order',
                    text: 'Please wait while canceling spk ' + spkId + '...',
                    showConfirmButton: false,
                    allowOutsideClick: false,
                    imageWidth: 100,
                    imageHeight: 100,
                    imageUrl: base_url+'/images/loading.gif'
                });
                let json = genSpkHistoryJson(spkId,status, '' ,result.value, 0, '');
                console.log(json);

                updateStatus(json, spkId, status);
            }
        });
}

function updateStatus(json, spkId, status){

    $.ajax({
        type: "PUT",
        url: updateSpkUrl,
        contentType: "application/json",
        data: json,
        success: function (data) {

            console.log(data);

            if(data.responseCode == 1001){
                Swal.fire({
                    title: "SPK is Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    html: '<b>' + data.suratPerintahKerja.spkId + '</b> ' + status,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        location.href = base_url + "/surat-perintah-kerja/" + spkId;
                    }
                });
            }else{
                Swal.fire({
                    title: "Update Unsuccessful",
                    text: data.responseMessage,
                    type: "warning",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        location.href = base_url + "/surat-perintah-kerja/" + spkId;
                    }
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

function rejectSpk(){

    let status = 'REJECTED';

    Swal
        .fire({
            title: 'Reject SPK',
            type: "warning",
            text: 'Are you sure want to reject SPK ' + spkId + '? Once rejected, you won\'t be able to revert this.',
            input: 'textarea',
            confirmButtonColor: '#dd4b39',
            confirmButtonText: 'Yes, reject it!',
            cancelButtonColor: '#aaa',
            cancelButtonText: 'Back',
            reverseButtons: true,
            inputPlaceholder: 'Type your message here...',
            inputAttributes: {
                'aria-label': 'Type your message here'
            },
            inputValidator: (value) => {
                if (!value) {
                    return 'You need to write something!'
                }
            },
            showCancelButton: true,
            inputAttributes:{
                maxlength: 255
            }
        })
        .then((result) =>{
            if (result.value) {
                Swal.fire({
                    title: 'Rejecting Work Order',
                    text: 'Please wait while rejecting spk ' + spkId + '...',
                    showConfirmButton: false,
                    allowOutsideClick: false,
                    imageWidth: 100,
                    imageHeight: 100,
                    imageUrl: base_url+'/images/loading.gif'
                });
                let json = genSpkHistoryJson(spkId,status, '',result.value, 0, '');
                console.log(json);

                updateStatus(json, spkId, status);
            }
        });
}

function approveSpk(){
    let status = 'APPROVED';

    Swal.fire({
        title: 'Approval Confirmation',
        text: 'Approve SPK ' + spkId +'?',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, approve it!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true
    }).then((result) => {
        if (result.value) {
            Swal.fire({
                title: 'Approving Work Order',
                text: 'Please wait while approving spk ' + spkId + '...',
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });

            let json = genSpkHistoryJson(spkId, status, '' , '', 0, '');
            console.log(json);
            updateStatus(json, spkId, status);
        }
    });
}

function doneapproveSpk(){
    let status = 'DONE_APPROVED';

    Swal.fire({
        title: 'Approval Confirmation',
        text: 'Approve SPK ' + spkId +'?',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, approve it!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true
    }).then((result) => {
        if (result.value) {
            Swal.fire({
                title: 'Approving Work Order',
                text: 'Please wait while approving spk ' + spkId + '...',
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });

            let json = genSpkHistoryJson(spkId, status, '' , '', 0, '');
            console.log(json);
            updateStatus(json, spkId, status);
        }
    });
}

function verifySpk(){
    let status = 'VERIFIED';

    Swal.fire({
        title: 'Confirmation',
        text: 'Verify SPK ' + spkId +'?',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, verify it!',
        cancelButtonText: 'No, cancel.',
        input: 'text',
        inputPlaceholder: 'Enter Asset Number',
        reverseButtons: true,
        inputValidator: (value) => {
            if (!value) {
                return 'Please fill asset number!'
            }
        },
        inputAttributes:{
            maxlength: 100
        }
    }).then((result) => {
        if(result.value){
            Swal.fire({
                title: 'Verifying Work Order',
                text: 'Please wait while verifying spk ' + spkId + '...',
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });
            let json = genSpkHistoryJson(spkId,status, result.value, '', 0, '');
            console.log(json);

            updateStatus(json, spkId, status);
        }
    });
}

function genSpkHistoryJson(spkId, statusName, assetNo, note, actualAmount, assetClassId){

    return '{'
        + '"spkId": "' + spkId + '",'
        + '"status": "' + statusName + '",'
        + '"assetNo": "' + assetNo + '",'
        + '"assetClassId": "' + assetClassId + '",'
        + '"actualAmount": ' + actualAmount + ','
        + '"spkNote": "' + note + '"'
        + '}';
}

function onClickChangeAssetNumber(){
    let status = 'VERIFIED';

    Swal.fire({
        title: 'Confirmation',
        text: 'Change Asset Number for SPK ' + spkId +'?',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'text',
        inputPlaceholder: 'Enter Asset Number',
        reverseButtons: true,
        inputValidator: (value) => {
            if (!value) {
                return 'Please fill asset number!'
            }
        },
        inputAttributes:{
            maxlength: 100
        }
    }).then((result) => {
        if(result.value){
            Swal.fire({
                title: 'Changing Asset Number',
                text: 'Please wait while changing asset number...',
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });
            let json = genSpkHistoryJson(spkId,status, result.value, '', 0, '');
            console.log(json);

            updateStatus(json, spkId, status);
        }
    });
}

function onPrintClick(){
    let printUrl = base_url+'/surat-perintah-kerja/' + spkId +'/print';
    let win = window.open(printUrl, '_blank');
    win.focus();
}

function onCloseRequestClick() {
    let status = 'CLOSE_REQUEST';

    Swal.fire({
            title: 'Request Close SPK',
            type: "warning",
            confirmButtonColor: '#dd4b39',
            confirmButtonText: 'Yes, make close request!',
            cancelButtonColor: '#aaa',
            cancelButtonText: 'Back',
            reverseButtons: true,
            showCancelButton: true,
            html:
                '<p>Please fill Close Request Form for SPK '+spkId+'</p>'
                + '<br/><p style="text-align: left">Reason:</p>'
                + '<textarea class="form-control" rows="3" id="closeReason" style="width: 472px; height: 109px;" maxlength="250"></textarea>'
                + '<br/>',
            preConfirm: () => {
                if($('#closeReason').val() == ''){
                    Swal.showValidationMessage('Please input close reason...');
                }
            }
        }).then((result) =>{
            if (result.value) {
                confirmCloseRequest(total_gr, $('#closeReason').val(), status);
            }
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

function confirmCloseRequest(actualAmount, closeReason, status){

    //debug(closeReason,status);
    Swal.fire({
        title: "Close-Request Confirmation",
        type: "warning",
        confirmButtonColor: '#dd4b39',
        confirmButtonText: 'Yes, close it!',
        cancelButtonColor: '#aaa',
        cancelButtonText: 'Cancel',
        reverseButtons: true,
        showCancelButton: true,
        html:
            '<p>Close-Request Details</p>'
            +'<div class="row">'
            + '<div class="col-sm-4 text-left">Actual Amount:</div><div class="col-sm-8 text-left"><b>'+actualAmount+'</b></div>'
            + '<div class="col-sm-4 text-left">Close Reason:</div><div class="col-sm-8 text-left"><b>'+closeReason+'</b></div>'
            + '</div>'
            + '<br><p class="text-left"><i>*) Make sure inputted data is correct!</i></p>'
        ,
    }).then((result) => {
        if(result.value){

            let json = genSpkHistoryJson(spkId, status, '' , closeReason, toNumber(actualAmount), '');
            console.log(json);

            //shows loading swal
            Swal.fire({
                title: 'Making Close Request',
                text: 'Please wait while sending request',
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });

            updateStatus(json, spkId, status);
        }
    });
}

function onApproveCloseClick(){
    let status = 'CLOSE_APPROVED';

    Swal.fire({
        title: 'Confirmation',
        text: 'Approve close request SPK ' + spkId +'?',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, approve it!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true
    }).then((result) => {
        if (result.value) {
            Swal.fire({
                title: 'Approving Work Order',
                text: 'Please wait while approving spk ' + spkId + '...',
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });

            let spkNts = document.getElementById("rsnSpk").innerHTML;
            let json = genSpkHistoryJson(spkId, status, '' , spkNts, 0, '');
            console.log(json);
            updateStatus(json, spkId, status);
        }
    });
}

function getSameTypeAssetClass(){

    assetClasses= new Map();
    let assetTypeId = document.getElementById('assetClass').innerHTML.substr(0,4);
    $.ajax({
        type: "GET",
        url: asset_class_url + "?assetTypeId="+assetTypeId,
        success(data){
            for(let i = 0; i < data.length; i ++){
                assetClasses.set(data[i].assetClassId, data[i].assetClassId +' - '+ data[i].assetClassName);
            }
        }
    });
}