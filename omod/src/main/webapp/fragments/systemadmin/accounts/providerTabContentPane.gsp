<%
    config.require('provider');

    def provider = config.provider;
    def uuid = provider.uuid ?: '';
%>

<div class="provider-${uuid} ${provider.providerId ? '' : 'hidden'}">
    <i class="icon-edit edit-action right" title="${ ui.message("general.edit") }"
       ng-show="!inEditMode && !uuidProviderMap['${uuid}'].retired" ng-click="edit('${uuid}')"></i>

    <table class="adminui-form-table" cellpadding="0" cellspacing="0">
        <tr class="adminui-border-bottom">
            <th valign="top">${ui.message("Provider.identifier")}</th>
            <td valign="top" ng-class="{retired: uuidProviderMap['${uuid}'].retired}">
                {{uuidProviderMap['${uuid}'].identifier}}
            </td>
        </tr>
        <tr>
            <th valign="top">${ui.message("adminui.providerRole.label")}</th>
            <td valign="top" ng-class="{retired: uuidProviderMap['${uuid}'].retired}">
                {{roles[uuidProviderMap['${uuid}'].providerRole]}}
            </td>
        </tr>
    </table>
    <div class="adminui-section-padded-top" ng-show="!inEditMode">
        <button type="button" value="${ui.message('general.restore')}" ng-disabled="requesting"
                ng-click="uuidProviderMap['${uuid}'].retired ? restore('${uuid}') : retire('${uuid}')">
            {{uuidProviderMap['${uuid}'].retired ? '${ui.message("general.restore")}' : '${ui.message("general.retire")}'}}
        </button>
    </div>
</div>

<div class="provider-${uuid} ${provider.providerId ? 'hidden' : ''}">
    <div class="right">
        <button id="adminui-provider-cancel${uuid}" type="button" class="cancel"
            ng-click="cancel('${uuid}'<% if(!provider.providerId) { %>, true<% } %>)">
            ${ ui.message("general.cancel") }
        </button>
        <button id="adminui-provider-save${uuid}" type="button" class="confirm"
            ng-click="save('${uuid}'<% if(!provider.providerId) { %>, '${account.person.uuid}'<% } %>)"
            ng-disabled="providerDetailsForm['identifier${uuid}'].\$invalid
             || providerDetailsForm['providerRole${uuid}'].\$invalid || requesting">
            ${ ui.message("general.save") }
        </button>
    </div>
    ${ui.includeFragment("adminui", "systemadmin/accounts/providerFormFields", [provider: provider])}
</div>