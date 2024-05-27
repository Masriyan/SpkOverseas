const createPurchaseOrderUrl = base_url +"/api/surat-perintah-kerja/purchase-order";

/*
* User click create purchaseOrder order button
* */
function onClickCreatePo(){

    let status = 'PURCHASE_ORDER_CREATED';

    Swal.fire({
        title: 'Confirmation',
        text: 'Create Purchase Order for SPK ' + spkId +'?',
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
                title: 'Creating Purchase Order',
                text: 'Please wait while creating Purchase Order for SPK ' + spkId,
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });
            let json = genSpkHistoryJson(spkId, status,'', '', 0, '');
            console.log(json);

            createPurchaseOrder(json);
        }
    });

}

/*
* User click retry internal order button
* */
function onClickRetryPurchaseOrder(){
    let status = 'PURCHASE_ORDER_CREATED';

    Swal.fire({
        title: 'Confirmation',
        text: 'Retry Create Purchase Order for SPK ' + spkId +'?',
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
                title: 'Creating Purchase Order',
                text: 'Please wait while creating Purchase Order for SPK ' + spkId,
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });
            let json = genSpkHistoryJson(spkId, status,'', '', 0, '');
            console.log(json);

            createPurchaseOrder(json);
        }
    });
}

//call create purchase order api
function createPurchaseOrder(json){
    $.ajax({
        type: "PUT",
        url: createPurchaseOrderUrl,
        contentType: "application/json",
        data: json,
        success: function (data) {

            console.log(data);

            if(data.responseCode == 1003){
                Swal.fire({
                    title: "Success",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    html: 'Purchase Order Creation successful with number <b>' + data.suratPerintahKerja.purchaseOrder + '</b> ',
                    confirmButtonText: 'Ok'
                }).then((result) =>{
                    if (result.value){
                        location.href = base_url + "/surat-perintah-kerja/" + spkId;
                    }
                });
            }
            //purchase order creation failed
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