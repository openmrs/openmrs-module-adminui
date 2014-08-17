<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.locationManager.label")}" }
    ];
</script>


<h3>${  ui.message("adminui.app.locationManager.label") }</h3>


<div id="tasks">


    <a class="button app big" href="${ ui.pageLink("adminui", "location/viewLocations") }">
            <i class="icon-list"></i>
            ${ ui.message("adminui.viewLocation.locationManagement.label") }
    </a>
    
    <a class="button app big" href="${ ui.pageLink("adminui", "location/viewLocationTags") }">
            <i class="icon-tags"></i>
            ${ ui.message("adminui.viewLocationTags.locationManagement.label") }
    </a>
    
    <a class="button app big" href="${ ui.pageLink("adminui", "location/viewLocationHierarchy") }">
            <i class="icon-sitemap"></i>
            ${ ui.message("adminui.viewLocationHierarchy.locationManagement.label") }
    </a>
    
    <a class="button app big" href="${ ui.pageLink("adminui", "location/viewLocationAttributeTypes") }">
            <i class="icon-quote-right"></i>
            ${ ui.message("adminui.viewLocationAttributeTypes.locationManagement.label") }
    </a>

</div>