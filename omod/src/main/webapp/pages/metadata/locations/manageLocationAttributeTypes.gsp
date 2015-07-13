 <%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
		{ icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
		{ label: "${ ui.message('adminui.app.configureMetadata.label')}" , link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
		{ label: "${ ui.message("adminui.manageLocationAttributeTypes.label")}" }

    ];
</script>

<input type="submit" class="button" value="${ui.message("adminui.addNewLocationAttributeType.label")}" onclick="window.location='/${ contextPath }/adminui/metadata/locations/locationAttributeType.page'"/>

<br><br>

 <table id="list-attributeTypes" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("general.name")}</th>
			<th>${ ui.message("general.description") }</th>
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
	            <i class="icon-pencil edit-action" title="${ ui.message("general.edit") }"
				   onclick="location.href='${ui.pageLink("adminui", "metadata/locations/locationAttributeType",[locationAttributeTypeId: it.id])}'"></i>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>
