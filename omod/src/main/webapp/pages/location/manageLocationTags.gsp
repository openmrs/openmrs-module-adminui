 <%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.locationManager.label")}" , link: '${ui.pageLink("adminui", "location/manageLocations")}'},
        { label: "${ ui.message("adminui.manageLocationTag.locationManagement.label")}" }

    ];
</script>

<h1>
    <h3>${ ui.message("adminui.manageLocationTag.locationManagement.label") }</h3>
</h1>

 <input type="submit" class="button" value="${ui.message("adminui.createLocationTag")}" onclick="javascript:window.location='/${ contextPath }/adminui/location/createLocationTag.page'"/>

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
	            <a href="/${ contextPath }/adminui/location/createLocationTag.page?locationTagId=${ it.id }">
	                <button>${ ui.message("adminui.edit") }</button>
	            </a>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>
