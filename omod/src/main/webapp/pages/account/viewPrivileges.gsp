<%
    ui.decorateWith("appui", "standardEmrPage")
%>
<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.administrationTools.label")}" , link: '${ui.pageLink("adminui", "adminUiHome")}'},
        { label: "${ ui.message("adminui.app.accountManager.label")}", link: '${ui.pageLink("adminui", "account/manageAccounts")}' },
        { label: "${ ui.message("adminui.viewPrivileges.accountManagement.label")}" }
    ];
</script>

<input type="submit" class="button" value="${ui.message("adminui.addNewPrivilege")}" onclick="javascript:window.location='${ui.pageLink("adminui","account/privilege", [action: 'add'])}'"/>
<br />
<br />
<table>
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
	            <i class="icon-pencil edit-action" title="${ ui.message("general.edit") }"
                   onclick="location.href='${ui.pageLink("adminui","account/privilege",[privilegeName: it.name, action: 'edit'])}'"></i>
            </td>
		</tr>
		<% } %>
	</tbody>
</table>


<% if ( (privileges != null) && (privileges.size() > 0) ) { %>
${ ui.includeFragment("uicommons", "widget/dataTable", [ object: "#list-levels",
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
