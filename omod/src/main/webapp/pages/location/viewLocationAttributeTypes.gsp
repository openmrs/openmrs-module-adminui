 <%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.locationManager.label")}" , link: '${ui.pageLink("adminui", "location/manageLocations")}'},
        { label: "${ ui.message("adminui.manageLocationAttributeTypes.locationManagement.label")}" }

    ];
</script>

<h1>
    <h3>${ ui.message("adminui.manageLocationAttributeTypes.locationManagement.label") }</h3>
</h1>

 <input type="submit" class="button" value="${ui.message("adminui.createLocationAttributeType")}" onclick="javascript:window.location='/${ contextPath }/adminui/location/createLocationAttributeType.page'"/>

<br><br>

 <table id="list-attributeTypes" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("adminui.locationAttributeType.name")}</th>
			<th>${ ui.message("adminui.locationAttributeType.description") }</th>
			<th>${ ui.message("adminui.locationAttributeType.datatypeConfig") }</th>
			<th>${ ui.message("adminui.locationAttributeType.dateCreated") }</th>
            <th></th>
		</tr>
	</thead>
	<tbody>
		<% locationAttributeTypes.each{  %>
	 	<tr>
	 		<td>
				${ ui.format(it.name)}
			</td>

			<td>
				${ ui.format(it.description) }
			</td>

			<td>
				${ ui.format(it.datatypeConfig) }
			</td>

			<td>
				${ ui.format(it.dateCreated) }
			</td>

			<td>
	            <a href="/${ contextPath }/adminui/location/createLocationAttributeType.page?locationAttributeTypeId=${ it.id }">
	                <button>${ ui.message("adminui.edit") }</button>
	            </a>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>
