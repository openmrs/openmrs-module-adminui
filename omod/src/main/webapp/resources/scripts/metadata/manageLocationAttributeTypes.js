var retireLocationAttributeTypeDialog = null;
var purgeLocationAttributeTypeDialog = null;

$(document).ready( function() {

    retireLocationAttributeTypeDialog = emr.setupConfirmationDialog({
        selector: '#adminui-retire-location-attribute-type-dialog',
        actions: {
            cancel: function() {
                retireLocationAttributeTypeDialog.close();
            }
        }
    });

    purgeLocationAttributeTypeDialog = emr.setupConfirmationDialog({
        selector: '#adminui-purge-location-attribute-type-dialog',
        actions: {
            cancel: function() {
                purgeLocationAttributeTypeDialog.close();
            }
        }
    });

});

function adminui_retireLocationAttributeType(locationAttributeTypeId, name) {
    jq("#retire-location-attribute-type-id").val(locationAttributeTypeId);
    jq("#retireLocationAttributeTypeMessage").text(jq("#retireLocationAttributeTypeMessageTemplate").val().replace("{0}", "\""+name+"\""));
    retireLocationAttributeTypeDialog.show();
}

function adminui_restoreLocationAttributeType(locationAttributeTypeId) {
     jq("#adminui-restore-form-"+locationAttributeTypeId).submit();
}

function adminui_purgeLocationAttributeType(locationAttributeTypeId, name) {
    jq("#purge-location-attribute-type-id").val(locationAttributeTypeId);
    jq("#purgeLocationAttributeTypeMessage").text(jq("#purgeLocationAttributeTypeMessageTemplate").val().replace("{0}", "\""+name+"\""));
    purgeLocationAttributeTypeDialog.show();
}
