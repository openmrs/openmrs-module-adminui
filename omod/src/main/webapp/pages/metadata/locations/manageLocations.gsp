<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<script type="text/javascript">
    var breadcrumbs = [
		{ icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
		{ label: "${ ui.message('adminui.app.configureMetadata.label')}" , link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
		{ label: "${ ui.message("adminui.manageLocations.label")}" }
    ];
</script>

    <a class="button" href="${ ui.pageLink("adminui", "metadata/locations/location") }">
            <i class="icon-plus"></i>
            ${ ui.message("adminui.addNewLocation.label") }
    </a>

<hr>
<table id="list-locations" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("general.name")}</th>
			<th>${ ui.message("general.description") }</th>
			<th>${ ui.message("adminui.location.tags") }</th>
            <th>${ ui.message("general.action") }</th>
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
                <% it.tags.eachWithIndex { tag, index -> %>
					${ ui.format(tag.name)} <% if (index < it.tags.size() - 1) { %> , <% } %>
				<% } %>
            </td>

			<td>
				<% if (!it.retired) { %>
		            <i class="icon-pencil edit-action" title="${ ui.message("general.edit") }"
					   onclick="location.href='${ui.pageLink("adminui", "metadata/locations/location",[locationId: it.id])}'">
					</i>
				<% } %>
				
				<% if (!it.retired) { %>
                	<i class="icon-remove delete-action" title="${ui.message("emr.delete")}"></i>
	            <% } %>
	            
	            <% if (it.retired) { %>
	                <i class="icon-reply edit-action" title="${ui.message("general.restore")}"></i>
	            <% } %>
	           
	            <i class="icon-trash delete-action" title="${ui.message("general.purge")}"></i>
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
