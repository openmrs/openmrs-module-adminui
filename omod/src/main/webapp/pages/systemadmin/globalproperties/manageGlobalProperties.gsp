<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("uicommons", "angular-resource.min.js")
    ui.includeJavascript("uicommons", "angular-ui/angular-ui-router.min.js")
    ui.includeJavascript("uicommons", "angular-app.js")
    ui.includeJavascript("uicommons", "angular-common.js")
    ui.includeJavascript("uicommons", "angular-common-error.js")
    ui.includeJavascript("uicommons", "services/systemSettingService.js")
    ui.includeJavascript("uicommons", "filters/display.js")
    ui.includeJavascript("uicommons", "filters/serverDate.js")

    ui.includeJavascript("uicommons", "ngDialog/ngDialog.js")
    ui.includeCss("uicommons", "ngDialog/ngDialog.min.css")
    ui.includeCss("adminui", "adminui.css")

    ui.includeJavascript("adminui", "systemadmin/manageSystemSettings.js")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('coreapps.app.systemAdministration.label')}", link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}'},
        { label: "${ ui.message("adminui.manageSystemSettings.title")}" }
    ];
    emr.loadMessages([
        "adminui.saved",
        "adminui.savedChanges",
        "adminui.purged",
        "adminui.save.fail",
        "adminui.saveChanges.fail",
        "adminui.purge.fail"
    ]);
</script>

<div id="manage-system-settings">
    <ui-view/>
</div>

<script type="text/javascript">
    angular.bootstrap("#manage-system-settings", [ "manageSystemSettings" ])
</script>