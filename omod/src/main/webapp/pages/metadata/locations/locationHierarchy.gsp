<%
    ui.decorateWith("appui", "standardEmrPage")

    def no = "hiu";
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('adminui.app.configureMetadata.label')}" , link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.locationHierarchy.label")}" }
    ];

</script>

<script type="text/javascript">



    </script>

<h3>${ ui.message("Location Hierarchy") }

<table id="hierarchicalGrid"></table>

