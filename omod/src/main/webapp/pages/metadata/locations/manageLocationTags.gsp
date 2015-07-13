 <%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
		{ icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
		{ label: "${ ui.message('adminui.app.configureMetadata.label')}" , link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
		{ label: "${ ui.message("adminui.manageLocationTags.label")}" }

    ];
</script>

<input type="submit" class="button" value="${ui.message("adminui.addLocationTag.label")}" onclick="window.location='/${ contextPath }/adminui/metadata/locations/locationTag.page'"/>

<br><br>

 <table id="list-tags" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("adminui.locationTag.name")}</th>
			<th>${ ui.message("adminui.locationTag.description") }</th>
			<th>${ ui.message("adminui.locationTag.creator") }</th>
			<th>${ ui.message("adminui.locationTag.dateCreated") }</th>
            <th></th>
		</tr>
	</thead>
	<tbody>
		<% locationTags.each{  %>
	 	<tr>
	 		<td>
				${ ui.format(it.name)}
			</td>

			<td>
				${ ui.format(it.description) }
			</td>

			<td>
				${ ui.format(it.creator) }
			</td>

			<td>
				${ ui.format(it.dateCreated) }
			</td>

			<td>
	            <i class="icon-pencil edit-action" title="${ ui.message("general.edit") }"
				   onclick="location.href='${ui.pageLink("adminui", "metadata/locations/locationTag",[locationTagId: it.id])}'"></i>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>
