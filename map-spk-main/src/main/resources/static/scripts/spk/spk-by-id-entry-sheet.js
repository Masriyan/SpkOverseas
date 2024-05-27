const createEntrySheetUrl = base_url +"/api/surat-perintah-kerja/entry-sheet";

/*
* User click confirm button
* */
function onConfirmBtnClick(termOfPayment, topAmount){

    Swal.fire({
        title: 'GR Amount Confirmation',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, set GR Amount!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true,
        html:
            '<p>Confirm payment '+termOfPayment+'?</p>'
            + '<input type="text" class="form-control input-lg" id="grAmount" style="text-align: end;" maxlength="17"/>'
            + '<p>*) Change amount if GR amount did not match with Term of Payment...</p>',
        onOpen: function () {
            //set input amount to autonumeric
            new AutoNumeric('#grAmount', autoNumericOptionsRupiah);
            //set default value
            AutoNumeric.getAutoNumericElement('#grAmount').set(topAmount);
        },
        preConfirm: () => {
            if($('#grAmount').val() == ''){
                Swal.showValidationMessage('GR Amount can not be empty')
            }
            else if(toNumber($('#grAmount').val()) > topAmount){
                Swal.showValidationMessage('GR Amount can not be greater than Term of Payment!')
            }
            else if(toNumber($('#grAmount').val()) == 0 ){
                Swal.showValidationMessage('GR Amount can not be fill with 0 (zero)')
            }
        }
    }).then((result) => {
        if(result.value){
            let grAmount = $('#grAmount').val();

            console.log("topAmount: " + topAmount);
            console.log("grAmount: " + grAmount);

            reConfirmGr(topAmount, termOfPayment, grAmount);
        }
    });
}

function reConfirmGr(topAmount, termOfPayment, grAmount){

    let swalType, swalMessage;

    if(toNumber(grAmount) < topAmount){
        swalType = 'warning';
        swalMessage = '<p>GR Amount is not equals to Term of Payment. Term of payment is <b>'+toCurrency(topAmount)+'</b>. Inputted GR Amount is <b>'+grAmount+'</b> </p>'
            +'<p>Are you sure want to confirm this payment?</p> '
    }
    else{
        swalType = 'question';
        swalMessage = '<p>GR Amount equals to Term of Payment (<b>'+grAmount+'</b>)</p>'
            +'<p>Are you sure want to confirm this payment?</p>'
    }

    Swal.fire({
        title: 'GR Amount Confirmation',
        type: swalType,
        html: swalMessage,
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, confirm GR!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true,
    })
    .then((result) => {
        if(result.value){
            showLoadingEntrySheet(termOfPayment);
            let json = genSpkHistoryJsonPayment(spkId, termOfPayment, toNumber(grAmount));

            createEntrySheet(json, termOfPayment);
        }
    });
}

function genSpkHistoryJsonPayment(spkId, termOfPayment, amount){

    let json = '{"spkId": "' + spkId +'",';

    switch (termOfPayment) {
        case 1:
            json += ' "grAmount1": ' + amount + '}';
            break;
        case 2:
            json += ' "grAmount2": ' + amount + '}';
            break;
        case 3:
            json += ' "grAmount3": ' + amount + '}';
            break;
        case 4:
            json += ' "grAmount4": ' + amount + '}';
            break;
        case 5:
            json += ' "grAmount5": ' + amount + '}';
            break;
    }

    return json;
}

function createEntrySheet(json, termOfPayment){
    $.ajax({
        type: "PUT",
        url: createEntrySheetUrl,
        contentType: "application/json",
        data: json,
        success: function (data) {

            console.log(data);

            if(data.responseCode == 1004 ||data.responseCode == 1005){

                //set entry sheet value
                let entrySheet;
                switch (termOfPayment) {
                    case 1:
                        entrySheet = data.suratPerintahKerja.entrySheet1;
                        break;
                    case 2:
                        entrySheet = data.suratPerintahKerja.entrySheet2;
                        break;
                    case 3:
                        entrySheet = data.suratPerintahKerja.entrySheet3;
                        break;
                    case 4:
                        entrySheet = data.suratPerintahKerja.entrySheet4;
                        break;
                    case 5:
                        entrySheet = data.suratPerintahKerja.entrySheet5;
                        break;
                }

                Swal.fire({
                    title: "Success",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    html: 'Entry Sheet Creation successful with number <b>' + entrySheet + '</b> ',
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        location.href = base_url + "/surat-perintah-kerja/" + spkId;
                    }
                });
            }
            //all payment confirmed, but not equals to SPK Amount
            else if(data.responseCode == 1006){
                Swal.fire({
                    title: "Warning",
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
            //entry sheet creation failed
            else{
                Swal.fire({
                    title: "Error",
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

function showLoadingEntrySheet(termOfPayment){
    Swal.fire({
        title: 'Confirming Work Order Payment',
        text: 'Please wait while confirming payment ' + termOfPayment + '...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}
