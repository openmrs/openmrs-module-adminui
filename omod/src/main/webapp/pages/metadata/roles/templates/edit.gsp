<h2 ng-hide="role.uuid">${ ui.message("adminui.role.addNewRole.title") }</h2>
<h2 ng-show="role.uuid">${ ui.message("adminui.role.editRole.title") }</h2>

<form class="simple-form-ui" name="roleForm" novalidate ng-submit="save()">
    <p>
        <label>${ ui.message("adminui.role.role") }</label>
        <input ng-hide="role.uuid" ng-model="role.name" required/>
        <p ng-show="role.uuid">{{role.name}}</p>
    </p>
    
    <p>
        <label>${ ui.message("adminui.role.description") }</label>
        <input ng-model="role.description" required/>
    </p>
    <div ng-if="dependantRoles.length > 0">
	    <p>
			${ ui.message("adminui.role.containedRoles.label") } {{role.name}}
	    </p>
	    <p>
            <table class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl">
                <tbody>
                    <tr ng-repeat="(idx, val) in dependantRoles" ng-hide="idx % 2">
                        <td >
                            {{dependantRoles[idx].name}}
                        </td>
                        <td ng-hide="!dependantRoles[idx + 1]">
                            {{dependantRoles[idx + 1].name}}
                        </td>
                    </tr>
                </tbody>
            </table>
	    </p>
    </div>
    <p>
        <label>${ ui.message("adminui.role.inheritedRoles") }</label>
    </p>
    <p>
        <table class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl">
            <tbody>
                <tr ng-repeat="(idx, val) in roles" ng-hide="idx % 2">
                    <td>
                        <input type="checkbox" ng-model="inheritedRoles[idx]"> {{roles[idx].name}}
                    </td>
                    <td ng-hide="!roles[idx + 1]" >
                        <input type="checkbox" ng-model="inheritedRoles[idx + 1]"> {{roles[idx + 1].name}}
                    </td>
                </tr>
            </tbody>
        </table>
    </p>
    <p>
        <label>${ ui.message("adminui.role.privileges") }</label>
    </p>
    <p>
        <label>${ ui.message("adminui.role.inheritedPrivileges.label") }</label>
    </p>
     <p>
        <table class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl">
            <tbody>
                <tr ng-repeat="(idx, val) in privileges" ng-hide="idx % 2">
                    <td ng-class="{disabled: inheritedPrivilegeFlags[idx] }">
                        <input type="checkbox" ng-disabled="inheritedPrivilegeFlags[idx]" ng-model="privilegeFlags[idx]">
                        {{privileges[idx].name}}
                    </td>
                    <td ng-hide="!privileges[idx + 1]" ng-class="{ disabled: inheritedPrivilegeFlags[idx + 1] }">
                        <input type="checkbox" ng-disabled="inheritedPrivilegeFlags[idx + 1]" ng-model="privilegeFlags[idx + 1]">
                        {{privileges[idx + 1].name}}
                    </td>
                </tr>
            </tbody>
        </table>
    </p>

    <p>
        <button ng-disabled="roleForm.\$invalid" type="submit" class="confirm right">${ui.message("general.save")}</button>
        <button type="button" class="cancel" ui-sref="list">${ui.message("general.cancel")}</button>
    </p>
</form>

