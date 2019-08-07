<button ui-sref="edit">${ ui.message("adminui.addNewProviderAttributeType.title") }</button>
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
    <tr ng-repeat="providerAttributeType in providerAttributeTypes">
        <td valign="top" ng-class="{ retired: providerAttributeType.retired }">{{providerAttributeType.name}}</td>
        <td valign="top" ng-class="{ retired: providerAttributeType.retired }">{{providerAttributeType.description}}</td>
        <td valign="top">
            <a ng-hide="providerAttributeType.retired" ui-sref="edit({providerAttributeTypeUuid: providerAttributeType.uuid})">
                <i class="icon-pencil edit-action" title="${ui.message("emr.edit")}"></i>
            </a>
            <a ng-hide="providerAttributeType.retired" ng-click="retire(providerAttributeType)">
                <i class="icon-remove delete-action" title="${ui.message("emr.delete")}"></i>
            </a>
            <a ng-show="providerAttributeType.retired" ng-click="unretire(providerAttributeType)">
                <i class="icon-reply edit-action" title="${ui.message("general.restore")}"></i>
            </a>
            <a ng-click="purge(providerAttributeType)" class="right">
                <i class="icon-trash delete-action" title="${ui.message("general.purge")}"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>