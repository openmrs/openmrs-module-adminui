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

 <table id="list-props" cellspacing="0" cellpadding="20">
	<thead>
		<tr>
			<th>${ ui.message("adminui.globalProp.name")}</th>
			<th>${ ui.message("adminui.globalProp.value") }</th>
			<th>${ ui.message("adminui.globalProp.description") }</th>
            <th></th>
		</tr>
	</thead>
	<tbody>
		<% globalProperties.each{  %>
	 	<tr>
	 		<td>
				${ ui.format(it.property)}
			</td>

			<td>
				${ ui.format(it.propertyValue) }
			</td>

			<td>
				${ ui.format(it.description) }
			</div></td>

			<td>
	            <a href="/${ contextPath }/adminui/systemAdmin/createGlobalProperty.page?globalProperty=${ it.property }">
	                <button>${ ui.message("adminui.edit") }</button>
	            </a>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>
