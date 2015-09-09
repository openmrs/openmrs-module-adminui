<div class="dialog-header">
    <h3>${ui.message('adminui.patientIdentifierType.purge.title')}</h3>
</div>
<div class="dialog-content">
    <h4>
        {{ '${ui.message("adminui.purge")}'.replace('{0}', patientIdentifierType.name) }}
    </h4>
    <br />
    <div>
        <button class="confirm right" ng-click="confirm(reason)">${ ui.message("uicommons.confirm") }</button>
        <button class="cancel" ng-click="closeThisDialog()">${ ui.message("uicommons.cancel") }</button>
    </div>
</div>