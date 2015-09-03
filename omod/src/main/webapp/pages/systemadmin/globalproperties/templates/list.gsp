<button ui-sref="edit">${ ui.message("adminui.addNewSystemSetting.title") }</button>
<br/>
<br/>

<table>
    <thead>
    <tr>
        <th>${ui.message('general.name')}</th>
        <th>${ui.message('general.description')}</th>
        <th class="adminui-action-column">${ui.message('general.action')}</th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="systemSetting in systemSettings" ng-if="!systemSetting.property.endsWith('.mandatory') && !systemSetting.property.endsWith('.started')">
        <td>{{systemSetting.property}}</td>
        <td>{{systemSetting.description}}</td>
        <td>
            <a ui-sref="edit({systemSettingUuid: systemSetting.uuid})">
                <i class="icon-pencil edit-action" title="${ui.message("emr.edit")}"></i>
            </a>
            <a ng-click="purge(systemSetting)" class="right">
                <i class="icon-trash delete-action" title="${ui.message("general.purge")}"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>