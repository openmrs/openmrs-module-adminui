<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("adminui", "account.css")
%>
<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.accountManager.label")}", link: '${ui.pageLink("adminui", "account/manageAccounts")}' },
        { label: "${ ui.message("adminui.managePrivileges.accountManagement.label")}" }
    ];
</script>

<% i=0 %>

<hr>
<table id="list-levels" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("adminui.sno")}</th>
			<th>${ ui.message("adminui.privilege.name") }</th>
			<th>${ ui.message("adminui.privilege.children") }</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<% privilegeLevels.each{  %>
	 	<tr>
	 		<td>
				${ ++i }
			</td>
			<td>
				 ${ ui.format(it.name) }
			</td>
			<td>
				${ ui.format(it.childRoles) }
            </td>
			<td>
	            <a href="/${ contextPath }/adminui/account/managePrivileges.page }">
	                <button>${ ui.message("adminui.edit") }</button>
	            </a>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>


<% if ( (privilegeLevels != null) && (privilegeLevels.size() > 0) ) { %>
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
