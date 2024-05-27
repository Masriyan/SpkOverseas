const base_url = window.location.origin + "/spk-app";
const postCountryUrl = base_url +"/api/countries";
const listCountriesUrl = base_url + "/countries"
const newCountryUrl = base_url + "/countries/create-new-country";

$(document).ready(function(){
    //set active menu
    setMenuOpen('menuCountries');
    setSubMenuActive('menuAddNewCountry');
});

function onSubmitCountry(){

    let countryCode = document.getElementById('countryCode').value;
    let countryName = document.getElementById('countryName').value;

    //for printing
    let label1 = document.getElementById('label1').value;
    let label2 = document.getElementById('label2').value;
    let label3 = document.getElementById('label3').value;
    let label4 = document.getElementById('label4').value;
    let label5 = document.getElementById('label5').value;
    let label6 = document.getElementById('label6').value;
    let label7 = document.getElementById('label7').value;
    let label8 = document.getElementById('label8').value;
    let label9 = document.getElementById('label9').value;
    let label10 = document.getElementById('label10').value;
    let label11 = document.getElementById('label11').value;
    let label12 = document.getElementById('label12').value;
    let label13 = document.getElementById('label13').value;
    let label14 = document.getElementById('label14').value;
    let label15 = document.getElementById('label15').value;
    let label16 = document.getElementById('label16').value;
    let label17 = document.getElementById('label17').value;
    let label18 = document.getElementById('label18').value;
    let label19 = document.getElementById('label19').value;
    let label20 = document.getElementById('label20').value;
    let label21 = document.getElementById('label21').value;
    let label22 = document.getElementById('label22').value;
    let label23 = document.getElementById('label23').value;
    let label24 = document.getElementById('label24').value;
    let label25 = document.getElementById('label25').value;
    let label26 = document.getElementById('label26').value;
    let label27 = document.getElementById('label27').value;
    let label28 = document.getElementById('label28').value;
    let label29 = document.getElementById('label29').value;
    let label30 = document.getElementById('label30').value;
    let label31 = document.getElementById('label31').value;



    //loading swal
    Swal.fire({
        title: 'Submitting New Country',
        text: 'Please wait while submitting Country...',
        showConfirmButton: false,
        allowOutsideClick: false,
        imageWidth: 100,
        imageHeight: 100,
        imageUrl: base_url+'/images/loading.gif'
    });

    let json = genCountryJson(countryCode, countryName,label1,label2,label3,label4,label5,label6,label7,label8,label9,label10,label11,label12,label13,label14,label15,label16,label17,label18,label19,label20,label21,label22,label23,label24,label25,label26,label27,label28,label29,label30,label31);

    postCountry(json);

    return false;
}

function postCountry(json){

    $.ajax({
        type: "POST",
        url: postCountryUrl,
        contentType: "application/json",
        data: json,
        async: false,
        success: function (data) {
            console.log(data);
            if(data.responseCode == 1060){
                Swal.fire({
                    title: data.responseMessage,
                    type: "success",
                    allowOutsideClick: false,
                    showCancelButton: true,
                    html: 'Country <b>'+data.country.countryCode+' , ' + data.country.countryName + '</b> is submitted',
                    confirmButtonText: 'View Countries',
                    cancelButtonText: 'Add Another',
                    reverseButtons: true
                }).then((result) =>{
                    if (result.value){
                        location.href = listCountriesUrl;
                    }else if(result.dismiss === Swal.DismissReason.cancel){
                        location.href = newCountryUrl;
                    }
                });
            }
            else{
                Swal.fire({
                    title: "Unsuccessful Submit Country",
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

function genCountryJson(countryCode, countryName,label1,label2,label3,label4,label5,label6,label7,label8,label9,label10,label11,label12,label13,label14,label15,label16,label17,label18,label19,label20,label21,label22,label23,label24,label25,label26,label27,label28,label29,label30,label31) {
    return '{ '
            + '"countryCode" : "'+countryCode+'",'
            + '"countryName" : "'+countryName+'",'
            + '"label1" : "'+label1+'",'
            + '"label2" : "'+label2+'",'
            + '"label3" : "'+label3+'",'
            + '"label4" : "'+label4+'",'
            + '"label5" : "'+label5+'",'
            + '"label6" : "'+label6+'",'
            + '"label7" : "'+label7+'",'
            + '"label8" : "'+label8+'",'
            + '"label9" : "'+label9+'",'
            + '"label10" : "'+label10+'",'
            + '"label11" : "'+label11+'",'
            + '"label12" : "'+label12+'",'
            + '"label13" : "'+label13+'",'
            + '"label14" : "'+label14+'",'
            + '"label15" : "'+label15+'",'
            + '"label16" : "'+label16+'",'
            + '"label17" : "'+label17+'",'
            + '"label18" : "'+label18+'",'
            + '"label19" : "'+label19+'",'
            + '"label20" : "'+label20+'",'
            + '"label21" : "'+label21+'",'
            + '"label22" : "'+label22+'",'
            + '"label23" : "'+label23+'",'
            + '"label24" : "'+label24+'",'
            + '"label25" : "'+label25+'",'
            + '"label26" : "'+label26+'",'
            + '"label27" : "'+label27+'",'
            + '"label28" : "'+label28+'",'
            + '"label29" : "'+label29+'",'
            + '"label30" : "'+label30+'",'
            + '"label31" : "'+label31+'"'
            + '}';
}
