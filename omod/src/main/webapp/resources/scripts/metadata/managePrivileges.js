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
    jq("#purgePrivilegeMessage").text(jq("#purgePrivilegeMessage").text().replace("{0}", "\""+name+"\""));
    purgePrivilegeDialog.show();
}
