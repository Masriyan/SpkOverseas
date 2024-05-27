const sbu = $('#sbu');
const vendor = $('#vendor');
const store = $('#store');
const costCenter = $('#costCenter');
const base_url = window.location.origin + "/spk-app";
const errMessage = $('#errMessage');
const errMessageLayout = $('#errMessageLayout');
const username = document.getElementById("loggedInUserName").innerHTML;
const uploadSpkQuotationUrl = base_url + "/api/surat-perintah-kerja/quotation";
const postSpkUrl = base_url +"/api/surat-perintah-kerja";
var uploadedQuotationFile = "";

$(document).ready(function(){

    //set active menu
    setMenuOpen('menuSpk');
    setSubMenuActive('menuCreateSpkO10k');

    $('#approvedQuotationDate').datepicker({
        autoclose: true,
        format: 'dd MM yyyy'
    });

    $('#estimateStoreOpeningDate').datepicker({
        autoclose: true,
        format: 'dd MM yyyy'
    });

    new AutoNumeric('#amount', autoNumericOptionsRupiah);
    new AutoNumeric('#top1Amount', autoNumericOptionsRupiah);
    new AutoNumeric('#top2Amount', autoNumericOptionsRupiah);
    new AutoNumeric('#top3Amount', autoNumericOptionsRupiah);
    new AutoNumeric('#top4Amount', autoNumericOptionsRupiah);
    new AutoNumeric('#top5Amount', autoNumericOptionsRupiah);

    $('#table-vendor').DataTable({
        "ordering": false,
        "bPaginate": true,
        "bLengthChange": false,
        "bFilter": true,
        "bInfo": true,
        "bAutoWidth": false
    });

    $('#table-cost-center').DataTable({
        "ordering": false,
        "bPaginate": true,
        "bLengthChange": false,
        "bFilter": true,
        "bInfo": true,
        "bAutoWidth": false
    });
});

