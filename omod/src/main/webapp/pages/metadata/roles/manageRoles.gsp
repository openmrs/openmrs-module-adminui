<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("adminui", "account.css")
%>
<script type="text/javascript">
    var breadcrumbs = [
		{ icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
		{ label: "${ ui.message('adminui.app.configureMetadata.label')}" , link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
		{ label: "${ ui.message("adminui.manageRoles.title")}" }
    ];
</script>

<% i=0 %>

<h3>${ ui.message("adminui.manageRoles.title") }</h3>

<input type="submit" class="button" value="${ui.message("adminui.addNewRole.label")}" onclick="window.location='/${ contextPath }/adminui/metadata/roles/role.page'"/>

<hr>
<table id="list-roles" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("adminui.role.role") }</th>
			<th>${ ui.message("adminui.role.inheritedRoles") }</th>
			<th>${ ui.message("adminui.role.privileges") }</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<% roles.each{  %>
	 	<tr>
	 		<td>
				 ${ ui.format(it.role) }
			</td>

            <td>
				 ${ ui.format(it.inheritedRoles) }
			</td>

			<td>
				 ${ ui.format(it.privileges) }
			</td>

			<td>
	            <a href="/${ contextPath }/adminui/account/createCapability.page?capabilityName=${ it.role }">
	                <button>${ ui.message("general.edit") }</button>
	            </a>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>


<% if ( (roles != null) && (roles.size() > 0) ) { %>
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
