<%
    ui.decorateWith("appui", "standardEmrPage")

    def no = "hiu";
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('coreapps.app.configureMetadata.label')}" , link: '${ui.pageLink("coreapps", "configuremetadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.locations.label")}", link: '${ui.pageLink("adminui","metadata/locations/locationsHome")}'},
        { label: "${ ui.message("adminui.locationHierarchy.label")}" }
    ];

</script>

<script type="text/javascript">



    </script>

<h3>${ ui.message("Location Hierarchy") }

<table id="hierarchicalGrid"></table>

