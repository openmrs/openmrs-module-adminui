<div class="dialog-header">
    <h3>Retire Visit Attribute Type</h3>
</div>
<div class="dialog-content">
    <h4>
        Retire &quot;{{ visitAttributeType.name }}&quot;?
    </h4>
    <p>
        Reason: <input type="text" ng-model="reason" placeholder="${ ui.message("emr.optional") }"/>
    </p>
    <br/>
    <div>
        <button class="confirm right" ng-click="confirm(reason)">${ ui.message("uicommons.confirm") }</button>
        <button class="cancel" ng-click="closeThisDialog()">${ ui.message("uicommons.cancel") }</button>
    </div>
</div>