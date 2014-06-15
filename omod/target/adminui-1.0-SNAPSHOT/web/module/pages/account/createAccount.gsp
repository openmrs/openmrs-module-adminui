<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("adminui", "account.css")
%>
<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.accountManager.label")}", link: '${ui.pageLink("adminui", "account/manageAccounts")}' },
        { label: "${ ui.message("adminui.createAccount.accountManagement.label")}" }
    ];
</script>

<h3>${  ui.message("adminui.createAccount.accountManagement.label") }</h3>

<a href="${ ui.pageLink("adminui", "account/account") }">
    <button id="create-account-button">${ ui.message("adminui.createAccount") }</button>
</a>