<h2 ng-hide="visitType.uuid">${ui.message('adminui.addNewVisitType.title')}</h2>
<h2 ng-show="visitType.uuid">${ui.message('adminui.editVisitType.title')}</h2>

<fieldset class="right" ng-show="visitType.uuid">
    <legend>${ui.message('general.auditInfo')}</legend>
    <p>
        ${ui.message('general.uuid')}: {{ visitType.uuid }}
    </p>
    <p>
        ${ui.message('general.createdBy')}:
        {{ visitType.auditInfo.creator | omrsDisplay }}
        ${ui.message('general.onDate')}
        {{ visitType.auditInfo.dateCreated | serverDate }}
    </p>
    <p ng-show="visitType.changedBy">
        ${ui.message('general.changedBy')}:
        {{ visitType.auditInfo.changedBy | omrsDisplay }}
        ${ui.message('general.onDate')}
        {{ visitType.auditInfo.dateChanged | serverDate }}
    </p>
</fieldset>


<form class="simple-form-ui" name="visitTypeForm" novalidate ng-submit="save()">
    <p>
        <label>${ui.message('general.name')}</label>
        <input ng-model="visitType.name" required/>
    </p>
    <p>
        <label>${ui.message('general.description')}</label>
        <textarea ng-model="visitType.description" cols="54" required></textarea>
    </p>

    <p>
        <button ng-disabled="visitTypeForm.\$invalid" type="submit" class="confirm right">${ui.message('general.save')}</button>
        <button type="button" class="cancel" ui-sref="list">${ui.message('general.cancel')}</button>
    </p>
</form>

