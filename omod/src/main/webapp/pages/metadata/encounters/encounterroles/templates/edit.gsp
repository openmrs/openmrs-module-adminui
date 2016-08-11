<h2 ng-hide="encounterRole.uuid">${ui.message('adminui.addNewEncounterRole.title')}</h2>
<h2 ng-show="encounterRole.uuid">${ui.message('adminui.editEncounterRole.title')}</h2>

<fieldset class="right" ng-show="encounterRole.uuid">
    <legend>${ui.message('general.auditInfo')}</legend>
    <p>
        ${ui.message('general.uuid')}: {{ encounterRole.uuid }}
    </p>
    <p>
        ${ui.message('general.createdBy')}:
        {{ encounterRole.auditInfo.creator | omrsDisplay }}
        ${ui.message('general.onDate')}
        {{ encounterRole.auditInfo.dateCreated | serverDate }}
    </p>
    <p ng-show="encounterRole.changedBy">
        ${ui.message('general.changedBy')}:
        {{ encounterRole.auditInfo.changedBy | omrsDisplay }}
        ${ui.message('general.onDate')}
        {{ encounterRole.auditInfo.dateChanged | serverDate }}
    </p>
</fieldset>


<form class="simple-form-ui" name="encounterRoleForm" novalidate ng-submit="save()">
    <p>
        <label>${ui.message('general.name')}</label>
        <input ng-model="encounterRole.name" required/>
    </p>
    <p>
        <label>${ui.message('general.description')}</label>
        <textarea ng-model="encounterRole.description" cols="54" required></textarea>
    </p>

    <p>
        <button ng-disabled="encounterRoleForm.\$invalid" type="submit" class="confirm right">${ui.message('general.save')}</button>
        <button type="button" class="cancel" ui-sref="list">${ui.message('general.cancel')}</button>
    </p>
</form>

