 <%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.administrationTools.label")}" , link: '${ui.pageLink("adminui", "adminUiHome")}'},
        { label: "${ ui.message("adminui.app.systemAdministration.label")}" , link: '${ui.pageLink("adminui", "systemAdmin/home")}'},
        { label: "${ ui.message("adminui.globalProps.systemAdministration.label")}" }

    ];
</script>

<h1>
    <h3>${ ui.message("adminui.globalProps.systemAdministration.label") }</h3>
</h1>

 <input type="submit" class="button" value="${ui.message("adminui.createGlobalProperty")}" onclick="javascript:window.location='/${ contextPath }/adminui/systemAdmin/createGlobalProperty.page'"/>

<br><br>

 <table style="table-layout: fixed; width: 100%">
    <thead>
		<tr>
			<th>${ ui.message("adminui.globalProp.name")}</th>
			<th style="width: 38%">${ ui.message("adminui.globalProp.value") }</th>
            <th style="width: 15px"></th>
		</tr>
	</thead>
	<tbody>
		<% globalProperties.each{  %>
	 	<tr>
	 		<td>
				<b>${ ui.format(it.property)}</b><br />
                <i style="font-size: 0.7em;">${ ui.format(it.description) }</i>
			</td>
			<td>
                ${ ui.format(it.propertyValue) }
			</td>
			<td>
                <i class="icon-pencil edit-action" title="${ ui.message("general.edit") }"
                   onclick="location.href='${ui.pageLink("adminui","systemAdmin/createGlobalProperty",[globalProperty:it.property ])}'"></i>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>
