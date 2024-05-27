const base_url = window.location.origin + "/spk-app";
const sbus_url = base_url + "/sbus";
const sbus_pagination_url = base_url + "/api/sbu";

$(document).ready(function(){

    //set active menu
    setMenuOpen('menuSbus');
    setSubMenuActive('menuListOfSbus');

    fillDataTable();
});

function fillDataTable(filterSbuId = '', filterSbuDesc = ''){
    //datatable
    let table = $('#sbu-list-table').DataTable({
        "processing": true,
        "serverSide": true,
        "ordering": false, //disable order
        "searching": false, //disable search
        "bLengthChange": false, //remove show entries
        "ajax": {
            "url": sbus_pagination_url,
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
                if(filterSbuId != '') data.sbuId = filterSbuId;
                if(filterSbuDesc != '') data.sbuDesc = filterSbuDesc;
            }
        },
        "columns": [
            {"data" : "sbuId"},
            {"data" : "sbuCode"},
            {"data" : "sbuDesc"},
            {"data" : "companyId"},
            {"data" : "approvalRulesId"},
        ],
    });
}

function onFilterSbuClick(){

    let filterSbuId = document.getElementById("filterSbuId").value;
    let filterSbuDesc = document.getElementById("filterSbuDesc").value;

    if(filterSbuId != '' || filterSbuDesc != ''){
        $('#sbu-list-table').DataTable().destroy();
        fillDataTable(filterSbuId, filterSbuDesc);
    }
    else {
        $('#sbu-list-table').DataTable().destroy();
        fillDataTable();
    }
}

function onClearClick(){

    document.getElementById('filterSbuId').value='';
    document.getElementById('filterSbuDesc').value='';

    $('#sbu-list-table').DataTable().destroy();
    fillDataTable();
}
