<button ui-sref="edit">${ ui.message("adminui.addNewVisitAttributeType.title") }</button>
<br/>
<br/>

<table>
    <thead>
    <tr>
        <th>${ui.message('general.name')}</th>
        <th>${ui.message('general.description')}</th>
        <th>${ui.message('general.action')}</th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="visitAttributeType in visitAttributeTypes" ng-class="{ retired: visitAttributeType.retired }">
        <td>{{visitAttributeType.name}}</td>
        <td>{{visitAttributeType.description}}</td>
        <td>
            <a ng-hide="visitAttributeType.retired" ui-sref="edit({visitAttributeTypeUuid: visitAttributeType.uuid})">
                <i class="icon-pencil edit-action" title="${ui.message("emr.edit")}"></i>
            </a>
            <a ng-hide="visitAttributeType.retired" ng-click="retire(visitAttributeType)">
                <i class="icon-remove delete-action" title="${ui.message("emr.delete")}"></i>
            </a>
            <a ng-show="visitAttributeType.retired" ng-click="unretire(visitAttributeType)">
                <i class="icon-reply edit-action" title="${ui.message("uicommons.unretire")}"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>