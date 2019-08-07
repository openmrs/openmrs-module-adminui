<button ui-sref="edit">${ ui.message("adminui.addNewPatientIdentifierType.title") }</button>
<br/>
<br/>

<table class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl">
    <thead>
    <tr>
        <th class="adminui-name-column">${ui.message('general.name')}</th>
        <th>${ui.message('general.description')}</th>
        <th class="adminui-action-column">${ui.message('general.action')}</th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="patientIdentifierType in patientIdentifierTypes">
        <td valign="top" ng-class="{ retired: patientIdentifierType.retired }">{{patientIdentifierType.name}}</td>
        <td valign="top" ng-class="{ retired: patientIdentifierType.retired }">{{patientIdentifierType.description}}</td>
        <td valign="top">
            <a ng-hide="patientIdentifierType.retired" ui-sref="edit({patientIdentifierTypeUuid: patientIdentifierType.uuid})">
                <i class="icon-pencil edit-action" title="${ui.message("emr.edit")}"></i>
            </a>
            <a ng-hide="patientIdentifierType.retired" ng-click="retire(patientIdentifierType)">
                <i class="icon-remove delete-action" title="${ui.message("emr.delete")}"></i>
            </a>
            <a ng-show="patientIdentifierType.retired" ng-click="unretire(patientIdentifierType)">
                <i class="icon-reply edit-action" title="${ui.message("general.restore")}"></i>
            </a>
            <a ng-click="purge(patientIdentifierType)" class="right">
                <i class="icon-trash delete-action" title="${ui.message("general.purge")}"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>