const base_url = window.location.origin + "/spk-app";
const spk_url = base_url + "/surat-perintah-kerja";
const spk_pagination_url = base_url + "/api/surat-perintah-kerja/";
const spk_genexcl_url = base_url + "/api/surat-perintah-kerja/excel";

$(document).ready(function(){

    //set active menu
    setMenuOpen('menuSpk');
    setSubMenuActive('menuListOfSpk');

    $('#filterApprovedQuotationDate').datepicker({
        autoclose: true,
        format: 'dd MM yyyy'
    });

    setTableFilterVendor();
    fillDataTable();
});

function fillDataTable(filterSpkId = '', filterSpkDescription = '', filterCompanyId = '',filterSbuId = '', filterApprovedQuotationDate = '', filterStoreId = '', filterVendorId = '', filterStatus = ''){
    //datatable
    let table = $('#spk-list-table').DataTable({
        "processing": true,
        "serverSide": true,
        "ordering": false, //disable order
        "searching": false, //disable search
        "bLengthChange": false, //remove show entries
        "ajax": {
            "url": spk_pagination_url,
            "type": "GET",
            "async": false,
            "data": function(data){

                //set page number.
                let start = data.start;
                let length = data.length;
                let pageNumber = start == 0 ? 1 : (start/length) +1;

                //delete unnecessary params.
                delete data.columns;
                delete data.search;
                delete data.start;
                delete data.length;
                delete data._;

                //adding query param
                data.pageNumber = pageNumber;
                if(filterSpkId != '') data.spkId = filterSpkId;
                if(filterSpkDescription != '') data.spkDescription = filterSpkDescription;
                if(filterCompanyId != '') data.companyId = filterCompanyId;
                if(filterSbuId != '') data.sbuId = filterSbuId;
                if(filterApprovedQuotationDate != '') data.approvedQuotationDate = changeDateFormat(filterApprovedQuotationDate);
                if(filterStoreId != '') data.storeId = filterStoreId;
                if(filterVendorId != '') data.vendorId = filterVendorId;
                if(filterStatus != '') data.status = filterStatus;
            }
        },
        "columns": [
            {
                "data" : "spkId",
                "className": "text-center"
            },
            {"data" : "spkDescription"},
            {
                "data" : "companyId",
                "className": "text-center"
            },
            {
                "data" : "sbuCode",
                "className": "text-center"
            },
            {
                "data" : "storeId",
                "className": "text-center"
            },
            {
                "data" : "vendorId",
                "className": "text-center"
            },
            {
                "data" : "assetNo",
                "className": "text-center"
            },
            {
                "data" : "internalOrder",
                "className": "text-center"
            },
            {
                "data" : "purchaseOrder",
                "className": "text-center"
            },
            {
                "data" : "amount",
//                render: $.fn.dataTable.render.number( '.', ',', 2, 'Rp. ' ),
                render: $.fn.dataTable.render.number( '.', ',', 2, '' ),
                "className": "text-right"
            },
            {
                "data" : null,
                render: function(data, type, row){
                    return toCurrency((data["grAmount1"] + data["grAmount2"] + data["grAmount3"] + data["grAmount4"] + data["grAmount5"]));
                    //return $.fn.dataTable.render.number( '.', ',', 2, 'Rp. ' );
                },
                "className": "text-right"
            },
            {
                "data" : "actualAmount",
//                render: $.fn.dataTable.render.number( '.', ',', 2, 'Rp. ' ),
                render: $.fn.dataTable.render.number( '.', ',', 2, '' ),
                "className": "text-right"
            },
            {
                "data" : "status",
                "className": "text-center"
            }
        ],
    });

    $('#spk-list-table').off('click.rowClick').on('click.rowClick', 'tr', function () {
        let row = table.row(this).data();
        goToSpkDetail(row.spkId);
    });
}

function onGenExcel(){

    let filterSpkId = document.getElementById("filterSpkId").value;
    let filterSpKDescription = document.getElementById("filterSpkDescription").value;
    let filterCompanyId = document.getElementById("filterCompanyId").value;
    let filterSbuId = document.getElementById("filterSbuId").value;
    let filterApprovedQuotationDate = document.getElementById("filterApprovedQuotationDate").value;
    let filterStoreId = document.getElementById("filterStoreId").value;
    let filterVendorId =  $("#filterVendorId").text();
    let filterStatus = document.getElementById("filStatus").value;

    if(filterSpkId != '' || filterSpKDescription != '' || filterCompanyId != '' || filterSbuId != '' || filterApprovedQuotationDate != '' || filterStoreId != '' || filterVendorId != '' || filterStatus != ''){
        creatExl(filterSpkId, filterSpKDescription, filterCompanyId, filterSbuId, filterApprovedQuotationDate, filterStoreId, filterVendorId, filterStatus);
    }
    else {
        creatExl();
    }
}

