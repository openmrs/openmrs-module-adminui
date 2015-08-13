<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("uicommons", "angular-resource.min.js")
    ui.includeJavascript("uicommons", "angular-ui/angular-ui-router.min.js")
    ui.includeJavascript("uicommons", "angular-app.js")
    ui.includeJavascript("uicommons", "angular-common.js")
    ui.includeJavascript("uicommons", "services/encounterTypeService.js")
    ui.includeJavascript("uicommons", "filters/display.js")
    ui.includeJavascript("uicommons", "filters/serverDate.js")

    ui.includeJavascript("uicommons", "ngDialog/ngDialog.js")
    ui.includeCss("uicommons", "ngDialog/ngDialog.min.css")
    ui.includeCss("adminui", "adminui.css")

    ui.includeJavascript("adminui", "metadata/manageEncounterTypes.js")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('adminui.app.configureMetadata.label')}", link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.manageEncounterTypes.title")}" }
    ];
    emr.loadMessages([
        "adminui.saved",
        "adminui.savedChanges",
        "adminui.retired",
        "adminui.restored",
        "adminui.purged",
        "adminui.save.fail",
        "adminui.saveChanges.fail",
        "adminui.retire.fail",
        "adminui.restore.fail",
        "adminui.purge.fail"
    ]);
</script>

<style type="text/css">
    /* TODO move this to a shared SCSS file */
    tr.retired {
        text-decoration: line-through;
    }

    input.ng-touched.ng-invalid {
        background: #ff8888;
    }
</style>

<div id="manage-encounter-types">
    <ui-view/>
</div>

<script type="text/javascript">
    angular.bootstrap("#manage-encounter-types", [ "manageEncounterTypes" ])
</script>