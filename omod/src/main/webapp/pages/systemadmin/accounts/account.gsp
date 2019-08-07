<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("uicommons", "angular-resource.min.js")
    ui.includeJavascript("uicommons", "angular-app.js")
    ui.includeJavascript("uicommons", "angular-common.js")
    ui.includeJavascript("uicommons", "angular-common-error.js")
    ui.includeJavascript("uicommons", "angular-sanitize.min.js")
    ui.includeJavascript("uicommons", "services/personService.js")
    ui.includeJavascript("uicommons", "services/providerService.js")
    ui.includeJavascript("uicommons", "services/userService.js")
    ui.includeJavascript("uicommons", "filters/display.js")
    ui.includeJavascript("uicommons", "filters/serverDate.js")
    ui.includeJavascript("uicommons", "ngDialog/ngDialog.js")

    ui.includeJavascript("adminui", "systemadmin/account.js")
    ui.includeJavascript("adminui", "directives/shouldMatch.js")

    ui.includeCss("uicommons", "ngDialog/ngDialog.min.css")
    ui.includeCss("adminui", "systemadmin/account.css")

    def createAccount = (account.person.personId == null ? true : false);
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('coreapps.app.systemAdministration.label')}" , link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}'},
        { label: "${ ui.message("adminui.manageAccounts.label")}", link: '${ui.pageLink("adminui", "systemadmin/accounts/manageAccounts")}' },
        { label: "${ ui.message((createAccount) ? 'adminui.addAccount.label' : 'adminui.editAccount.label')}" }

    ];

    contextPath = '${ui.contextPath()}';

    emr.loadMessages([
        "error.password.match"
    ]);
    <% if(createAccount) { %>
        initAccountDetails(${personJson}, ${userJson}, ${providerJson}, ${otherAccountData.addUserAccount},
            ${otherAccountData.addProviderAccount});
    <% } %>

    setMessages(${messages});
</script>

<h2>${ ui.message((createAccount) ? 'adminui.addAccount.label' : 'adminui.editAccount.label')}</h2>

<% if(createAccount) { %>
<div id="adminui-accounts" ng-app="adminui.createAccount" ng-controller="AccountController">

<form name="accountForm" class="simple-form-ui" novalidate method="post" action="">
<% } else { %>

<%
        def createAuditInfo = '';
        def changeAuditInfo = '';
        if(account.creator) {
            createAuditInfo = ui.message('adminui.createdByOn', ui.encodeJavaScriptAttribute(ui.format(account.creator)), ui.format(account.dateCreated));
        }
        if(account.changedBy) {
            changeAuditInfo = ui.message('adminui.changedByOn', ui.encodeJavaScriptAttribute(ui.format(account.changedBy)), ui.format(account.dateChanged));
        }
%>

    <div id="account-audit-info" class="right adminui-auditInfo"
         ng-controller="AccountAuditInfoController" ng-init="createAuditInfo='${createAuditInfo}';
            changeAuditInfo='${changeAuditInfo}'; personUuid='${account.person.uuid}'">

        <fieldset class="right">
            <legend class="w-auto">${ui.message('adminui.auditInfo')}</legend>
            <p>
                <span class="adminui-label">${ui.message('general.uuid')}:</span> ${ account.person.uuid }
            </p>
            <p ng-bind-html="createAuditInfo"></p>
            <p ng-bind-html="changeAuditInfo"></p>
        </fieldset>

    </div>
<% } %>

    <div class="adminui-account-section">
        ${ ui.includeFragment("adminui", "systemadmin/accounts/personDetails") }
    </div>

    <div class="adminui-account-section adminui-section-padded-top">
        ${ ui.includeFragment("adminui", "systemadmin/accounts/userDetails") }
    </div>

    <div class="adminui-account-section adminui-section-padded-top">
        ${ ui.includeFragment("adminui", "systemadmin/accounts/providerDetails") }
    </div>

<% if(createAccount) { %>
    <div class="adminui-account-section adminui-section-padded-top">
        <input id="adminui-account-cancel" type="button" class="cancel" value="${ ui.message('general.cancel')}"
               onclick="window.location='${ui.pageLink("adminui", "systemadmin/accounts/manageAccounts")}'" />
        <input type="submit" class="confirm" id="save-button" value="${ ui.message("general.save") }"
               ng-disabled="accountForm.\$invalid || (!addUserAccount && !addProviderAccount)" />
    </div>
</form>

</div>
<% } %>

<script type="text/javascript">
    angular.bootstrap("#account-audit-info", [ 'adminui.accountAuditInfo' ]);
</script>