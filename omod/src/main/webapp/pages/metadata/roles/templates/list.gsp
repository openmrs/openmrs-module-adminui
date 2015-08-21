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
    <tr ng-repeat="role in roles" ng-class="{ retired: role.retired }">
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
            <a ng-hide="role.retired" ui-sref="edit({roleUuid: role.uuid})">
                <i class="icon-pencil edit-action" title="${ui.message("emr.edit")}"></i>
            </a>
            <a ng-hide="role.retired" ng-click="retire(role)">
                <i class="icon-remove delete-action" title="${ui.message("emr.delete")}"></i>
            </a>
            <a ng-show="role.retired" ng-click="unretire(role)">
                <i class="icon-reply edit-action" title="${ui.message("uicommons.unretire")}"></i>
            </a>
            <!-- though purge works, if not required, button can be removed/commented out
            -->
            <a ng-click="purge(role)" class="right">
                <i class="icon-trash delete-action" title="${ui.message("general.purge")}"></i>
            </a>
        </td>
    </tr>
    </tbody>
</table>

