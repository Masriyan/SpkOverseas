const base_url = window.location.origin + "/spk-app";
const rules_url = base_url + "/api/rule";
const rules_pagination_url = base_url + "/api/rule";

$(document).ready(function(){

    //set active menu
    setMenuOpen('menuRules');
    setSubMenuActive('menuListOfRules');

    fillDataTable();
});

function fillDataTable(filterRuleId = '', filterAsset = '',filterPo = '',
                       filterAppLevel = '',filterRole = '',filterMinAmt = '', filterMaxAmt = ''){
    //datatable
    let table = $('#apprule-list-table').DataTable({
        "processing": true,
        "serverSide": true,
        "ordering": false, //disable order
        "searching": false, //disable search
        "bLengthChange": false, //remove show entries
        "ajax": {
            "url": rules_pagination_url,
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
                if(filterRuleId != '') data.approvalRulesId = filterRuleId;
                if(filterAsset != '') data.assetTypeId = filterAsset;
                if(filterPo != '') data.poType = filterPo;
                if(filterAppLevel != '') data.approvalLevel = filterAppLevel;
                if(filterRole != '') data.roleId = filterRole;
                if(filterMinAmt != '') data.minAmount = filterMinAmt;
                if(filterMaxAmt != '') data.maxAmount = filterMaxAmt;
            }
        },
        "columns": [
            {"data" : "approvalRulesId"},
            {"data" : "assetTypeId"},
            {"data" : "poType"},
            {"data" : "approvalLevel"},
            {"data" : "roleName"},
            {"data" : "minAmount"},
            {"data" : "maxAmount"},
            {"data" : "countryName"},
            {
                "data" : "approvalRulesId",
                render: function(data,type,row ){
                    return '<button class="btn btn-danger btn-sm" onclick="onClickDeleteRule(\''+row["approvalRulesId"]+','+row["assetTypeId"]+','+row["poType"]+','+row["approvalLevel"]+','+row["minAmount"]+','+row["maxAmount"]+'\')"><i class="fa fa-trash"></i> DELETE</button>';
                          // + ' <button class="btn btn-success btn-sm" onclick="onClickUpdate(\''+row["approvalRulesId"]+','+row["assetTypeId"]+','+row["poType"]+','+row["approvalLevel"]+','+row["minAmount"]+','+row["maxAmount"]+'\')"><i class="fa fa-user"></i> EDIT</button>';
                }
            }
        ],
    });
}

function onFilterSbuClick(){

    let filterRuleId = document.getElementById("filterRuleId").value;
    let filterAsset = document.getElementById("filterAssetType").value;
    let filterPo = document.getElementById("filterPoType").value;
    let filterAppLevel = document.getElementById("filterAppLevel").value;
    let filterRole = document.getElementById("filterRole").value;
    let filterMinAmt = document.getElementById("filterMinAmt").value;
    let filterMaxAmt = document.getElementById("filterMaxAmt").value;

    if(filterRuleId != '' || filterAsset != '' || filterPo != '' || filterAppLevel != ''
        || filterRole != '' || filterMinAmt != ''|| filterMaxAmt != ''){
        $('#apprule-list-table').DataTable().destroy();
        fillDataTable(filterRuleId, filterAsset,filterPo, filterAppLevel,
        filterRole, filterMinAmt, filterMaxAmt);
    }
    else {
        $('#apprule-list-table').DataTable().destroy();
        fillDataTable();
    }
}

function onClearClick(){

    document.getElementById('filterRuleId').value='';
    document.getElementById('filterAssetType').value='';
    document.getElementById('filterPoType').value='';
    document.getElementById('filterAppLevel').value='';
    document.getElementById('filterRole').value='';
    document.getElementById('filterMinAmt').value='';
    document.getElementById('filterMaxAmt').value='';

    $('#apprule-list-table').DataTable().destroy();
    fillDataTable();
}

function onClickUpdate(rule){
    window.open(base_url+"/rules/" + rule +"/");
}

function onClickDeleteRule(rule){

    Swal.fire({
        title: 'Delete Confirmation?',
        html: 'Do you want to delete  <b>'  +rule+ '</b> ? <br/> Once deleted, you won\'t be able to revert this.',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.value) {
            showDeleteLoading();
            deleteRule(rule);
        }
    })

}

function showDeleteLoading(){
    Swal.fire({
        title: 'Deleting Rule',
        text: 'Please wait while deleting rule...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function deleteRule(rule) {

    $.ajax({
        type: "DELETE",
        url: rules_url + '/' + rule+ '/',
        contentType: "application/json",
        async: false,
        success: function (data) {
            if(data.responseCode == 1051){
                Swal.fire({
                    title: 'Deleted!',
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false
                }).then(() =>{
                    onFilterSbuClick();
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessful Delete Rule",
                    text: data.responseMessage,
                    type: "warning",
                    allowOutsideClick: false,
                    confirmButtonText: 'Ok'
                });
            }
        },
        error: function (e) {
            Swal.fire({
                title: "Something went wrong :(",
                text: "Please try again...",
                type: "warning",
                allowOutsideClick: false,
                confirmButtonText: 'Ok'
            });
        }
    });
}
