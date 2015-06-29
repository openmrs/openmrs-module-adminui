<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('coreapps.app.configureMetadata.label')}" , link: '${ui.pageLink("coreapps", "configuremetadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.encounters.title")}" }
    ];
</script>


<h3>${  ui.message("adminui.encounters.title") }</h3>


<div id="tasks">


    <a class="button app big" href="${ ui.pageLink("adminui", "metadata/encounters/encounterTypes/manageEncounterTypes") }">
            <i class="icon-list"></i>
            ${ ui.message("adminui.manageEncounterTypes.title") }
    </a>

</div>