 <%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
		{ icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
		{ label: "${ ui.message('coreapps.app.configureMetadata.label')}" , link: '${ui.pageLink("coreapps", "configuremetadata/configureMetadata")}'},
		{ label: "${ ui.message("adminui.app.locations.label")}", link: '${ui.pageLink("adminui","metadata/locations/locationsHome")}'},
		{ label: "${ ui.message("adminui.manageLocationTags.label")}" }

    ];
</script>

<h3>${ ui.message("adminui.manageLocationTags.label") }</h3>

 <input type="submit" class="button" value="${ui.message("adminui.addLocationTag.label")}" onclick="javascript:window.location='/${ contextPath }/adminui/metadata/locations/createLocationTag.page'"/>

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
	            <a href="/${ contextPath }/adminui/metadata/locations/createLocationTag.page?locationTagId=${ it.id }">
	                <button>${ ui.message("general.edit") }</button>
	            </a>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>
