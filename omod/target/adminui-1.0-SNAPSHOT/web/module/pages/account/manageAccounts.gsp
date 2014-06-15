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


<div id="apps">
    <a class="button app big" href="${ ui.pageLink("adminui", "account/account") }">
        <div class="task">
            <i class="icon-plus"></i>
            ${ ui.message("adminui.createAccount.accountManagement.label") }
        </div>
    </a>
</div>



<div id="apps">
    <a class="button app big" href="${ ui.pageLink("adminui", "account/viewAccounts") }">
        <div class="task">
            <i class="icon-list"></i>
            ${ ui.message("adminui.viewAccount.accountManagement.label") }
        </div>
    </a>
</div>