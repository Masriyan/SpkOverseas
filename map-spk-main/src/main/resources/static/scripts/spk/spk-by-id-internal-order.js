const createInternalOrderUrl = base_url +"/api/surat-perintah-kerja/internal-order";
const closeInternalOrderUrl = base_url +"/api/surat-perintah-kerja/close-internal-order";
/*
* User click create internal order button
* */
function onClickInternalOrder(){
    let status = 'INTERNAL_ORDER_CREATED';

    Swal.fire({
        title: 'Confirmation',
        text: 'Create Internal Order for SPK ' + spkId +'?',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, create it!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true
    }).then((result) => {
        if(result.value){
            Swal.fire({
                title: 'Creating Internal Order',
                text: 'Please wait while creating Internal Order for SPK ' + spkId,
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });
            let json = genSpkHistoryJson(spkId, status,'', '', 0, '');
            console.log(json);

            createInternalOrder(json);
        }
    });
}

/*
* User click retry internal order button
* */
function onClickRetryInternalOrder(){
    let status = 'INTERNAL_ORDER_CREATED';

    Swal.fire({
        title: 'Confirmation',
        text: 'Retry Create Internal Order for SPK ' + spkId +'?',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, retry!',
        cancelButtonText: 'No, cancel.',
        reverseButtons: true
    }).then((result) => {
        if(result.value){
            Swal.fire({
                title: 'Creating Internal Order',
                text: 'Please wait while creating Internal Order for SPK ' + spkId,
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });
            let json = genSpkHistoryJson(spkId, status,'', '', 0, '');
            console.log(json);

            createInternalOrder(json);
        }
    });
}

//call create internal order api
function createInternalOrder(json){
    $.ajax({
        type: "PUT",
        url: createInternalOrderUrl,
        contentType: "application/json",
        data: json,
        success: function (data) {

            console.log(data);

            if(data.responseCode == 1002){
                Swal.fire({
                    title: "Success",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    html: 'Internal Order Creation successful with number <b>' + data.suratPerintahKerja.internalOrder + '</b> ',
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        location.href = base_url + "/surat-perintah-kerja/" + spkId;
                    }
                });
            }
            //internal order creation failed
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

function onCloseIoInSapClick(){

    let status = 'CLOSE_INTERNAL_ORDER';

    Swal.fire({
        title: 'Close Internal Order',
        text: 'Please wait while closing Internal Order for SPK ' + spkId,
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });

    let json = genSpkHistoryJson(spkId, status,'', '', 0, '');

    $.ajax({
        type: "PUT",
        url: closeInternalOrderUrl,
        contentType: "application/json",
        data: json,
        success: function (data) {

            console.log(data);

            if(data.responseCode == 1008){
                Swal.fire({
                    title: "Success",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        location.href = base_url + "/surat-perintah-kerja/" + spkId;
                    }
                });
            }
            //internal order creation failed
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