
$(document).ready(function(){
    let printContents = document.getElementById('spk').innerHTML;
    let originalContents = document.body.innerHTML;

    document.body.innerHTML = printContents;

    setTimeout(function(){window.print();},1000);

    document.body.innerHTML = originalContents;
});


