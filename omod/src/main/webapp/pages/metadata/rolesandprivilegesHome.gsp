<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('coreapps.app.configureMetadata.label')}" , link: '${ui.pageLink("coreapps", "configuremetadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.rolesAndPrivileges.title")}" }
    ];
</script>


<h3>${  ui.message("adminui.rolesAndPrivileges.title") }</h3>


<div id="tasks">


    <a class="button app big" href="${ ui.pageLink("adminui", "metadata/roles/manageRoles") }">
            <i class="icon-list"></i>
            ${ ui.message("adminui.manageRoles.title") }
    </a>

    <a class="button app big" href="${ ui.pageLink("adminui", "metadata/privileges/managePrivileges") }">
            <i class="icon-tags"></i>
            ${ ui.message("adminui.managePrivileges.title") }
    </a>

</div>