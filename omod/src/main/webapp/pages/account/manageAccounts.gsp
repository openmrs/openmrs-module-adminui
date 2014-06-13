<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("admintoolsui", "account.css")
%>
<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("admintoolsui.app.system.administration.accountManager.label")}", link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}' },
        { label: "${ ui.message("emr.task.accountManagement.label")}" }
    ];
</script>

<h3>${  ui.message("emr.task.accountManagement.label") }</h3>

<a href="${ ui.pageLink("emr", "account/account") }">
    <button id="create-account-button">${ ui.message("emr.createAccount") }</button>
</a>

<%--
<hr>
<table id="list-accounts" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("emr.person.name")}</th>
			<th>${ ui.message("emr.user.username") }</th>
			<th>${ ui.message("emr.gender") }</th>
            <th>${ ui.message("emr.account.providerRole.label") }</th>
            <th>${ ui.message("emr.account.providerIdentifier.label") }</th>
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
				<% if(it.username && it.username != '') {%>
					${ ui.format(it.username) }
				<% } %>
			</td>
			<td>
				${ ui.format(it.person.gender) }
			</td>
            <td>
                ${ ui.format(it.providerRole) }
            </td>
            <td>
                ${ ui.format(it.provider?.identifier) }
            </td>
			<td>
	            <a href="/${ contextPath }/emr/account/account.page?personId=${ it.person.personId }">
	                <button>${ ui.message("emr.edit") }</button>
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

--%>