<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("adminui", "account.css")
%>
<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.accountManager.label")}" }
    ];
</script>


<h3>${  ui.message("adminui.app.accountManager.label") }</h3>


<div id="tasks">

    <a class="button app big" href="${ ui.pageLink("adminui", "account/viewAccounts") }">
            <i class="icon-list"></i>
            ${ ui.message("adminui.viewAccount.accountManagement.label") }
    </a>
    
    <a class="button app big" href="${ ui.pageLink("adminui", "account/viewRoles") }">
            <i class="icon-user"></i>
            ${ ui.message("adminui.viewRoles.accountManagement.label") }
    </a>
    
    <a class="button app big" href="${ ui.pageLink("adminui", "account/viewPrivileges") }">
            <i class="icon-key"></i>
            ${ ui.message("adminui.viewPrivileges.accountManagement.label") }
    </a>
    
</div>