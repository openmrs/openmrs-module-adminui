 <%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
		{ icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
		{ label: "${ ui.message('coreapps.app.configureMetadata.label')}" , link: '${ui.pageLink("coreapps", "configuremetadata/configureMetadata")}'},
		{ label: "${ ui.message("adminui.manageLocationAttributeTypes.label")}" }

    ];
</script>

 <h3>${ ui.message("adminui.manageLocationAttributeTypes.label") }</h3>

 <input type="submit" class="button" value="${ui.message("adminui.addLocationAttributeType.label")}" onclick="javascript:window.location='/${ contextPath }/adminui/metadata/locations/createLocationAttributeType.page'"/>

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
	            <a href="/${ contextPath }/adminui/metadata/locations/createLocationAttributeType.page?locationAttributeTypeId=${ it.id }">
	                <button>${ ui.message("general.edit") }</button>
	            </a>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>
