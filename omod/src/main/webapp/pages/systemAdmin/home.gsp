<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.systemAdministration.label")}" }
    ];
</script>


<h3>${  ui.message("adminui.app.systemAdministration.label") }</h3>


<div id="tasks">

    <a class="button app big" href="${ ui.pageLink("adminui", "systemAdmin/globalProps") }">
            <i class="icon-globe"></i>
            ${ ui.message("adminui.globalProps.systemAdministration.label") }
    </a>


</div>