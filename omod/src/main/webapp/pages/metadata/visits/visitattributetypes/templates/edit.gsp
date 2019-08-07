<h2 ng-hide="visitAttributeType.uuid">${ui.message('adminui.addNewVisitAttributeType.title')}</h2>
<h2 ng-show="visitAttributeType.uuid">${ui.message('adminui.editVisitAttributeType.title')}</h2>

<fieldset class="right" ng-show="visitAttributeType.uuid">
    <legend>${ui.message('general.auditInfo')}</legend>
    <p>
        ${ui.message('general.uuid')}: {{ visitAttributeType.uuid }}
    </p>
    <p>
        ${ui.message('general.createdBy')}:
        {{ visitAttributeType.auditInfo.creator | omrsDisplay }}
        ${ui.message('general.onDate')}
        {{ visitAttributeType.auditInfo.dateCreated | serverDate }}
    </p>
    <p ng-show="visitAttributeType.changedBy">
        ${ui.message('general.changedBy')}:
        {{ visitAttributeType.auditInfo.changedBy | omrsDisplay }}
        ${ui.message('general.onDate')}
        {{ visitAttributeType.auditInfo.dateChanged | serverDate }}
    </p>
</fieldset>

<form name="visitAttributeTypeForm" novalidate ng-submit="save()" class="simple-form-ui">
    <p>
        <label>${ui.message('general.name')}<span class="adminui-text-red">*</span></label>
        <input class="form-control form-control-sm form-control-lg form-control-md" ang-model="visitAttributeType.name" required/>
        <span ng-repeat="item in error.fieldErrors.name" class="field-error">{{item.message}}</span>
    </p>
    <p>
        <label>${ui.message('general.description')}</label>
        <input class="form-control form-control-sm form-control-lg form-control-md" ng-model="visitAttributeType.description" />
        <span ng-repeat="item in error.fieldErrors.description" class="field-error">{{item.message}}</span>
    </p>
    <p>
        <label>${ui.message('adminui.minOccurs')}<span class="adminui-text-red">*</span></label>
        <input class="form-control form-control-sm form-control-lg form-control-md" type="number" ng-model="visitAttributeType.minOccurs" required/>
        <span ng-repeat="item in error.fieldErrors.minOccurs" class="field-error">{{item.message}}</span>
    </p>
    <p>
        <label>${ui.message('adminui.maxOccurs')}</label>
        <input class="form-control form-control-sm form-control-lg form-control-md" type="number" ng-model="visitAttributeType.maxOccurs" />
        <span ng-repeat="item in error.fieldErrors.maxOccurs" class="field-error">{{item.message}}</span>
    </p>
    <p>
        <label>${ui.message('adminui.datatype')}<span class="adminui-text-red">*</span></label>
        <select class="form-control form-control-sm" ng-model="visitAttributeType.datatypeClassname" 
        	ng-options="customDatatype.datatypeClassname as customDatatype.display for customDatatype in customDatatypes" 
        	ng-change="updateHandlerClassnames(true)" ng-init="updateHandlerClassnames(false)" required />
    </p>
	<p>
        <label>${ui.message('adminui.datatypeConfig')}</label>
        <textarea class="form-control form-control-sm form-control-lg form-control-md" ng-model="visitAttributeType.datatypeConfig" rows="3" cols="54" />
        <span ng-repeat="item in error.fieldErrors.datatypeConfig" class="field-error">{{item.message}}</span>
    </p>
    <p>
        <label>${ui.message('adminui.preferredHandler')}</label>
        <select class="form-control form-control-sm" ng-model="visitAttributeType.preferredHandlerClassname"
			ng-options="handler.handlerClassname as handler.display for handler in handlers">
        	<option value="">${ui.message('general.default')}</option>
        </select>
    </p>
    <p>
        <label>${ui.message('adminui.handlerConfig')}</label>
        <textarea class="form-control form-control-sm form-control-lg form-control-md" ng-model="visitAttributeType.handlerConfig" rows="3" cols="54" />
        <span ng-repeat="item in error.fieldErrors.handlerConfig" class="field-error">{{item.message}}</span>
    </p>

    <p>
        <button ng-disabled="visitAttributeTypeForm.\$invalid" type="submit" class="confirm right">${ui.message('general.save')}</button>
        <button type="button" class="cancel" ui-sref="list">${ui.message('general.cancel')}</button>
    </p>
</form>

