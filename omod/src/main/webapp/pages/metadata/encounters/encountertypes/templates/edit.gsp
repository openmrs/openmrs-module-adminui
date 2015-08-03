<h2 ng-hide="encounterType.uuid">Add Encounter Type</h2>
<h2 ng-show="encounterType.uuid">Edit Encounter Type</h2>

<fieldset class="right" ng-show="encounterType.uuid">
    <legend>Audit Info</legend>
    <p>
        UUID: {{ encounterType.uuid }}
    </p>
    <p>
        Created:
        {{ encounterType.auditInfo.creator | omrs.display }}
        on
        {{ encounterType.auditInfo.dateCreated | serverDate }}
    </p>
    <p ng-show="encounterType.changedBy">
        Changed by:
        {{ encounterType.auditInfo.changedBy | omrs.display }}
        on
        {{ encounterType.auditInfo.dateChanged | serverDate }}
    </p>
</fieldset>


<form name="encounterTypeForm" novalidate ng-submit="save()">
    <p>
        <label>Name</label>
        <input ng-model="encounterType.name" required/>
    </p>
    <p>
        <label>Description</label>
        <input ng-model="encounterType.description" required/>
    </p>

    <p>
        <button ng-disabled="encounterTypeForm.\$invalid" type="submit" class="confirm right">Save</button>
        <button type="button" class="cancel" ui-sref="list">Cancel</button>
    </p>
</form>

