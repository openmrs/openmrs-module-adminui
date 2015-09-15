<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("uicommons", "angular-resource.min.js")
    ui.includeJavascript("uicommons", "angular-ui/angular-ui-router.min.js")
    ui.includeJavascript("uicommons", "angular-app.js")
    ui.includeJavascript("uicommons", "angular-common.js")
    ui.includeJavascript("uicommons", "angular-common-error.js")
    ui.includeJavascript("uicommons", "services/roleService.js")
    ui.includeJavascript("uicommons", "services/privilegeService.js")
    ui.includeJavascript("uicommons", "filters/display.js")
    ui.includeJavascript("uicommons", "filters/serverDate.js")

    ui.includeJavascript("uicommons", "ngDialog/ngDialog.js")
    
    ui.includeCss("uicommons", "ngDialog/ngDialog.min.css")
	ui.includeCss("adminui", "adminui.css")

    ui.includeJavascript("adminui", "metadata/manageRoles.js")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('adminui.app.configureMetadata.label')}", link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.manageRoles.title")}" }
    ];
    emr.loadMessages([
    	"adminui.role.purge.success",
    	"adminui.role.purge.error",
    	"adminui.role.getRoles.error",
    	"adminui.role.getPrivileges.error",
    	"adminui.role.save.success",
    	"adminui.role.save.error",
    	"adminui.role.purge",
        "uicommons.generalSavedNotification"
    ]);
</script>

<div id="manage-roles">
    <ui-view />
</div>

<script type="text/javascript">
    angular.bootstrap("#manage-roles", [ "manageRoles" ])
</script>