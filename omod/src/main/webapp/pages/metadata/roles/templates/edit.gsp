<h2 ng-hide="role.uuid">Add Role</h2>
<h2 ng-show="role.uuid">Edit Role</h2>

<fieldset class="right" ng-show="role.uuid">
    <legend>${ui.message('general.auditInfo')}</legend>
    <p>
        ${ui.message('general.uuid')}: {{ role.uuid }}
    </p>
    <p>
        ${ui.message('general.createdBy')}:
        {{ role.auditInfo.creator | omrs.display }}
        ${ui.message('general.onDate')}
        {{ role.auditInfo.dateCreated | serverDate }}
    </p>
    <p ng-show="role.changedBy">
        ${ui.message('general.changedBy')}:
        {{ role.auditInfo.changedBy | omrs.display }}
        ${ui.message('general.onDate')}
        {{ role.auditInfo.dateChanged | serverDate }}
    </p>
</fieldset>

<form class="simple-form-ui" name="roleForm" novalidate ng-submit="save()">
    <p>
        <label>Role</label>
        <input ng-model="role.name" required/>
    </p>
    <p>
        <label>Description</label>
        <input ng-model="role.description" required/>
    </p>
    <p>
        <label>Inherited Roles</label>
    </p>
    <p>
		Roles that contain (inherit privileges from) {{role.name}}
    </p>
    <p>
    	<om-coltable om-ct-main="dependantRoles" om-ct-cols="2" om-ct-ckb="false" om-ct-sec=""></om-coltable>
    	<!-- if directive approach approved, this can be removed
    	<table>
    		<tbody>
			    <tr ng-repeat="(roleIndex, lsRole) in dependantRoles" ng-hide="roleIndex % 2" >
			    	<td>
			            {{dependantRoles[roleIndex].name}}
			        </td>
			        <td ng-hide="!dependantRoles[roleIndex + 1]">
			            {{dependantRoles[roleIndex + 1].name}}
			        </td>
			    </tr>
    		</tbody>
    	</table>
    	-->
    </p>
    <p>
    	<om-coltable om-ct-main="roles" om-ct-cols="2" om-ct-ckb="true" om-ct-sec="inheritedRoles"></om-coltable>
    	<!-- if directive approach approved, this can be removed
    	<table>
    		<tbody>
			    <tr ng-repeat="(roleIndex, lsRole) in roles" ng-hide="roleIndex % 2" >
			    	<td>
						<input type="checkbox" ng-model="inheritedRoles[roleIndex]" >
			            {{roles[roleIndex].name}}
			        </td>
			        <td ng-hide="!roles[roleIndex + 1]">
						<input type="checkbox" ng-model="inheritedRoles[roleIndex + 1]" >
			            {{roles[roleIndex + 1].name}}
			        </td>
			    </tr>
    		</tbody>
    	</table>
    	-->
    </p>
    <p>
        <label>Privileges</label>
    </p>
    <p>
		Privileges without checkboxes 
		represent privileges inherited from other roles, 
		these cannot be removed individually.
    </p>
    <p>
    	<om-coltable om-ct-main="inheritedPrivileges" om-ct-cols="2" om-ct-ckb="false" om-ct-sec=""></om-coltable>
    	<!-- if directive approach approved, this can be removed
    	<table>
    		<tbody>
			    <tr ng-repeat="(privilegeIndex, privilege) in inheritedPrivileges" ng-hide="privilegeIndex % 2" >
			        <td>
			            {{inheritedPrivileges[privilegeIndex].name}}
			        </td>
			        <td ng-hide="!inheritedPrivileges[privilegeIndex + 1]">
			            {{inheritedPrivileges[privilegeIndex + 1].name}}
			        </td>
			    </tr>
    		</tbody>
     	</table>
     	-->
     </p>
     <p>
    	<om-coltable om-ct-main="privileges" om-ct-cols="2" om-ct-ckb="true" om-ct-sec="privilegeFlags"></om-coltable>
    	<!-- if directive approach approved, this can be removed
    	<table>
    		<tbody>
			    <tr ng-repeat="(privilegeIndex, privilege) in privileges" ng-hide="privilegeIndex % 2" >
			        <td>
						<input type="checkbox" ng-model="privilegeFlags[privilegeIndex]" >
			            {{privileges[privilegeIndex].name}}
			        </td>
			        <td ng-hide="!privileges[privilegeIndex + 1]">
						<input type="checkbox" ng-model="privilegeFlags[privilegeIndex + 1]" >
			            {{privileges[privilegeIndex + 1].name}}
			        </td>
			    </tr>
    		</tbody>
    	</table>
    	-->
    </p>

    <p>
        <button ng-disabled="roleForm.\$invalid" type="submit" class="confirm right">Save</button>
        <button type="button" class="cancel" ui-sref="list">Cancel</button>
    </p>
</form>

