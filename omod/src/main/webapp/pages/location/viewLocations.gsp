<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.locationManager.label")}", link: '${ui.pageLink("adminui", "location/manageLocations")}' },
        { label: "${ ui.message("adminui.viewLocation.locationManagement.label")}" }
    ];
</script>


<hr>
<table id="list-locations" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("adminui.location.name")}</th>
			<th>${ ui.message("adminui.location.description") }</th>
			<th>${ ui.message("adminui.location.tags") }</th>
            <th></th>
		</tr>
	</thead>
	<tbody>
		<% locations.each{  %>
	 	<tr>
	 		<td>
				${ ui.format(it.name)}
			</td>

			<td>
				${ ui.format(it.description) }
			</td>

            <td>
                <% it.tags.each{ tag-> %>
					${ ui.format(tag.name)+','}
				<% } %>
            </td>

			<td>
	            <a href="/${ contextPath }/adminui/location/location.page?locationId=${ it.locationId }">
	                <button>${ ui.message("adminui.edit") }</button>
	            </a>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>


<% if ( (locations != null) && (locations.size() > 0) ) { %>
${ ui.includeFragment("uicommons", "widget/dataTable", [ object: "#list-locations",
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
