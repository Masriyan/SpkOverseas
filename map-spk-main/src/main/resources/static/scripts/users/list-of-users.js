const base_url = window.location.origin + "/spk-app";
const users_api_url = base_url + "/api/users/";

$(document).ready(function(){
    //set active menu
    setMenuOpen('menuUsers');
    setSubMenuActive('menuListOfUsers');

    fillDataTable();
});

function fillDataTable(filterUserName = '', filterName = '', filterRoleId = 0){
    //datatable
    let table = $('#user-list-table').DataTable({
        "processing": true,
        "serverSide": true,
        "ordering": false, //disable order
        "searching": false, //disable search
        "bLengthChange": false, //remove show entries
        "ajax": {
            "url": users_api_url,
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
                data.roleId = filterRoleId;
                if(filterUserName != '') data.userName = filterUserName;
                if(filterName != '') data.name = filterName;
            }
        },
        "columns": [
            {"data" : "userName"},
            {"data" : "firstName"},
            {"data" : "lastName"},
            {"data" : "email"},
            {"data" : "roleName"},
            {
                "data" : "enabled",
                render: function (enabled) {
                    if(enabled) return "ACTIVE";
                    else return  "INACTIVE";
                }
            },
            {
                "data" : "userName",
                render: function(data, ){
                    return '<button class="btn btn-danger btn-sm" onclick="onClickDeleteUser(\''+data+'\')"><i class="fa fa-trash"></i> DELETE</button>'
                        + ' <button class="btn btn-success btn-sm" onclick="onClickDetails(\''+data+'\')"><i class="fa fa-user"></i> DETAILS</button>';
                }
            }
        ],
    });
}

function onFilterUserClick(){

    let filterUserName = document.getElementById("filterUserName").value;
    let filterName = document.getElementById("filterName").value;
    let filterRoleId = document.getElementById("filterRoleId").value;

    if(filterUserName != '' || filterName != '' || filterRoleId != ''){
        $('#user-list-table').DataTable().destroy();
        fillDataTable(filterUserName, filterName, filterRoleId);
    }
    else {
        $('#user-list-table').DataTable().destroy();
        fillDataTable();
    }
}

function onClearClick(){

    document.getElementById('filterUserName').value='';
    document.getElementById('filterName').value='';
    document.getElementById('filterRoleId').value='';

    $('#user-list-table').DataTable().destroy();
    fillDataTable();
}

function onClickDeleteUser(userName){

    Swal.fire({
        title: 'Delete Confirmation?',
        html: 'Do you want to delete  <b>'  +userName + '</b>? <br/> Once deleted, you won\'t be able to revert this.',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.value) {
            showDeleteLoading();
            deleteUser(userName);
        }
    })

}

function deleteUser(userName) {

    $.ajax({
        type: "DELETE",
        url: users_api_url + userName + '/',
        contentType: "application/json",
        async: false,
        success: function (data) {
            if(data.responseCode == 1013){
                Swal.fire({
                    title: 'Deleted!',
                    text: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false
                }).then(() =>{
                    onFilterUserClick();
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessful Delete User",
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
        title: 'Deleting User',
        text: 'Please wait while deleting user...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });
}

function onClickDetails(userName){
    window.open(base_url+"/users/" + userName +"/", '_blank');
}