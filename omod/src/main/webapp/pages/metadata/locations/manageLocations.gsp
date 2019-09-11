<%
    ui.decorateWith("appui", "standardEmrPage")
    
    ui.includeCss("adminui", "adminui.css")
	ui.includeJavascript("appui", "jquery-3.4.1.min.js")
%>

<style type="text/css">
	table td {
	    max-width: 100px;
	    overflow: hidden;
	    text-overflow: ellipsis;
	    white-space: nowrap;
	}
</style>

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
<table  class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl" id="list-locations" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th>${ ui.message("general.name")}</th>
			<th>${ ui.message("general.description") }</th>
			<th>${ ui.message("adminui.location.tags") }</th>
            <th class="adminui-action-column">${ ui.message("general.action") }</th>
		</tr>
	</thead>
	<tbody>
		<% locations.each{  %>
	 	<tr>
	 		<td valign="top" <% if (it.location.retired) { %> class="retired" <% } %> >
	 			<% for (i = 1; i <= it.depth; i++) { %>
	 				&nbsp;&nbsp;
	 			<% } %>
				${ ui.encodeHtmlContent(it.location.name) }
			</td>

			<td valign="top" <% if (it.location.retired) { %> class="retired" <% } %>>
				${ ui.encodeHtmlContent(it.location.description ?: '') }
			</td>

            <td valign="top" <% if (it.location.retired) { %> class="retired" <% } %>>
                <% it.location.tags.eachWithIndex { tag, index -> %>
					${ ui.encodeHtmlContent(ui.format(tag))} <% if (index < it.location.tags.size() - 1) { %> , <% } %>
				<% } %>
            </td>

			<td valign="top">
				<% if (!it.location.retired) { %>
		            <i class="icon-pencil edit-action" title="${ ui.message("general.edit") }"
					   onclick="location.href='${ui.pageLink("adminui", "metadata/locations/location",[locationId: it.location.id])}'">
					</i>
					
					<i class="icon-remove delete-action" title="${ui.message("emr.delete")}" onclick="retireLocation('${ ui.encodeJavaScriptAttribute(it.location.name) }', ${ it.location.id})"></i>
				<% } %>
				
	            <% if (it.location.retired) { %>
	                <i class="icon-reply edit-action" title="${ui.message("general.restore")}" onclick="restoreLocation(${ it.location.id})"></i>
	            <% } %>

	            &nbsp;&nbsp;<i class="icon-trash delete-action" title="${ui.message("general.purge")}" onclick="purgeLocation('${ ui.encodeJavaScriptAttribute(it.location.name) }', ${ it.location.id})"></i>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>

<div id="adminui-retire-location-dialog" class="dialog" style="display: none">
    <div class="dialog-header">
        <h3>${ ui.message("adminui.location.retire") }</h3>
    </div>
    <div class="dialog-content">
        <h4>
	        <span id="retireLocationMessage"/>
	    </h4>
        <form method="POST" action="manageLocations.page">
        	<p>
            	<input type="text" id="reason" name="reason" placeholder="${ ui.message("emr.optional") }"/>
            </p>
            <br/>
            <input type="hidden" id="locationId" name="locationId"/>
            <input type="hidden" name="action" value="retire"/>
            <button class="confirm right" type="submit">${ ui.message("general.yes") }</button>
            <button class="cancel">${ ui.message("general.no") }</button>
        </form>
    </div>
</div>

<div style="display: none">
	<form id="adminui-restore-location-form" method="POST" action="manageLocations.page">
	    <input type="hidden" id="locationId" name="locationId"/>
	    <input type="hidden" name="action" value="restore"/>
    </form>
</div>

<div id="adminui-purge-location-dialog" class="dialog" style="display: none">
    <div class="dialog-header">
        <h3>${ ui.message("adminui.location.purge") }</h3>
    </div>
    <div class="dialog-content">
        <ul>
            <li class="info">
                <span id="purgeLocationMessage"/>
            </li>
        </ul>
        <form method="POST" action="manageLocations.page">
            <input type="hidden" id="locationId" name="locationId"/>
            <input type="hidden" name="action" value="purge"/>
            <button class="confirm right" type="submit">${ ui.message("general.yes") }</button>
            <button class="cancel">${ ui.message("general.no") }</button>
        </form>
    </div>
</div>

<script type="text/javascript">

	var retireLocationDialog = null;
	var purgeLocationDialog = null;

	jq(document).ready( function() {
	    
	    retireLocationDialog = emr.setupConfirmationDialog({
	        selector: '#adminui-retire-location-dialog',
	        actions: {
	            cancel: function() {
	            	retireLocationDialog.close();
	            }
	        }
	    });
	    
	    purgeLocationDialog = emr.setupConfirmationDialog({
	        selector: '#adminui-purge-location-dialog',
	        actions: {
	            cancel: function() {
	            	purgeLocationDialog.close();
	            }
	        }
	    });
	
	});

	function retireLocation(name, id) {
	    jq("#adminui-retire-location-dialog #locationId").val(id);
	    jq("#retireLocationMessage").text('${ ui.message("adminui.retire") }'.replace('{0}', name));
	    retireLocationDialog.show();
	}
	
	function restoreLocation(id) {
	    jq("#adminui-restore-location-form #locationId").val(id);
	    jq("#adminui-restore-location-form").submit();
	}
	
	function purgeLocation(name, id) {
	    jq("#adminui-purge-location-dialog #locationId").val(id);
	    jq("#purgeLocationMessage").text('${ ui.message("adminui.purge") }'.replace('{0}', name));
	    purgeLocationDialog.show();
	}
	
</script>
