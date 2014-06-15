jq(function() {
    jq('input[type=text]').first().focus();

    jq('#cancel-button').click(function() {
        window.history.back();
    });
});



function checkConfirmButton(){
    var patient1Id = jq("#patient1").val();
    var patient2Id = jq("#patient2").val();

    if( patient1Id > 0 &&  patient2Id > 0 &&
        (patient1Id != patient2Id)){
        enableButton();
        jq("#confirm-button").focus();
    } else {
        disableButton();
    }
}

function enableButton(){
    jq("#confirm-button").removeAttr("disabled");
    jq("#confirm-button").removeClass("disabled");
}

function disableButton(){
    jq("#confirm-button").attr("disabled","disabled");
    jq("#confirm-button").addClass('disabled');
}