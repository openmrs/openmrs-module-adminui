<button ui-sref="edit">${ ui.message("adminui.addNewSystemSetting.title") }</button>
<br/>
<br/>

<style type="text/css">
	table td {
	    max-width: 100px;
	    overflow: hidden;
	    text-overflow: ellipsis;
	    white-space: nowrap;
	    padding-top: 0px;
	    padding-bottom: 0px;
	}
    .adminui-gp-action-column{
        width:50px;
    }
</style>

<table class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl" >
    <thead>
    <tr>
        <th>${ui.message('general.name')}</th>
        <th>${ui.message('general.value')}</th>
        <th class="adminui-gp-action-column">${ui.message('general.action')}</th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="systemSetting in systemSettings" ng-if="!systemSetting.property.endsWith('.mandatory') && !systemSetting.property.endsWith('.started')">
        <td valign="top" title='{{systemSetting.description}}'>{{systemSetting.property}}</td>
        <td valign="top">{{systemSetting.value}}</td>
        <td valign="top">
            <a ng-click="purge(systemSetting)" class="right">
                <i class="icon-trash delete-action" title="${ui.message("general.purge")}"></i>
            </a>
            <a ui-sref="edit({systemSettingUuid: systemSetting.uuid})">
                <i class="icon-pencil edit-action" title="${ui.message("emr.edit")}"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>