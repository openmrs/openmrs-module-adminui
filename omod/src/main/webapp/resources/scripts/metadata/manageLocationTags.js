var retireLocationTagDialog = null;
var purgeLocationTagDialog = null;

$(document).ready( function() {

    retireLocationTagDialog = emr.setupConfirmationDialog({
        selector: '#adminui-retire-location-tag-dialog',
        actions: {
            cancel: function() {
                retireLocationTagDialog.close();
            }
        }
    });

    purgeLocationTagDialog = emr.setupConfirmationDialog({
        selector: '#adminui-purge-location-tag-dialog',
        actions: {
            cancel: function() {
                purgeLocationTagDialog.close();
            }
        }
    });

});

function adminui_retireLocationTag(locationTagId, name) {
    jq("#retire-location-tag-id").val(locationTagId);
    jq("#retireLocationTagMessage").text(jq("#retireLocationTagMessage").text().replace("{0}", "\""+name+"\""));
    retireLocationTagDialog.show();
}

function adminui_restoreLocationTag(locationTagId) {
     jq("#adminui-restore-form-"+locationTagId).submit();
}

function adminui_purgeLocationTag(locationTagId, name) {
    jq("#purge-location-tag-id").val(locationTagId);
    jq("#purgeLocationTagMessage").text(jq("#purgeLocationTagMessage").text().replace("{0}", "\""+name+"\""));
    purgeLocationTagDialog.show();
}
