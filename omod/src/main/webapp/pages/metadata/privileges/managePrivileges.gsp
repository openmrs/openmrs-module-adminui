<%
    ui.decorateWith("appui", "standardEmrPage")
	ui.includeJavascript("adminui", "metadata/managePrivileges.js")
	ui.includeCss("adminui", "adminui.css")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('adminui.app.configureMetadata.label')}" , link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.managePrivileges.title")}" }
    ];
</script>

<h3>${ ui.message("adminui.managePrivileges.title") }</h3>

<input type="submit" class="button" value="${ui.message("adminui.addNewPrivilege.label")}" onclick="window.location='${ui.pageLink("adminui","metadata/privileges/privilege", [action: 'add'])}'"/>
<br />
<br />
<table id="list-privileges" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("general.name") }</th>
			<th>${ ui.message("general.description") }</th>
			<th>${ ui.message("general.action") }</th>
		</tr>
	</thead>
	<tbody>
		<% privileges.each{  %>
	 	<tr ng-class="{ retired: privilege.retired }">
	 		<td>
				 ${ ui.format(it.name) }
			</td>
			<td>
                ${ ui.format(it.description) }
            </td>
			<td>
                <form id="adminui-restore-form-${it.name}" method="POST">
                    <i class="icon-pencil edit-action${it.retired ? ' adminui-hidden' : ''}" title="${ ui.message("general.edit") }"
                       onclick="location.href='${ui.pageLink("adminui", "metadata/privileges/privilege",[privilegeName: it.name, action: 'edit'])}'"></i>
                    <% if(!it.retired) { %>
                    <i class="icon-remove delete-action" title="${ ui.message("general.retire") }" onclick="adminui_retirePrivilege('${it.name}', '${it.name}')"></i>
				    <% } else { %>
                    <i class="icon-reply edit-action" title="${ ui.message("general.restore") }" onclick="adminui_restorePrivilege('${it.name}')"></i>
				    <% } %>
                    <i class="icon-trash delete-action right" title="${ ui.message("general.purge") }" onclick="adminui_purgePrivilege('${it.name}', '${it.name}')"></i>
                    <input type="hidden" name="privilegeName" value="${it.name}" />
                    <input id="adminui-restore-action" type="hidden" name="action" value="restore" />
                </form>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>


<div id="adminui-retire-privilege-dialog" class="dialog" style="display: none">
    <div class="dialog-header">
		 <h3>${ui.message('adminui.privilege.retire')}</h3>
    </div>
    <div class="dialog-content">
        <h4 id="retirePrivilegeMessage">${ui.message('adminui.retire')}</h4>
        <form id="retirePrivilegeForm" method="POST">
            <span>
                ${ui.message('general.reason')}: <input type="text" name="reason" placeholder="${ ui.message("emr.optional") }" />
            </span>
            <br/>
            <input type="hidden" name="action" value="retire">
            <input type="hidden" id="retire-privilege-name" name="privilegeName" value="">
            <div>
                <button class="confirm right">${ ui.message("uicommons.confirm") }</button>
                <button class="cancel">${ ui.message("uicommons.cancel") }</button>
            </div>
        </form>
	 </div>
</div>

<div id="adminui-purge-privilege-dialog" class="dialog" style="display: none">
     <div class="dialog-header">
         <h3>${ui.message('adminui.privilege.purge')}</h3>
     </div>
     <div class="dialog-content">
         <h4 id="purgePrivilegeMessage">${ui.message('adminui.purge')}</h4>
         <form id="purgePrivilegeForm" method="post">
             <input type="hidden" name="action" value="purge" />
             <input type="hidden" id="purge-privilege-name" name="privilegeName" value="" />
             <br />
             <div>
                 <button class="confirm right">${ ui.message("uicommons.confirm") }</button>
                 <button class="cancel">${ ui.message("uicommons.cancel") }</button>
             </div>
         </form>
     </div>
</div>


<% if ( (privileges != null) && (privileges.size() > 0) ) { %>
${ ui.includeFragment("uicommons", "widget/dataTable", [ object: "#list-privileges",
        options: [
                bFilter: true,
                bJQueryUI: true,
                bLengthChange: false,
                iDisplayLength: 10,
                sPaginationType: '\"full_numbers\"',
                bSort: false,
                sDom: '\'ft<\"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg \"ip>\''
        ]
]) }
<% } %>
