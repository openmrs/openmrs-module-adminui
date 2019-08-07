<button ui-sref="edit">${ ui.message("adminui.addNewEncounterType.title") }</button>
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
    <tr ng-repeat="encounterType in encounterTypes">
        <td valign="top" ng-class="{ retired: encounterType.retired }">{{encounterType.name}}</td>
        <td valign="top" ng-class="{ retired: encounterType.retired }">{{encounterType.description}}</td>
        <td valign="top">
            <a ng-hide="encounterType.retired" ui-sref="edit({encounterTypeUuid: encounterType.uuid})">
                <i class="icon-pencil edit-action" title="${ui.message("emr.edit")}"></i>
            </a>
            <a ng-hide="encounterType.retired" ng-click="retire(encounterType)">
                <i class="icon-remove delete-action" title="${ui.message("emr.delete")}"></i>
            </a>
            <a ng-show="encounterType.retired" ng-click="unretire(encounterType)">
                <i class="icon-reply edit-action" title="${ui.message("general.restore")}"></i>
            </a>
            <a ng-click="purge(encounterType)" class="right">
                <i class="icon-trash delete-action" title="${ui.message("general.purge")}"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>