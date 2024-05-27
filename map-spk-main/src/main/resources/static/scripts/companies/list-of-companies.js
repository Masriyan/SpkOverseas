const base_url = window.location.origin + "/spk-app";
const companies_url = base_url + "/companies";
const companies_pagination_url = base_url + "/api/companies/";
const comp_api_url = base_url + "/api/companies/";

$(document).ready(function(){

    //set active menu
    setMenuOpen('menuCompanies');
    setSubMenuActive('menuListOfCompanies');

    fillDataTable();
});

function fillDataTable(filterCompanyId = '', filterCompanyName = '', filterSbuId = ''){
    //datatable
    let table = $('#company-list-table').DataTable({
        "processing": true,
        "serverSide": true,
        "ordering": false, //disable order
        "searching": false, //disable search
        "bLengthChange": false, //remove show entries
        "ajax": {
            "url": companies_pagination_url,
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
                if(filterCompanyId != '') data.companyId = filterCompanyId;
                if(filterCompanyName != '') data.companyName = filterCompanyName;
                if(filterSbuId != '') data.sbuId = filterSbuId;
            }
        },
        "columns": [
            {"data" : "companyId"},
            {"data" : "processingGroup"},
            {"data" : "companyName"},
            {"data" : "sbuCode"},
            {"data" : "coa"},
            {"data" : "npwp"},
            {"data" : "address1"},
            {"data" : "address2"},
            {"data" : "city"},
            {"data" : "approvalRulesId"},
            {"data" : "countryName"},
            {
                "data" : "sbuCode",
                render: function(data,type,row ){
                    return '<button class="btn btn-success btn-sm" onclick="onClickDetails(\''+row["companyId"]+','+row["sbuCode"]+','+row["sbuDesc"]+'\')"><i class="fa fa-user"></i> DETAILS</button>'
                    + '<button class="btn btn-danger btn-sm" onclick="onClickDeleteSbu(\''+row["companyId"]+','+row["sbuCode"]+','+row["sbuDesc"]+'\')"><i class="fa fa-trash"></i> DELETE</button>';
                }
            }
        ],
    });
}

function onFilterCompanyClick(){

    let filterCompanyId = document.getElementById("filterCompanyId").value;
    let filterCompanyName = document.getElementById("filterCompanyName").value;
    let filterSbuId = document.getElementById("filterSbuId").value;

    if(filterCompanyId != '' || filterCompanyName != '' || filterSbuId != ''){
        $('#company-list-table').DataTable().destroy();
        fillDataTable(filterCompanyId, filterCompanyName, filterSbuId);
    }
    else {
        $('#company-list-table').DataTable().destroy();
        fillDataTable();
    }
}

function onClearClick(){

    document.getElementById('filterCompanyId').value='';
    document.getElementById('filterCompanyName').value='';
    document.getElementById('filterSbuId').value='';

    $('#company-list-table').DataTable().destroy();
    fillDataTable();
}

function onClickDeleteSbu(sbuDelete){

    Swal.fire({
        title: 'Delete Confirmation?',
        html: 'Do you want to delete  <b>'  +sbuDelete+ '</b>? <br/> Once deleted, you won\'t be able to revert this.',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.value) {
            showDeleteLoading();
            deleteSbu(sbuDelete);
        }
    })

}

function deleteSbu(sbuDelete) {

    $.ajax({
        type: "DELETE",
        url: comp_api_url + sbuDelete + '/',
        contentType: "application/json",
        async: false,
        success: function (data) {
            if(data.responseCode == 1041){
                Swal.fire({
                    title: 'Deleted!',
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false
                }).then(() =>{
                    onFilterCompanyClick();
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessful Delete Sbu / Company",
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

function showDeleteLoading(){
    Swal.fire({
        title: 'Deleting Sbu / Company',
        text: 'Please wait while deleting Sbu / Company...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickDetails(sbuDetails){
    window.open(base_url+"/companies/" + sbuDetails +"/", '_blank');
}
