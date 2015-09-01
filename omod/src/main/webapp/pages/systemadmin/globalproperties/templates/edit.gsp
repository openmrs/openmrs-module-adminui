<h2 ng-hide="systemSetting.uuid">${ui.message('adminui.addNewSystemSetting.title')}</h2>
<h2 ng-show="systemSetting.uuid">${ui.message('adminui.editSystemSetting.title')}</h2>

<fieldset class="right" ng-show="systemSetting.uuid">
    <legend>${ui.message('general.auditInfo')}</legend>
    <p>
        ${ui.message('general.uuid')}: {{ systemSetting.uuid }}
    </p>
    <p>
        ${ui.message('general.createdBy')}:
        {{ systemSetting.auditInfo.creator | omrs.display }}
        ${ui.message('general.onDate')}
        {{ systemSetting.auditInfo.dateCreated | serverDate }}
    </p>
    <p ng-show="systemSetting.changedBy">
        ${ui.message('general.changedBy')}:
        {{ systemSetting.auditInfo.changedBy | omrs.display }}
        ${ui.message('general.onDate')}
        {{ systemSetting.auditInfo.dateChanged | serverDate }}
    </p>
</fieldset>


<form class="simple-form-ui" name="systemSettingForm" novalidate ng-submit="save()">
    <p ng-hide="systemSetting.property">
        <label>${ui.message('general.name')}</label>
        <input ng-model="systemSetting.property" required/>
    </p>
    <p>
        <label>${ui.message('general.description')}</label>
        <textarea ng-model="systemSetting.description" cols="54" required></textarea>
    </p>
    <p>
        <label>${ui.message('general.value')}</label>
        <input ng-model="systemSetting.value"/>
    </p>

    <p>
        <button ng-disabled="systemSettingForm.\$invalid" type="submit" class="confirm right">${ui.message('general.save')}</button>
        <button type="button" class="cancel" ui-sref="list">${ui.message('general.cancel')}</button>
    </p>
</form>

