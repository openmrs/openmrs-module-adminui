<%
    ui.includeJavascript("adminui", "fragments/systemadmin/userDetails.js")

    def createAccount = (account.person.personId == null ? true : false);
%>

<% if(!createAccount) { %>
<script type="text/javascript">
    //This function is in userDetails.js
    initUserDetails(${uuidAndUserMapJson}, ${privilegeLevelsJson}, ${capabilitiesJson},
            '${ui.message('general.yes')}', '${ui.message('general.no')}');
</script>

<div id="adminui-user-details" ng-controller="UserDetailsController">

    <form name="userDetailsForm" class="simple-form-ui" novalidate>
        <% } %>
        <fieldset class="adminui-account-fieldset">
            <legend class="w-auto"><b>${ ui.message("adminui.user.account.details") }</b></legend>
            <% if(createAccount) { %>
            <input id="adminui-addUserAccount" type="checkbox" name="addUserAccount" value="true"
                <% if (otherAccountData.getAddUserAccount()) { %> checked='checked'<% } %>
                ng-model="addUserAccount">

            ${ ui.message("adminui.account.addUserAccount") }
            <div ng-show="addUserAccount">
            ${ui.includeFragment("adminui", "systemadmin/accounts/userFormFields")}
            </div>
            <% } %>

            <% if(!createAccount && account.userAccounts.size == 1) { %>
            <div id="adminui-first-user-details" ng-class="{'invisible':inEditMode}">
                <div class="adminui-first-user-ele">
                    <button id="adminui-addFirstUser" type="button" ng-click="add('${account.userAccounts[0].uuid}')">
                    ${ui.message("adminui.account.addUser.account")}
                    </button>
                </div>
                <div id="adminui-first-user" class="hidden adminui-first-user-ele">
                    ${ui.includeFragment("adminui", "systemadmin/accounts/userTabContentPane",
                        [user: account.userAccounts[0]])}
                </div>
            </div>
            <% } %>

            <% if(!createAccount && account.userAccounts.size > 1) { %>
            <div id="adminui-users">
                <ul>
                    <% account.userAccounts.each { %>
                    <li ng-class="{'ui-state-disabled':inEditMode}">
                        <a href="#${ it.uuid }" <% if(!it.userId) { %> ng-click="add('${it.uuid}')"
                           ng-show="!editing && !inEditMode"
                           title="${ ui.message("adminui.account.addAnotherUserAccount") }"<% } %>>
                            <% if(it.userId) { %>
                            <span ng-class="{retired: uuidUserMap['${it.uuid}'].retired}">
                                {{getTabLabel('${it.uuid}')}}
                            </span>&nbsp;&nbsp;
                            <% } else { %>
                            <i class="icon-plus add-action right ng-class:{'invisible':inEditMode}"></i>
                            <% } %>
                        </a>
                    </li>
                    <% } %>
                </ul>

                <% account.userAccounts.each { %>
                <div id="${it.uuid}">
                    ${ui.includeFragment("adminui", "systemadmin/accounts/userTabContentPane", [user: it])}
                </div>
                <% } %>
            </div>
            <% } %>

        </fieldset>
        <% if(!createAccount) { %>
    </form>

    <script id="retireUserTemplate" type="text/ng-template">
    ${ui.includeFragment("adminui", "systemadmin/accounts/retireUserDialog")}
    </script>

</div>

<script type="text/javascript">
    angular.bootstrap("#adminui-user-details", [ 'adminui.userDetails' ]);
</script>
<% } %>