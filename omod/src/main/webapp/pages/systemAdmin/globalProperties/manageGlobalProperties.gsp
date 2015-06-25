 <%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('coreapps.app.systemAdministration.label')}" , link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}'},
        { label: "${ ui.message('adminui.globalProperties.label')}" }
       ];
</script>

<h1>
    <h3>${ ui.message("adminui.globalProperties.label") }</h3>
</h1>

 <input type="submit" class="button" value="${ui.message("adminui.addGlobalProperty.label")}" onclick="javascript:window.location='/${ contextPath }/adminui/systemadmin/globalproperties/globalProperty.page'"/>

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
                   onclick="location.href='${ui.pageLink("adminui","systemadmin/globalproperties/globalProperty",[globalProperty:it.property ])}'"></i>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>
