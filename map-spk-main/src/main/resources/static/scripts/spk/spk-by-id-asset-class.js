const updateSpkAssetUrl = base_url +"/api/surat-perintah-kerja/asset-class";

function onChangeAssetClassClick(){
    let status = 'UPDATE_ASSET_CLASS_ID';

    Swal.fire({
        title: 'Confirmation',
        text: 'Select Asset Class',
        type: 'question',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Yes, change it!',
        cancelButtonText: 'No, cancel.',
        input: 'select',
        inputOptions: assetClasses,
        inputPlaceholder: 'Select Asset Class',
        reverseButtons: true,
        inputValidator: (value) => {
            if (!value){
                return 'Please select a asset class!'
            }
        }
    }).then((result) => {
        if(result.value){
            let assetClassId = result.value;
            let assetName = assetClasses.get(assetClassId);
            let note = 'Changed asset class from ' + document.getElementById('assetClass').innerHTML + ' to ' +assetName;

            let json = genSpkHistoryJson(spkId, status, '', note, 0, assetClassId);

            console.log(json);

            Swal.fire({
                title: 'Changing Asset Class',
                text: 'Please wait while changing spk ' + spkId + '...',
                showConfirmButton: false,
                allowOutsideClick: false,
                imageWidth: 100,
                imageHeight: 100,
                imageUrl: base_url+'/images/loading.gif'
            });

            changeAssetClass(json);
        }
    });
}

function changeAssetClass(json) {

    $.ajax({
        type: "PUT",
        url: updateSpkAssetUrl,
        contentType: "application/json",
        data: json,
        success: function (data) {

            console.log(data);

            if(data.responseCode == 1007){
                Swal.fire({
                    title: "SPK is Updated",
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
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