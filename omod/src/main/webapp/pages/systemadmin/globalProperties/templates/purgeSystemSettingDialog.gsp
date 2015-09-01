<div class="dialog-header">
    <h3>${ui.message('adminui.systemSetting.purge.title')}</h3>
</div>
<div class="dialog-content">
    <h4>
        ${ui.message('uicommons.delete')} &quot;{{ systemSetting.property }}&quot;?
    </h4>
    <br />
    <div>
        <button class="confirm right" ng-click="confirm(reason)">${ ui.message("uicommons.confirm") }</button>
        <button class="cancel" ng-click="closeThisDialog()">${ ui.message("uicommons.cancel") }</button>
    </div>
</div>