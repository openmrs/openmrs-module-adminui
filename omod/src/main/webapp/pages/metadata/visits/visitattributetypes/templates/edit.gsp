<h2 ng-hide="visitAttributeType.uuid">${ui.message('adminui.addNewVisitAttributeType.title')}</h2>
<h2 ng-show="visitAttributeType.uuid">${ui.message('adminui.editVisitAttributeType.title')}</h2>

<fieldset class="right" ng-show="visitAttributeType.uuid">
    <legend>${ui.message('general.auditInfo')}</legend>
    <p>
        ${ui.message('general.uuid')}: {{ visitAttributeType.uuid }}
    </p>
    <p>
        ${ui.message('general.createdBy')}:
        {{ visitAttributeType.auditInfo.creator | omrs.display }}
        ${ui.message('general.onDate')}
        {{ visitAttributeType.auditInfo.dateCreated | serverDate }}
    </p>
    <p ng-show="visitAttributeType.changedBy">
        ${ui.message('general.changedBy')}:
        {{ visitAttributeType.auditInfo.changedBy | omrs.display }}
        ${ui.message('general.onDate')}
        {{ visitAttributeType.auditInfo.dateChanged | serverDate }}
    </p>
</fieldset>


<form name="visitAttributeTypeForm" novalidate ng-submit="save()">
    <p>
        <label>${ui.message('general.name')}</label>
        <input ng-model="visitAttributeType.name" required/>
    </p>
    <p>
        <label>${ui.message('general.description')}</label>
        <input ng-model="visitAttributeType.description" />
    </p>
    <p>
        <label>${ui.message('adminui.minOccurs')}</label>
        <input type="number" ng-model="visitAttributeType.minOccurs" required/>
    </p>
    <p>
        <label>${ui.message('adminui.maxOccurs')}</label>
        <input type="number" ng-model="visitAttributeType.maxOccurs" />
    </p>
    <p>
        <label>${ui.message('adminui.datatype')}</label>
        <select ng-model="visitAttributeType.datatypeClassname" ng-options="item.datatypeClassname as item.display for item in customDatatypes" required />
    </p>
	<p>
        <label>${ui.message('adminui.datatypeConfiguration')}</label>
        <textarea ng-model="visitAttributeType.datatypeConfig" />
    </p>
    <p>
        <label>${ui.message('adminui.handler')}</label>
        <select ng-model="visitAttributeType.preferredHandlerClassname" ng-init="visitAttributeType.preferredHandlerClassname = options[0]">
        	<option value="">${ui.message('general.default')}</option>
        	<option ng-repeat="handlerClassname in visitAttributeType.datatypeClassname" value="{{handlerClassname}}">{{handlerClassname}}</option>
        </select>	
    </p>
    <p>
        <label>${ui.message('adminui.handlerConfiguration')}</label>
        <textarea ng-model="visitAttributeType.handlerConfig" />
    </p>

    <p>
        <button ng-disabled="visitAttributeTypeForm.\$invalid" type="submit" class="confirm right">${ui.message('general.save')}</button>
        <button type="button" class="cancel" ui-sref="list">${ui.message('general.cancel')}</button>
    </p>
</form>

