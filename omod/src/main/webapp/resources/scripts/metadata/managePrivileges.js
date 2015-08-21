var retirePrivilegeDialog = null;
var purgePrivilegeDialog = null;

$(document).ready( function() {

	retirePrivilegeDialog = emr.setupConfirmationDialog({
        selector: '#adminui-retire-privilege-dialog',
        actions: {
            cancel: function() {
            	retirePrivilegeDialog.close();
            }
        }
    });

	purgePrivilegeDialog = emr.setupConfirmationDialog({
        selector: '#adminui-purge-privilege-dialog',
        actions: {
            cancel: function() {
            	purgePrivilegeDialog.close();
            }
        }
    });

});

function adminui_retirePrivilege(privilegeName, name) {
    jq("#retire-privilege-name").val(privilegeName);
    jq("#retirePrivilegeMessage").text(jq("#retirePrivilegeMessage").text().replace("{0}", "\""+name+"\""));
    retirePrivilegeDialog.show();
}

function adminui_restorePrivilege(privilegeName) {
    console.log(privilegeName);
    jq("#adminui-restore-form-"+privilegeName).submit();
}

function adminui_purgePrivilege(privilegeName, name) {
    console.log(privilegeName);
    jq("#purge-privilege-name").val(privilegeName);
    jq("#purgePrivilegeMessage").text(jq("#purgePrivilegeMessage").text().replace("{0}", "\""+name+"\""));
    purgePrivilegeDialog.show();
}
