<button ui-sref="edit">${ ui.message("adminui.addNewVisitAttributeType.title") }</button>
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
    <tr ng-repeat="visitAttributeType in visitAttributeTypes" ng-class="{ retired: visitAttributeType.retired }">
        <td valign="top">{{visitAttributeType.name}}</td>
        <td valign="top">{{visitAttributeType.description}}</td>
        <td valign="top">
            <a ng-hide="visitAttributeType.retired" ui-sref="edit({visitAttributeTypeUuid: visitAttributeType.uuid})">
                <i class="icon-pencil edit-action" title="${ui.message("emr.edit")}"></i>
            </a>
            <a ng-hide="visitAttributeType.retired" ng-click="retire(visitAttributeType)">
                <i class="icon-remove delete-action" title="${ui.message("emr.delete")}"></i>
            </a>
            <a ng-show="visitAttributeType.retired" ng-click="unretire(visitAttributeType)">
                <i class="icon-reply edit-action" title="${ui.message("uicommons.unretire")}"></i>
            </a>
            <a ng-click="purge(visitAttributeType)">
                <i class="icon-trash delete-action right" title="${ui.message("general.purge")}"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>