function creatExl(filterSpkId = '', filterSpkDescription = '', filterCompanyId = '',filterSbuId = '', filterApprovedQuotationDate = '', filterStoreId = '', filterVendorId = '', filterStatus = ''){
    //create file Excel
    console.log("Start Here !!");

    let urlExc = spk_genexcl_url;
    urlExc += '?';
    let sign = urlExc.charAt(urlExc.length-1);

    console.log("Current URL = "+urlExc);

    var label  = ["spkId=", "spkDescription=", "companyId=", "sbuId=", "approvedQuotationDate=", "storeId=", "vendorId=", "status="];
    var filter = [filterSpkId, filterSpkDescription, filterCompanyId, filterSbuId, filterApprovedQuotationDate, filterStoreId, filterVendorId, filterStatus];

    for (i = 0; i < filter.length; i++) {
        if(filter[i] != ''){
    	    if(sign == '?'){
    		    urlExc += label[i] + filter[i];
    			sign = urlExc.charAt(urlExc.length-1);
    		}
    		else if(sign != '?')
    		{
    			urlExc += "&"+label[i] + filter[i];
    			sign = urlExc.charAt(urlExc.length-1);
    		}
    	}
    }

    console.log("URL = "+urlExc);
    console.log("Sign = "+sign);

    let today = new Date();
    let datetime = today.getFullYear()+'`'+(today.getMonth()+1)+'`'+
                   today.getDate()+'-'+today.getHours()+'/:'+
                   today.getMinutes()+'/:'+today.getSeconds();

    fetch(urlExc)
      .then(resp => resp.blob())
      .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'SPK_'+datetime+'.xlsx';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        alert('Your Excel File has Downloaded ..'); // or you know, something with better UX...
      })
      .catch(() => alert('Oh No! Something Wrong..'));
}

function onFilterClick(){

    let filterSpkId = document.getElementById("filterSpkId").value;
    let filterSpKDescription = document.getElementById("filterSpkDescription").value;
    let filterCompanyId = document.getElementById("filterCompanyId").value;
    let filterSbuId = document.getElementById("filterSbuId").value;
    let filterApprovedQuotationDate = document.getElementById("filterApprovedQuotationDate").value;
    let filterStoreId = document.getElementById("filterStoreId").value;
    let filterVendorId =  $("#filterVendorId").text();
    let filterStatus = document.getElementById("filStatus").value;

    if(filterSpkId != '' || filterSpKDescription != '' || filterCompanyId != '' || filterSbuId != '' || filterApprovedQuotationDate != '' || filterStoreId != '' || filterVendorId != '' || filterStatus != ''){
        $('#spk-list-table').DataTable().destroy();
        fillDataTable(filterSpkId, filterSpKDescription, filterCompanyId, filterSbuId, filterApprovedQuotationDate, filterStoreId, filterVendorId, filterStatus);
    }
    else {
        $('#spk-list-table').DataTable().destroy();
        fillDataTable();
    }
}

function goToSpkDetail(spkId){
    window.open(spk_url+"/"+spkId, '_blank');
}

function onClearClick(){

    document.getElementById('filterSpkId').value='';
    document.getElementById('filterCompanyId').value='';
    document.getElementById('filterSbuId').value='';
    document.getElementById('filterApprovedQuotationDate').value='';
    document.getElementById('filterStoreId').value='';
    document.getElementById('filterVendorId').value='';
    document.getElementById('filStatus').value='';

    $('#spk-list-table').DataTable().destroy();
    fillDataTable();
}

function setTableFilterVendor() {
    $('#table-filter-vendor').DataTable({
        "ordering": false,
        "bPaginate": true,
        "bLengthChange": false,
        "bFilter": true,
        "bInfo": true,
        "bAutoWidth": false
    });

    //set data table filter-vendor row click function
    $('#table-filter-vendor').on("click", "tr", function (event) {
        //get data on seleted row
        var data = $('#table-filter-vendor').DataTable().row($(this)).data();
        console.log(data[0]);
        onSelectVendor(data[0], data[1]);
    });
}

function onSelectVendor(vendorId, vendorName){
    $("#filterVendorId").val(vendorId + ' - ' + vendorName);
    $("#filterVendorId").text(vendorId);

    $('#modal-filter-vendor').modal('hide');
}