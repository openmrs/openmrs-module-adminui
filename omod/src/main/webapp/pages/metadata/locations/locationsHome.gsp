<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('coreapps.app.configureMetadata.label')}" , link: '${ui.pageLink("coreapps", "configuremetadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.locations.label")}" }
    ];
</script>


<h3>${  ui.message("adminui.locations.label") }</h3>


<div id="tasks">


    <a class="button app big" href="${ ui.pageLink("adminui", "metadata/locations/manageLocations") }">
            <i class="icon-list"></i>
            ${ ui.message("adminui.manageLocations.label") }
    </a>

    <a class="button app big" href="${ ui.pageLink("adminui", "metadata/locations/manageLocationTags") }">
            <i class="icon-tags"></i>
            ${ ui.message("adminui.manageLocationTags.label") }
    </a>

    <a class="button app big" href="${ ui.pageLink("adminui", "metadata/locations/locationHierarchy") }">
            <i class="icon-sitemap"></i>
            ${ ui.message("adminui.locationHierarchy.label") }
    </a>

    <a class="button app big" href="${ ui.pageLink("adminui", "metadata/locations/manageLocationAttributeTypes") }">
            <i class="icon-quote-right"></i>
            ${ ui.message("adminui.manageLocationAttributeTypes.label") }
    </a>

</div>