<button ui-sref="edit">${ ui.message("adminui.role.addNewRole.title") }</button>
<br/>
<br/>

<table class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl" id="list-roles" cellspacing="0" cellpadding="2">
    <thead>
    <tr>
        <th class="adminui-name-column">${ui.message('adminui.role.role')}</th>
        <th>${ui.message('general.description')}</th>
        <th class="adminui-action-column">${ui.message('general.action')}</th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="role in roles" >
        <td valign="top">{{role.name}}</td>
        <td valign="top">{{role.description}}</td>
        <td valign="top">
            <a ui-sref="edit({roleUuid: role.uuid})">
                <i class="icon-pencil edit-action" title="${ui.message("emr.edit")}"></i>
            </a>
            <a ng-click="purge(role)" class="right">
                <i class="icon-trash delete-action" title="${ui.message("general.purge")}"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>

