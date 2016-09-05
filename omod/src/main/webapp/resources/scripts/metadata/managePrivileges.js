var purgePrivilegeDialog = null;

$(document).ready( function() {

	purgePrivilegeDialog = emr.setupConfirmationDialog({
        selector: '#adminui-purge-privilege-dialog',
        actions: {
            cancel: function() {
            	purgePrivilegeDialog.close();
            }
        }
    });

});

function adminui_purgePrivilege(privilegeName, name) {
    jq("#purge-privilege-name").val(privilegeName);
    jq("#purgePrivilegeMessage").text(jq("#purgePrivilegeMessageTemplate").val().replace("{0}", "\""+name+"\""));
    purgePrivilegeDialog.show();
}
