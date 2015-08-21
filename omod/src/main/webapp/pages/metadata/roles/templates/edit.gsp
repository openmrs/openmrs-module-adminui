<h2 ng-hide="role.uuid">${ ui.message("adminui.role.addNewRole.title") }</h2>
<h2 ng-show="role.uuid">${ ui.message("adminui.role.editRole.title") }</h2>

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
        <label>${ ui.message("adminui.role.role") }</label>
        <input ng-model="role.name" required/>
    </p>
    <p>
        <label>${ ui.message("adminui.role.description") }</label>
        <input ng-model="role.description" required/>
    </p>
    <p>
		<label>${ ui.message("adminui.role.containedRoles.label") } </label>{{role.name}}
    </p>
    <p>
    	<om-coltable om-ct-main="dependantRoles" om-ct-cols="2" om-ct-ckb="false" om-ct-sec=""></om-coltable>
    </p>
    <p>
        <label>${ ui.message("adminui.role.inheritedRoles") }</label>
    </p>
    <p>
    	<om-coltable om-ct-main="roles" om-ct-cols="2" om-ct-ckb="true" om-ct-sec="inheritedRoles"></om-coltable>
    </p>
    <p>
    	<label>${ ui.message("adminui.role.inheritedPrivileges.label") }</label>
    </p>
    <p>
    	<om-coltable om-ct-main="inheritedPrivileges" om-ct-cols="2" om-ct-ckb="false" om-ct-sec=""></om-coltable>
     </p>
    <p>
        <label>${ ui.message("adminui.role.privileges") }</label>
    </p>
     <p>
    	<om-coltable om-ct-main="privileges" om-ct-cols="2" om-ct-ckb="true" om-ct-sec="privilegeFlags"></om-coltable>
    </p>

    <p>
        <button ng-disabled="roleForm.\$invalid" type="submit" class="confirm right">${ui.message("general.save")}</button>
        <button type="button" class="cancel" ui-sref="list">${ui.message("general.cancel")}</button>
    </p>
</form>