function onSubmitBtnClick(){

    let spkDescription = document.getElementById("spkDescription").value.trim();
    let approvedQuotationDate = document.getElementById("approvedQuotationDate").value;
    let estimateStoreOpeningDate = document.getElementById("estimateStoreOpeningDate").value;
    let companyId = document.getElementById("company").value;
    let sbuId = document.getElementById("sbu").value;
    let vendorId = vendor.text();
    let storeId = document.getElementById("store").value;
    let costCenterId = costCenter.text();
    let assetClassId = document.getElementById("assetClass").value;
    let amount = document.getElementById("amount").value;
    let contactPerson = document.getElementById("contactPerson").value.trim();
    let top1Amount = document.getElementById("top1Amount").value;
    let top2Amount = document.getElementById("top2Amount").value;
    let top3Amount = document.getElementById("top3Amount").value;
    let top4Amount = document.getElementById("top4Amount").value;
    let top5Amount = document.getElementById("top5Amount").value;
    let top1Percentage = document.getElementById("top1Percentage").value;
    let top2Percentage = document.getElementById("top2Percentage").value;
    let top3Percentage = document.getElementById("top3Percentage").value;
    let top4Percentage = document.getElementById("top4Percentage").value;
    let top5Percentage = document.getElementById("top5Percentage").value;
    let top1Message = document.getElementById("top1Message").value.trim();
    let top2Message = document.getElementById("top2Message").value.trim();
    let top3Message = document.getElementById("top3Message").value.trim();
    let top4Message = document.getElementById("top4Message").value.trim();
    let top5Message = document.getElementById("top5Message").value.trim();
    let quotationFile = new FormData();
    let fi = document.getElementById("quotationFile").files;

    let amountNumb = toNumber(document.getElementById("amount").value);

    if(spkDescription == ""){
        errMessage.text("");
        errMessage.append("SPK Description should be filled.");
        errMessageLayout.fadeIn();
    }
    else if(approvedQuotationDate == ""){
        errMessage.text("");
        errMessage.append("Approved Quotation Date should be filled.");
        errMessageLayout.fadeIn();
    }
    else if(estimateStoreOpeningDate == ""){
        errMessage.text("");
        errMessage.append("Estimate Store Opening Date should be filled.");
        errMessageLayout.fadeIn();
    }
    else if(companyId == ""){
        errMessage.text("");
        errMessage.append("Company should be selected.");
        errMessageLayout.fadeIn();
    }
    else if(sbuId == ""){
        errMessage.text("");
        errMessage.append("Sbu should be selected.");
        errMessageLayout.fadeIn();
    }
    else if(vendorId == "" || !vendorId){
        errMessage.text("");
        errMessage.append("Vendor should be selected.");
        errMessageLayout.fadeIn();
    }
    else if(storeId == ""){
        errMessage.text("");
        errMessage.append("Store should be selected.");
        errMessageLayout.fadeIn();
    }
    else if(costCenterId == ""){
        errMessage.text("");
        errMessage.append("Cost Center should be selected.");
        errMessageLayout.fadeIn();
    }
    else if(assetClassId == ""){
        errMessage.text("");
        errMessage.append("Asset Class should be selected.");
        errMessageLayout.fadeIn();
    }
    else if(amount == ""){
        errMessage.text("");
        errMessage.append("Amount should be filled.");
        errMessageLayout.fadeIn();
    }
/*    else if(amountNumb < 10000001){
        errMessage.text("");
        errMessage.append("Amount minimum is 10.000.001, Please input in SPK PO Service Expense");
        errMessageLayout.fadeIn();
    }*/
    else if(contactPerson == ""){
        errMessage.text("");
        errMessage.append("Contact Person should be filled.");
        errMessageLayout.fadeIn();
    }
    else if(fi.length == 0){
        errMessage.text("");
        errMessage.append("Please select Quotation file.");
        errMessageLayout.fadeIn();
    }
    else if(Math.round((fi.item(0).size / 1024)) >= 10240){
        errMessage.text("");
        errMessage.append("Maximum file size is 10MB. Quotation file is too large.");
        errMessageLayout.fadeIn();
    }
    else if(!isTopAmountSequentially(top1Amount, top2Amount, top3Amount, top4Amount, top5Amount) && !isTopPercentageSequentially(top1Percentage, top2Percentage, top3Percentage, top4Percentage, top5Percentage)){
        errMessage.text("");
        errMessage.append("Term of payment should filled sequentially.");
        errMessageLayout.fadeIn();
    }
    else if (isTopValid()){
        errMessageLayout.fadeOut();

        Swal.fire({
            title: 'Submitting Work Order',
            text: 'Please wait while submitting spk...',
            showConfirmButton: false,
            allowOutsideClick: false,
            imageWidth: 100,
            imageHeight: 100,
            imageUrl: base_url+'/images/loading.gif'
        });

        //upload SPK Quotation
        quotationFile.append("quotationFile", $("#quotationFile")[0].files[0]);

        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: uploadSpkQuotationUrl,
            data: quotationFile,
            processData: false, //prevent jQuery from automatically transforming the data into a query string
            contentType: false,
            cache: false,
            timeout: 600000,
            async: false,
            success: function (data) {
                //success upload quotation file to server.
                uploadedQuotationFile = data.toString();
                console.log("SUCCESS : ", data);

                //post SPK to server
                let spkJson = genSpkJson(spkDescription, approvedQuotationDate, estimateStoreOpeningDate, companyId, sbuId, vendorId, storeId, costCenterId, assetClassId,
                    toNumber(amount), contactPerson, toNumber(top1Amount), toNumber(top2Amount), toNumber(top3Amount), toNumber(top4Amount), toNumber(top5Amount),
                    top1Percentage, top2Percentage, top3Percentage, top4Percentage, top5Percentage, top1Message, top2Message, top3Message, top4Message, top5Message, uploadedQuotationFile);
                console.log("Spk Json = ", spkJson);
                postSpk(spkJson);

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
    }
    return false;
}

function onCompanyChange(){

    Swal.fire({
        title: 'Fetching Company Data',
        text: 'Please wait...',
        showConfirmButton: false,
        allowOutsideClick: false,
        onBeforeOpen: () => {
            Swal.showLoading()
        }
    });

    let companyId = document.getElementById("company").value;

    if(companyId){
        setSbu(username,companyId);
        setVendor(companyId);
        setStore(companyId);
        setCostCenterByCompany(companyId);
    }else{
        setSbuEmpty();
        setVendorEmpty();
        setStoreEmpty();
        setCostCenterEmpty();
    }

    Swal.close();
}

function onStoreChange() {
    var companyId = document.getElementById("company").value;
    var storeId = document.getElementById("store").value;

    if(store){
        setCostCenterByCompanyAndStore(companyId, storeId);
    }else{
        setCostCenterEmpty();
    }

}

function setSbu(username , companyId){
    let sbuUrl = base_url +"/api/sbu/comp?username=" + username + "&companyId=" + companyId;

    $.ajax({
        url: sbuUrl,
        type: "GET",
        async:false,
        success: function (data) {
            setSbuEmpty();

            for(let i=0; i<data.length; i++){
                sbu.append('<option value=' + data[i].sbuId + '>' + data[i].sbuCode + ' - ' + data[i].sbuDesc + '</option>');
            }
        }
    });
}

function setSbuEmpty(){
    sbu.empty();
    sbu.append('<option value="">-Select Sbu-</option>');
}

function setVendor(companyId){
    var vendorUrl = base_url +"/api/vendor?companyId=" + companyId;

    $.ajax({
        url: vendorUrl,
        type: "GET",
        async:false,
        success: function (data) {
            setVendorEmpty();

            $('#table-vendor').DataTable().rows().remove().draw();

            if(data!='') {
                $.each(data, function(i, vendor) {
                    $('#table-vendor').DataTable().row.add([
                        data[i].vendorId,
                        data[i].vendorName]);
                });
            }

            $('#table-vendor').DataTable().draw();

            //set data table row click function
            $('#table-vendor').on("click", "tr", function(event){
                //get data on seleted row
                var data = $('#table-vendor').DataTable().row($(this)).data();
                console.log(data[0]);
                onSelectVendor(data[0], data[1]);
            });
        }
    });
}

//select item number from modal
function onSelectVendor(vendorId, vendorName){
    //document.getElementById("vendor").value = vendor;
    vendor.val(vendorId + ' - ' + vendorName);
    vendor.text(vendorId);
    $('#modal-vendor').modal('hide');
}

function setVendorEmpty(){
    vendor.val('');
    vendor.text('');
}

function setStore(companyId){
    let storeUrl = base_url +"/api/store?companyId=" + companyId;

    $.ajax({
        url: storeUrl,
        type: "GET",
        async:false,
        success: function (data) {
            setStoreEmpty();

            for(let i=0; i<data.length; i++){
                store.append('<option value=' + data[i].storeId + '>' + data[i].storeId + ' - ' + data[i].storeName + '</option>');
            }
        }
    });
}

function setStoreEmpty(){
    store.empty();
    store.append('<option value="">-Select Store-</option>');
}

function setCostCenterByCompany(companyId){
    var storeUrl = base_url +"/api/costCenter?companyId=" + companyId;

    $.ajax({
        url: storeUrl,
        type: "GET",
        async:false,
        success: function (data) {
            setCostCenterEmpty();

            $('#table-cost-center').DataTable().rows().remove().draw();

            if(data!='') {
                $.each(data, function(i, vendor) {
                    $('#table-cost-center').DataTable().row.add([
                        data[i].costCenterId,
                        data[i].costCenterName]);
                });
            }

            $('#table-cost-center').DataTable().draw();

            //set data table row click function
            $('#table-cost-center').on("click", "tr", function(event){
                //get data on seleted row
                var data = $('#table-cost-center').DataTable().row($(this)).data();
                console.log(data[0]);
                onSelectCostCenter(data[0], data[1]);
            });

            /*for(var i=0; i<data.length; i++){
                costCenter.append('<option value=' + data[i].costCenterId + '>' + data[i].costCenterId + ' - ' + data[i].costCenterName + '</option>');
            }*/
        }
    });
}

//select item number from modal
function onSelectCostCenter(costCenterId, costCenterName){
    costCenter.val(costCenterId + ' - ' + costCenterName);
    costCenter.text(costCenterId);
    $('#modal-cost-center').modal('hide');
}

function setCostCenterByCompanyAndStore(companyId, storeId){
    var storeUrl = base_url +"/api/costCenter?companyId=" + companyId + "&storeId=" + storeId;

    $.ajax({
        url: storeUrl,
        type: "GET",
        success: function (data) {
            setCostCenterEmpty();

            for(var i=0; i<data.length; i++){
                costCenter.append('<option value=' + data[i].costCenterId + '>' + data[i].costCenterId + ' - ' + data[i].costCenterName + '</option>');
            }
        }
    });
}

function setCostCenterEmpty() {
    costCenter.val('');
    costCenter.text('');
}

//convert form to json
function genSpkJson(spkDescription, approvedQuotationDate, estimateStoreOpeningDate, companyId, sbuId, vendorId, storeId, costCenterId, assetClassId,
                    amount, contactPerson, top1Amount, top2Amount, top3Amount, top4Amount, top5Amount,
                    top1Percentage, top2Percentage, top3Percentage, top4Percentage, top5Percentage,
                    top1Message, top2Message, top3Message, top4Message, top5Message, quotationFile){

    top1Amount = top1Amount == "" ? 0 : top1Amount;
    top2Amount = top2Amount == "" ? 0 : top2Amount;
    top3Amount = top3Amount == "" ? 0 : top3Amount;
    top4Amount = top4Amount == "" ? 0 : top4Amount;
    top5Amount = top5Amount == "" ? 0 : top5Amount;
    top1Percentage = top1Percentage == "" ? 0 : top1Percentage;
    top2Percentage = top2Percentage == "" ? 0 : top2Percentage;
    top3Percentage = top3Percentage == "" ? 0 : top3Percentage;
    top4Percentage = top4Percentage == "" ? 0 : top4Percentage;
    top5Percentage = top5Percentage == "" ? 0 : top5Percentage;

    return '{"spkDescription": "' + spkDescription + '",'
        + '"approvedQuotationDate": "' + changeDateFormat(approvedQuotationDate) + '",'
        + '"estimateStoreOpeningDate": "' + changeDateFormat(estimateStoreOpeningDate) + '",'
        + '"companyId": "' + companyId + '",'
        + '"sbuId": "' + sbuId + '",'
        + '"spkType": "Asset",'
        + '"vendorId": "' + vendorId + '",'
        + '"storeId": "' + storeId + '",'
        + '"costCenterId": "' + costCenterId + '",'
        + '"assetClassId": "' + assetClassId + '",'
        + '"amount": ' + amount + ','
        + '"contactPerson": "' + contactPerson + '",'
        + '"top1Amount": ' + top1Amount + ','
        + '"top2Amount": ' + top2Amount + ','
        + '"top3Amount": ' + top3Amount + ','
        + '"top4Amount": ' + top4Amount + ','
        + '"top5Amount": ' + top5Amount + ','
        + '"top1Percentage": ' + top1Percentage + ','
        + '"top2Percentage": ' + top2Percentage + ','
        + '"top3Percentage": ' + top3Percentage + ','
        + '"top4Percentage": ' + top4Percentage + ','
        + '"top5Percentage": ' + top5Percentage + ','
        + '"top1Message": "' + top1Message + '",'
        + '"top2Message": "' + top2Message + '",'
        + '"top3Message": "' + top3Message + '",'
        + '"top4Message": "' + top4Message + '",'
        + '"top5Message": "' + top5Message + '",'
        + '"quotationFile": "' + quotationFile + '"}';
}

function onInputPayment(){

    let top1Amount = document.getElementById("top1Amount");
    let top2Amount = document.getElementById("top2Amount");
    let top3Amount = document.getElementById("top3Amount");
    let top4Amount = document.getElementById("top4Amount");
    let top5Amount = document.getElementById("top5Amount");
    let top1Percentage = document.getElementById("top1Percentage");
    let top2Percentage = document.getElementById("top2Percentage");
    let top3Percentage = document.getElementById("top3Percentage");
    let top4Percentage = document.getElementById("top4Percentage");
    let top5Percentage = document.getElementById("top5Percentage");
    let top1Message = document.getElementById("top1Message");
    let top2Message = document.getElementById("top2Message");
    let top3Message = document.getElementById("top3Message");
    let top4Message = document.getElementById("top4Message");
    let top5Message = document.getElementById("top5Message");

    if(toNumber(top1Amount.value) != "" || toNumber(top2Amount.value) != "" || toNumber(top3Amount.value) != "" || toNumber(top4Amount.value) != "" || toNumber(top5Amount.value) != ""){
        top1Percentage.disabled = true;
        top2Percentage.disabled = true;
        top3Percentage.disabled = true;
        top4Percentage.disabled = true;
        top5Percentage.disabled = true;

        if(toNumber(top1Amount.value) != "") top1Message.disabled = false; else top1Message.disabled = true;
        if(toNumber(top2Amount.value) != "") top2Message.disabled = false; else top2Message.disabled = true;
        if(toNumber(top3Amount.value) != "") top3Message.disabled = false; else top3Message.disabled = true;
        if(toNumber(top4Amount.value) != "") top4Message.disabled = false; else top4Message.disabled = true;
        if(toNumber(top5Amount.value) != "") top5Message.disabled = false; else top5Message.disabled = true;
    }
    else if(top1Percentage.value != "" || top2Percentage.value != "" || top3Percentage.value != "" || top4Percentage.value != "" || top5Percentage.value != ""){
        top1Amount.disabled = true;
        top2Amount.disabled = true;
        top3Amount.disabled = true;
        top4Amount.disabled = true;
        top5Amount.disabled = true;

        if(top1Percentage.value != "") top1Message.disabled = false; else top1Message.disabled = true;
        if(top2Percentage.value != "") top2Message.disabled = false; else top2Message.disabled = true;
        if(top3Percentage.value != "") top3Message.disabled = false; else top3Message.disabled = true;
        if(top4Percentage.value != "") top4Message.disabled = false; else top4Message.disabled = true;
        if(top5Percentage.value != "") top5Message.disabled = false; else top5Message.disabled = true;
    }else {
        top1Percentage.disabled = false;
        top2Percentage.disabled = false;
        top3Percentage.disabled = false;
        top4Percentage.disabled = false;
        top5Percentage.disabled = false;
        top1Amount.disabled = false;
        top2Amount.disabled = false;
        top3Amount.disabled = false;
        top4Amount.disabled = false;
        top5Amount.disabled = false;
        top1Message.value="";
        top2Message.value="";
        top3Message.value="";
        top4Message.value="";
        top5Message.value="";
        top1Message.disabled = true;
        top2Message.disabled = true;
        top3Message.disabled = true;
        top4Message.disabled = true;
        top5Message.disabled = true;
    }
}

//check term of payment valid
function isTopValid(){

    let top1Amount = toNumber(document.getElementById("top1Amount").value);
    let top2Amount = toNumber(document.getElementById("top2Amount").value);
    let top3Amount = toNumber(document.getElementById("top3Amount").value);
    let top4Amount = toNumber(document.getElementById("top4Amount").value);
    let top5Amount = toNumber(document.getElementById("top5Amount").value);
    let top1Percentage = toNumber(document.getElementById("top1Percentage").value);
    let top2Percentage = toNumber(document.getElementById("top2Percentage").value);
    let top3Percentage = toNumber(document.getElementById("top3Percentage").value);
    let top4Percentage = toNumber(document.getElementById("top4Percentage").value);
    let top5Percentage = toNumber(document.getElementById("top5Percentage").value);
    let amount = toNumber(document.getElementById("amount").value);

    if(top1Amount == "" && top2Amount == "" && top3Amount == "" && top4Amount == "" && top5Amount == ""
        && top1Percentage == "" && top2Percentage == "" && top3Percentage == "" && top4Percentage == "" && top5Percentage == ""){

        errMessage.text("");
        errMessage.append("Term of Payment should be filled.");
        errMessageLayout.fadeIn();

        return false;
    }
    else{

        if(top1Amount != "" || top2Amount != "" || top3Amount != "" || top4Amount != "" || top5Amount != ""){

            top1Amount = top1Amount == "" ? 0 : top1Amount;
            top2Amount = top2Amount == "" ? 0 : top2Amount;
            top3Amount = top3Amount == "" ? 0 : top3Amount;
            top4Amount = top4Amount == "" ? 0 : top4Amount;
            top5Amount = top5Amount == "" ? 0 : top5Amount;

            let totalAmount = parseInt(top1Amount) + parseInt(top2Amount) + parseInt(top3Amount) + parseInt(top4Amount) + parseInt(top5Amount);

            if(totalAmount != amount){

                console.log("totalAmount: " + totalAmount);
                console.log("amount: " + amount);

                errMessage.text("");
                errMessage.append("Term of payment did not equals to amount.");
                errMessageLayout.fadeIn();
                return false;
            }else{
                console.log("true");
                console.log("totalAmount: " + totalAmount);
                console.log("amount: " + amount);
                return true;
            }
        }
        else if(top1Percentage != "" || top2Percentage != "" || top3Percentage != "" || top4Percentage != "" || top5Percentage != ""){

            top1Percentage = top1Percentage == "" ? 0 : top1Percentage;
            top2Percentage = top2Percentage == "" ? 0 : top2Percentage;
            top3Percentage = top3Percentage == "" ? 0 : top3Percentage;
            top4Percentage = top4Percentage == "" ? 0 : top4Percentage;
            top5Percentage = top5Percentage == "" ? 0 : top5Percentage;

            let totalPercentage = parseInt(top1Percentage) + parseInt(top2Percentage) + parseInt(top3Percentage) + parseInt(top4Percentage) + parseInt(top5Percentage);

            if(totalPercentage != 100) {

                errMessage.text("");
                errMessage.append("Total percentage should equals to 100.");
                errMessageLayout.fadeIn();

                return false;
            }else{
                return true;
            }
        }
    }
}

function postSpk(spkJson){

    $.ajax({
        type: "POST",
        url: postSpkUrl,
        contentType: "application/json",
        data: spkJson,
        async: false,
        success: function (data) {
            console.log(data);

            if(data.responseCode == 1000){
                Swal.fire({
                    title: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    showCancelButton: true,
                    html: 'SPK submitted with number <b>' +data.suratPerintahKerja.spkId + '</b>',
                    confirmButtonText: 'View SPk Detail',
                    cancelButtonText: 'Submit another SPK',
                    reverseButtons: true
                }).then((result) =>{
                    if (result.value){
                        location.href = base_url + "/surat-perintah-kerja/" + data.suratPerintahKerja.spkId;
                    }else if(result.dismiss === Swal.DismissReason.cancel){
                        location.href = base_url + "/surat-perintah-kerja/create-new-spk";
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessful Submit Work Order",
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

function isTopAmountSequentially(top1Amount, top2Amount, top3Amount, top4Amount, top5Amount){

    let isSequentially = false;

    if(top5Amount != ''){
        isSequentially = top1Amount != '' && top2Amount != '' && top3Amount != ''&& top4Amount != '';
    }
    else if(top4Amount != ''){
        isSequentially = top1Amount != '' && top2Amount != '' && top3Amount != '';
    }
    else if(top3Amount != ''){
        isSequentially = top1Amount != '' && top2Amount != '';
    }
    else if(top2Amount != ''){
        isSequentially = top1Amount != '';
    }
    else if(top1Amount != ''){
        isSequentially = top1Amount != '';
    }


    return isSequentially;
}

function isTopPercentageSequentially(top1Percentage, top2Percentage, top3Percentage, top4Percentage, top5Percentage){

    let isSequentially = false;

    if(top5Percentage > 0){
        isSequentially = top1Percentage > 0 && top2Percentage > 0 && top3Percentage > 0 && top4Percentage > 0;
    }
    else if(top4Percentage > 0){
        isSequentially = top1Percentage > 0 && top2Percentage > 0 && top3Percentage > 0;
    }
    else if(top3Percentage > 0){
        isSequentially = top1Percentage > 0 && top2Percentage > 0;
    }
    else if(top2Percentage > 0){
        isSequentially = top1Percentage > 0;
    }
    else if(top1Percentage > 0){
        isSequentially = top1Percentage > 0;
    }
    return isSequentially;
}