<%
    config.require('user');

    def user = config.user;
    def uuid = user.uuid ?: '';

%>

<div class="user-${uuid} ${user.userId ? '' : 'hidden'}">
    <i class="icon-edit edit-action right" title="${ ui.message("general.edit") }"
       ng-show="!inEditMode && !uuidUserMap['${uuid}'].retired" ng-click="edit('${uuid}')"></i>

    <table class="adminui-form-table" cellpadding="0" cellspacing="0">
        <tr class="adminui-border-bottom">
            <th valign="top">${ui.message("User.username")}</th>
            <td valign="top" ng-class="{retired: uuidUserMap['${uuid}'].retired}">
                {{uuidUserMap['${uuid}'].username}}
            </td>
        </tr>
        <tr class="adminui-border-bottom">
            <th valign="top">${ui.message("adminui.account.privilegeLevel")}</th>
            <td valign="top" ng-class="{retired: uuidUserMap['${uuid}'].retired}">
                {{privilegeLevels[uuidUserMap['${uuid}'].privilegeLevel]}}
            </td>
        </tr>
        <tr class="adminui-border-bottom">
            <th valign="top">
                ${ui.message("adminui.account.user.isSupposedToChangePassword")}
            </th>
            <td valign="top" ng-class="{retired: uuidUserMap['${uuid}'].retired}">
                {{yesOrNo[uuidUserMap['${uuid}'].userProperties.forcePassword]}}
            </td>
        </tr>

        <% customUserPropertyViewFragments.each { fragment -> %>
            ${ ui.includeFragment(fragment.extensionParams.provider, fragment.extensionParams.fragment, [
                    label : ui.message(fragment.extensionParams.label),
                    personId : account.person.personId,
                    personUuid : account.person.uuid,
                    userId : user.userId,
                    userUuid : user.uuid
            ])}
        <% }
        %>

        <tr>
            <th valign="top">${ui.message("adminui.account.capabilities")}</th>
            <td valign="top" ng-class="{retired: uuidUserMap['${uuid}'].retired}">
                {{printCapabilities(uuidUserMap['${uuid}'].capabilities)}}
            </td>
        </tr>
    </table>
    <div class="adminui-section-padded-top" ng-show="!inEditMode">
        <button type="button" value="${ui.message('general.restore')}" ng-disabled="requesting"
                ng-click="uuidUserMap['${uuid}'].retired ? restore('${uuid}') : retire('${uuid}')">
            {{uuidUserMap['${uuid}'].retired ? '${ui.message("general.restore")}' : '${ui.message("general.retire")}'}}
        </button>
    </div>
</div>

<div class="user-${uuid} ${user.userId ? 'hidden' : ''}">
    <div class="right">
        <button id="adminui-user-cancel${uuid}" type="button" class="cancel"
            ng-click="cancel('${uuid}'<% if(!user.userId) { %>, true<% } %>)">
            ${ ui.message("general.cancel") }
        </button>
        <button id="adminui-user-save${uuid}" type="button" class="confirm"
            ng-click="save('${uuid}'<% if(!user.userId) { %>, '${account.person.uuid}'<% } %>)"
            ng-disabled="userDetailsForm['username${uuid}'].\$invalid
                        || userDetailsForm['privilegeLevel${uuid}'].\$invalid
                        || userDetailsForm['password${uuid}'].\$invalid
                        || userDetailsForm['confirmPassword${uuid}'].\$invalid || requesting">
            ${ ui.message("general.save") }
        </button>
    </div>
    ${ui.includeFragment("adminui", "systemadmin/accounts/userFormFields", [user: user])}
</div>