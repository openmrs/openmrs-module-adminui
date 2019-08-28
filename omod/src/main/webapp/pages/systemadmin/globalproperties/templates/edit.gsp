<h2 ng-hide="systemSetting.uuid">${ui.message('adminui.addNewSystemSetting.title')}</h2>
<h2 ng-show="systemSetting.uuid">${ui.message('adminui.editSystemSetting.title')}</h2>

<fieldset class="right" ng-show="systemSetting.uuid">
    <legend>${ui.message('adminui.auditInfo')}</legend>
    <p>
        ${ui.message('general.uuid')}: {{ systemSetting.uuid }}
    </p>
</fieldset>


<form class="simple-form-ui" name="systemSettingForm" novalidate ng-submit="save()">
    <p>
        <label>${ui.message('general.name')}</label>
        <input  class="form-control form-control-sm form-control-lg form-control-md" ng-model="systemSetting.property" ng-disabled="systemSetting.uuid" required/>
    </p>
    <p>
        <label>${ui.message('general.description')}</label>
        <textarea  class="form-control form-control-sm form-control-lg form-control-md" ng-model="systemSetting.description" cols="54"></textarea>
    </p>
    <p>
        <label>${ui.message('general.value')}</label>
        <textarea  class="form-control form-control-sm form-control-lg form-control-md" ng-model="systemSetting.value" cols="54"></textarea>
    </p>

    <p>
        <button ng-disabled="systemSettingForm.\$invalid" type="submit" class="confirm right">${ui.message('general.save')}</button>
        <button type="button" class="cancel" ui-sref="list">${ui.message('general.cancel')}</button>
    </p>
</form>

