<%
    ui.includeJavascript("adminui", "fragments/systemadmin/personDetails.js")

    def genderOptions = [ [label: ui.message("Person.gender.male"), value: 'M'],
                      [label: ui.message("Person.gender.female"), value: 'F']]

    def createAccount = (account.person.personId == null ? true : false);
    def formName = createAccount ? "accountForm" : "personDetailsForm"

%>

<% if(!createAccount) { %>
<script type="text/javascript">
    initPersonDetails(${customPersonAttributeJson});
</script>

<div id="adminui-person-details" ng-controller="EditPersonDetailsController"
     ng-init="init('${account.person.uuid}', '${account.gender}', '${account.person.personName.uuid}',
                   '${ui.encodeJavaScriptAttribute(account.familyName)}', '${ui.encodeJavaScriptAttribute(account.givenName)}', '${ui.message("Person.gender.male")}',
                   '${ui.message("Person.gender.female")}')">

    <form name="personDetailsForm" class="simple-form-ui" novalidate ng-submit="save()">
    <div class="adminui-form-person-details-panel">
<% } %>
        <fieldset class="<% if(createAccount) { %>adminui-account-fieldset<% } else { %>adminui-form-person-details-panel<% } %>">
        <legend class="w-auto"><b>${ ui.message("adminui.person.details") }</b></legend>
        <% if(!createAccount) { %>
        <div class="adminui-account-person-details">
            <i class="icon-edit edit-action right" title="${ ui.message("general.edit") }"
                ng-show="!inEditMode" ng-click="edit()"></i>

            <table class="adminui-form-table" cellpadding="0" cellspacing="0" bgcolor="red">
                <tr class="adminui-border-bottom">
                    <th valign="top">${ui.message("PersonName.familyName")}</th>
                    <td valign="top">{{person.familyName}}</td>
                </tr>
                <tr class="adminui-border-bottom">
                    <th valign="top">${ui.message("adminui.person.givenName")}</th>
                    <td valign="top">{{person.givenName}}</td>
                </tr>
                <tr>
                    <th valign="top">${ui.message("Person.gender")}</th>
                    <td valign="top">{{genders[person.gender]}}</td>
                </tr>

                <% customPersonAttributeViewFragments.each { fragment ->
                    if(fragment.extensionParams.type == 'personAttribute') { %>
                    ${ ui.includeFragment(fragment.extensionParams.provider, fragment.extensionParams.fragment, [
                            label : ui.message(fragment.extensionParams.label),
                            personId : account.person.personId,
                            personUuid : account.person.uuid,
                            initialValue : ui.message(account.getPersonAttribute(fragment.extensionParams.uuid) != null ?
                                    account.getPersonAttribute(fragment.extensionParams.uuid).value : '')
                    ])}
                    <% }
                } %>

            </table>
        </div>
        <% } %>
        <div class="adminui-account-person-details ${!createAccount ? 'hidden' : ''}">
            <% if(!createAccount) { %>
            <div class="right">
                <button id="adminui-person-cancel" type="button" class="cancel" ng-click="cancel()">
                    ${ ui.message("general.cancel") }
                </button>
                <button ng-disabled="personDetailsForm.\$invalid || requesting" id="adminui-person-save" type="submit" class="confirm">
                    ${ ui.message("general.save") }
                </button>
            </div>
            <% } %>
            ${ ui.includeFragment("uicommons", "field/text", [
                label: ui.message("PersonName.familyName")+"<span class='adminui-text-red'>*</span>",
                id: "adminui-familyName",
                formFieldName: "familyName",
                initialValue: ui.encodeHtmlContent((account.familyName ?: '')),
                otherAttributes:['ng-model': 'person.familyName', 'required': '', 'ng-maxlength': propertyMaxLengthMap['familyName']]
            ])}
            <span class="field-error" ng-show="${formName}.familyName.\$dirty && ${formName}.familyName.\$invalid">
                <span ng-show="${formName}.familyName.\$error.required">
                    ${ui.message("adminui.field.required")}
                </span>
                <span ng-show="${formName}.familyName.\$error.maxlength">
                    ${ui.message("adminui.field.exceeded.maxChars", propertyMaxLengthMap['familyName'])}
                </span>
            </span>

            ${ ui.includeFragment("uicommons", "field/text", [
                label: ui.message("adminui.person.givenName")+"<span class='adminui-text-red'>*</span>",
                id: "adminui-givenName",
                formFieldName: "givenName",
                initialValue: ui.encodeHtmlContent((account.givenName ?: '')),
                otherAttributes:['ng-model': 'person.givenName', 'required':'', 'ng-maxlength': propertyMaxLengthMap['givenName']]
            ])}
            <span class="field-error" ng-show="${formName}.givenName.\$dirty && ${formName}.givenName.\$invalid">
                <span ng-show="${formName}.givenName.\$error.required">
                    ${ui.message("adminui.field.required")}
                </span>
                <span ng-show="${formName}.givenName.\$error.maxlength">
                    ${ui.message("adminui.field.exceeded.maxChars", propertyMaxLengthMap['givenName'])}
                </span>
            </span>

            ${ ui.includeFragment("uicommons", "field/radioButtons", [
                label: ui.message("Person.gender")+"<span class='adminui-text-red'>*</span>",
                id: "adminui-gender",
                formFieldName: "gender",
                initialValue: account.gender,
                options: genderOptions,
                otherAttributes:['ng-model': 'person.gender', 'required':'']
            ])}
            <span class="field-error" ng-show="${formName}.gender.\$dirty && ${formName}.gender.\$invalid">
                <span ng-show="${formName}.gender.\$error.required">
                    ${ui.message("adminui.field.required")}
                </span>
            </span>

            <% customPersonAttributeEditFragments.each { fragment ->
                if(fragment.extensionParams.type == 'personAttribute') {%>
                ${ ui.includeFragment(fragment.extensionParams.provider, fragment.extensionParams.fragment, [
                        formFieldName : fragment.extensionParams.formFieldName,
                        id : fragment.extensionParams.formFieldName,
                        title : ui.message(fragment.extensionParams.title),
                        label : ui.message(fragment.extensionParams.label),
                        initialValue : ui.message(account.getPersonAttribute(fragment.extensionParams.uuid) != null ?
                                account.getPersonAttribute(fragment.extensionParams.uuid).value : ''),
                        personId : account.person ? account.person.personId : "",
                        personUuid : account.person ? account.person.uuid : ""
                ])}
            <% }
            }%>

        </div>
        </fieldset>
<% if(!createAccount) { %>
    </div>
    </form>

</div>

<script type="text/javascript">
    angular.bootstrap("#adminui-person-details", [ 'adminui.personDetails' ]);
</script>
<% } %>