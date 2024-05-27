const base_url = window.location.origin + "/spk-app";
const countries_url = base_url + "/countries";
const countries_pagination_url = base_url + "/api/countries/";
const comp_api_url = base_url + "/api/countries/";

$(document).ready(function(){

    //set active menu
    setMenuOpen('menuCountries');
    setSubMenuActive('menuListOfCountries');

    fillDataTable();
});

function fillDataTable(filterCountryCode = '', filterCountryName = ''){
    //datatable
    let table = $('#country-list-table').DataTable({
        "processing": true,
        "serverSide": true,
        "ordering": false, //disable order
        "searching": false, //disable search
        "bLengthChange": false, //remove show entries
        "ajax": {
            "url": countries_pagination_url,
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
                if(filterCountryCode != '') data.countryCode = filterCountryCode;
                if(filterCountryName != '') data.countryName = filterCountryName;
            }
        },
        "columns": [
            {"data" : "countryId"},
            {"data" : "countryCode"},
            {"data" : "countryName"},
            {
                "data" : "countryName",
                render: function(data,type,row ){
                    return '<button class="btn btn-success btn-sm" onclick="onClickDetails(\''+row["countryId"]+'\')"><i class="fa fa-user"></i> DETAILS</button>'
                    + '<button class="btn btn-danger btn-sm" style="margin-left:10px;" onclick="onClickDeleteCountry(\''+row["countryCode"]+','+row["countryName"]+'\')"><i class="fa fa-trash"></i> DELETE</button>';

                }
            }
        ],
    });
}

function onFilterCountryClick(){

    let filterCountryCode = document.getElementById("filterCountryCode").value;
    let filterCountryName = document.getElementById("filterCountryName").value;

    if(filterCountryCode != '' || filterCountryName != '' ){
        $('#country-list-table').DataTable().destroy();
        fillDataTable(filterCountryCode, filterCountryName);
    }
    else {
        $('#country-list-table').DataTable().destroy();
        fillDataTable();
    }
}

function onClearClick(){

    document.getElementById('filterCountryCode').value='';
    document.getElementById('filterCountryName').value='';

    $('#country-list-table').DataTable().destroy();
    fillDataTable();
}

function onClickDeleteCountry(countryDelete){

    Swal.fire({
        title: 'Delete Confirmation?',
        html: 'Do you want to delete  <b>'  +countryDelete+ '</b>? <br/> Once deleted, you won\'t be able to revert this.',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.value) {
            showDeleteLoading();
            deleteCountry(countryDelete);
        }
    })

}

function deleteCountry(countryDelete) {

    $.ajax({
        type: "DELETE",
        url: comp_api_url + countryDelete + '/',
        contentType: "application/json",
        async: false,
        success: function (data) {
            if(data.responseCode == 1061){
                Swal.fire({
                    title: 'Deleted!',
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false
                }).then(() =>{
                    onFilterCountryClick();
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessful Delete Country",
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
        title: 'Deleting Country',
        text: 'Please wait while deleting Country...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickDetails(countryDetails){
    window.open(base_url+"/countries/" + countryDetails +"/", '_blank');
}
