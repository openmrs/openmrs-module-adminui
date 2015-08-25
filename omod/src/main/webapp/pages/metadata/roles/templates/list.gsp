<button ui-sref="edit">${ ui.message("adminui.role.addNewRole.title") }</button>
<br/>
<br/>

<table id="list-roles" cellspacing="0" cellpadding="2">
    <thead>
    <tr>
        <th>${ui.message('adminui.role.role')}</th>
        <th>${ui.message('general.description')}</th>
        <th>${ui.message('adminui.role.inheritedRoles')}</th>
        <th>${ui.message('adminui.role.privileges')}</th>
        <th>${ui.message('general.action')}</th>
    </tr>
    </thead>
    <tbody>
    <tr ng-repeat="role in roles" >
        <td>{{role.name}}</td>
        <td>{{role.description}}</td>
        <td>
        	<table>
        		<tbody>
        			<tr ng-repeat="inheritedRole in role.inheritedRoles" >
        				<td>{{inheritedRole.name}}</td>
        			</tr>
        		</tbody>
        	</table>
        </td>
        <td>
        	<table>
        		<tbody>
        			<tr ng-repeat="privilege in role.privileges" >
       					<td>{{privilege.name}}</td>
        			</tr>
        		</tbody>
        	</table>
        </td>
        <td>
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

