<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("adminui", "account.css")
%>
<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.accountManager.label")}", link: '${ui.pageLink("adminui", "account/manageAccounts")}' },
        { label: "${ ui.message("adminui.viewAccount.accountManagement.label")}" }
    ];
</script>


<hr>
<table id="list-accounts" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("adminui.person.name")}</th>
			<th>${ ui.message("adminui.user.username") }</th>
			<th>${ ui.message("adminui.gender") }</th>
            <th>${ ui.message("adminui.account.providerRole.label") }</th>
            <th>${ ui.message("adminui.account.personIdentifier.label") }</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<% accounts.each{  %>
	 	<tr>
	 		<td>
				${ ui.format(it.person.personName)}
			</td>
			<td>
				<% it.users.each{ %>
					${ ui.format(it.username)}
				<% } %>
			</td>
			<td>
				${ ui.format(it.person.gender) }
			</td>
            <td>
                ${ ui.format(it.providerSet) }
            </td>
            <td>
                ${ ui.format(it.person.personId) }
            </td>
			<td>
	            <a href="/${ contextPath }/adminui/account/account.page?personId=${ it.person.personId }">
	                <button>${ ui.message("adminui.edit") }</button>
	            </a>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>


<% if ( (accounts != null) && (accounts.size() > 0) ) { %>
${ ui.includeFragment("uicommons", "widget/dataTable", [ object: "#list-accounts",
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
