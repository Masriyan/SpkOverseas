const base_url = window.location.origin + "/spk-app";
const spk_url = base_url + "/surat-perintah-kerja";
const spk_on_process_grouped_by_company_url = base_url +"/api/chart/spk-on-process-grouped-by-company"

$(document).ready(function () {

    let table_spk = $('#table-spk').DataTable({
        "bLengthChange": false, //remove show entries
        "ordering": false, //disable order
    });

    $('#table-spk').off('click.rowClick').on('click.rowClick', 'tr', function () {
        let row = table_spk.row(this).data();
        viewSpkDetail(row[0]);
    });

    setOnProcessChart();

});

function goToListOfSpk() {
    document.getElementById("list-of-spk").scrollIntoView({block: 'start', behavior: 'smooth' });
}

function viewSpkDetail(spkId){
    window.open(spk_url+"/"+spkId, '_blank');
}

function setOnProcessChart(){
    $.ajax({
        type: "GET",
        url: spk_on_process_grouped_by_company_url,
        contentType: 'application/json',
        success: function (data) {
            let ctx = document.getElementById('spkOnProgressChart').getContext('2d');
            let myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: data.labels,
                    datasets: [{
                        label: 'Number of SPK',
                        data: data.numberOfSpks,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)',
                            'rgba(0,128,128, 0.2)',
                            'rgba(255,215,0, 0.2)',
                            'rgba(255,0,255, 0.2)',
                            'rgba(210,105,30, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)',
                            'rgba(255, 159, 64, 1)',
                            'rgba(0,128,128, 1)',
                            'rgba(255,215,0, 1)',
                            'rgba(255,0,255, 1)',
                            'rgba(210,105,30, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true
                            }
                        }]
                    }
                }
            });

        }
    });
}