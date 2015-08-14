<%
    def identifier = ''
    def providerRoleUuid = ''
    def providerUuid = ''
    def formName = (account.person.personId == null) ? "accountForm" : "providerDetailsForm"

    if(config.provider){
        def provider = config.provider;
        providerUuid = provider.uuid;
        identifier = provider.identifier ?: ''
        if(provider.providerRole){
            providerRoleUuid = provider.providerRole.uuid
        }
    }

    def providerRoleOptions = []
    providerRoles.each {
        providerRoleOptions.push([label: ui.format(it), value: it.uuid])
    }

    def requiredAttribute = (account.person.personId == null ? "ng-required" : "required")
    def requiredAttributeValue = (account.person.personId == null ? "addProviderAccount" : "")

    def identifierAttributes = ["ng-model":"uuidProviderMap['"+providerUuid+"'].identifier",
                                     "ng-maxlength": propertyMaxLengthMap['identifier']]
    identifierAttributes[requiredAttribute] = requiredAttributeValue

    def providerRoleAttributes = ["ng-model":"uuidProviderMap['"+providerUuid+"'].providerRole"]
    providerRoleAttributes[requiredAttribute] = requiredAttributeValue
%>

<div style="width: 70%">
    <table class="adminui-form-table" cellpadding="0" cellspacing="0">
        <tr>
            <td valign="top">
                ${ ui.includeFragment("uicommons", "field/text", [
                        label: ui.message("Provider.identifier")+"<span class='adminui-text-red'>*</span>",
                        id: "adminui-identifier"+providerUuid,
                        formFieldName: "identifier"+providerUuid,
                        initialValue: identifier,
                        otherAttributes: identifierAttributes
                ]) }
                <span class="field-error" ng-show="${formName}['identifier${providerUuid}'].\$dirty
                    && ${formName}['identifier${providerUuid}'].\$invalid">
                    <span ng-show="${formName}['identifier${providerUuid}'].\$error.required">
                        ${ui.message("adminui.field.required")}
                    </span>
                    <span ng-show="${formName}['identifier${providerUuid}'].\$error.maxlength">
                        ${ui.message("adminui.field.exceeded.maxChars", propertyMaxLengthMap['identifier'])}
                    </span>
                </span>
            </td>
            <td valign="top">
                ${ ui.includeFragment("uicommons", "field/dropDown", [
                        label: ui.message("adminui.providerRole.label")+"<span class='adminui-text-red'>*</span>",
                        id: "adminui-providerRole"+providerUuid,
                        formFieldName: "providerRole"+providerUuid,
                        options: providerRoleOptions,
                        initialValue: providerRoleUuid,
                        otherAttributes: providerRoleAttributes
                ])}
                <span class="field-error" ng-show="${formName}['providerRole${providerUuid}'].\$dirty
                    && ${formName}['providerRole${providerUuid}'].\$invalid">
                    <span ng-show="${formName}['providerRole${providerUuid}'].\$error.required">
                        ${ui.message("adminui.field.required")}
                    </span>
                </span>
            </td>
        </tr>
    </table>
</div>