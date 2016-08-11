<h2 ng-hide="providerAttributeType.uuid">${ui.message('adminui.addNewProviderAttributeType.title')}</h2>
<h2 ng-show="providerAttributeType.uuid">${ui.message('adminui.editProviderAttributeType.title')}</h2>

<fieldset class="right" ng-show="providerAttributeType.uuid">
    <legend>${ui.message('general.auditInfo')}</legend>
    <p>
        ${ui.message('general.uuid')}: {{ providerAttributeType.uuid }}
    </p>
    <p>
        ${ui.message('general.createdBy')}:
        {{ providerAttributeType.auditInfo.creator | omrsDisplay }}
        ${ui.message('general.onDate')}
        {{ providerAttributeType.auditInfo.dateCreated | serverDate }}
    </p>
    <p ng-show="providerAttributeType.changedBy">
        ${ui.message('general.changedBy')}:
        {{ providerAttributeType.auditInfo.changedBy | omrsDisplay }}
        ${ui.message('general.onDate')}
        {{ providerAttributeType.auditInfo.dateChanged | serverDate }}
    </p>
</fieldset>


<form class="simple-form-ui" name="providerAttributeTypeForm" novalidate ng-submit="save()">
    <p>
        <label>${ui.message('general.name')}<span class='adminui-text-red'>*</span></label>
        <input ng-model="providerAttributeType.name" required/>
    </p>
    <p>
        <label>${ui.message('general.description')}</label>
        <textarea ng-model="providerAttributeType.description" cols="54"></textarea>
    </p>
    
    <p>
        <label>${ui.message('FormField.minOccurs')}<span class='adminui-text-red'>*</span></label>
        <input ng-model="providerAttributeType.minOccurs" required/>
    </p>
    <p>
        <label>${ui.message('FormField.maxOccurs')}</label>
        <input ng-model="providerAttributeType.maxOccurs"/>
    </p>
    <p>
        <label>${ui.message('AttributeType.datatypeClassname')}<span class='adminui-text-red'>*</span></label>
        <select ng-model="providerAttributeType.datatypeClassname" required>
        	<option></option>
        	<% datatypeClassnames.each { dtcn -> %>
	        	<option value="${dtcn}" <% if (dtcn == '{{providerAttributeType.datatypeClassname}}') { %> selected="selected" <% } %> >
	        		${ui.message(dtcn + '.name')}
	        	</option>
        	<% } %>
        </select>
    </p>
    <p>
        <label>${ui.message('AttributeType.datatypeConfig')}</label>
        <textarea ng-model="providerAttributeType.datatypeConfig" cols="54"></textarea>
    </p>
    <p>
        <label>${ui.message('AttributeType.preferredHandlerClassname')}</label>
        <select ng-model="providerAttributeType.preferredHandlerClassname">
        	<option></option>
        	<% preferredHandlerClassnames.each { phcn -> %>
	        	<option value="${phcn}" <% if (phcn == '{{providerAttributeType.preferredHandlerClassname}}') { %> selected="selected" <% } %> >
	        		${ui.message(phcn + '.name')}
	        	</option>
        	<% } %>
        </select>
    </p>
    <p>
        <label>${ui.message('AttributeType.handlerConfig')}</label>
        <textarea ng-model="providerAttributeType.handlerConfig" cols="54"></textarea>
    </p>

    <p>
        <button ng-disabled="providerAttributeTypeForm.\$invalid" type="submit" class="confirm right">${ui.message('general.save')}</button>
        <button type="button" class="cancel" ui-sref="list">${ui.message('general.cancel')}</button>
    </p>
</form>

