<button ui-sref="edit">${ ui.message("adminui.addNewVisitType.title") }</button>
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
    <tr ng-repeat="visitType in visitTypes">
        <td valign="top" ng-class="{ retired: visitType.retired }">{{visitType.name}}</td>
        <td valign="top" ng-class="{ retired: visitType.retired }">{{visitType.description}}</td>
        <td valign="top">
            <a ng-hide="visitType.retired" ui-sref="edit({visitTypeUuid: visitType.uuid})">
                <i class="icon-pencil edit-action" title="${ui.message("emr.edit")}"></i>
            </a>
            <a ng-hide="visitType.retired" ng-click="retire(visitType)">
                <i class="icon-remove delete-action" title="${ui.message("emr.delete")}"></i>
            </a>
            <a ng-show="visitType.retired" ng-click="unretire(visitType)">
                <i class="icon-reply edit-action" title="${ui.message("general.restore")}"></i>
            </a>
            <a ng-click="purge(visitType)" class="right">
                <i class="icon-trash delete-action" title="${ui.message("general.purge")}"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>