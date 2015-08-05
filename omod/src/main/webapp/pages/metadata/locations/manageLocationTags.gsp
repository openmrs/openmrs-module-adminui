 <%
    ui.decorateWith("appui", "standardEmrPage")
	ui.includeJavascript("adminui", "metadata/manageLocationTags.js")
	ui.includeCss("adminui", "adminui.css")
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
			<th>${ ui.message("general.name")}</th>
			<th>${ ui.message("general.description") }</th>
			<th class="adminui-action-column">${ ui.message("general.action") }</th>
		</tr>
	</thead>
	<tbody>
		<% locationTags.each{  %>
	 	<tr>
	 		<td ${it.retired ? 'class="adminui-retired"' : ''}>${ ui.format(it.name) }</td>
			<td ${it.retired ? 'class="adminui-retired"' : ''}>${ ui.format(it.description) }</td>
			<td>
                <form id="adminui-restore-form-${it.id}" method="POST">
                    <i class="icon-pencil edit-action${it.retired ? ' adminui-hidden' : ''}" title="${ ui.message("general.edit") }"
                       onclick="location.href='${ui.pageLink("adminui", "metadata/locations/locationTag",[locationTagId: it.id, action: 'edit'])}'"></i>
                    <% if(!it.retired) { %>
                    <i class="icon-remove delete-action" title="${ ui.message("general.retire") }" onclick="adminui_retireLocationTag(${it.id}, '${it.name}')"></i>
				    <% } else { %>
                    <i class="icon-reply edit-action" title="${ ui.message("general.restore") }" onclick="adminui_restoreLocationTag(${it.id})"></i>
				    <% } %>
                    <i class="icon-trash delete-action right" title="${ ui.message("general.purge") }" onclick="adminui_purgeLocationTag(${it.id}, '${it.name}')"></i>
                    <input type="hidden" name="locationTagId" value="${it.id}" />
                    <input id="adminui-restore-action" type="hidden" name="action" value="restore" />
                </form>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>

<div id="adminui-retire-location-tag-dialog" class="dialog" style="display: none">
    <div class="dialog-header">
		 <h3>${ui.message('adminui.locationTag.retire')}</h3>
    </div>
    <div class="dialog-content">
        <h4 id="retireLocationTagMessage">${ui.message('adminui.retire')}</h4>
        <form id="retireLocationTagForm" method="POST">
            <span>
                ${ui.message('general.reason')}: <input type="text" name="reason" placeholder="${ ui.message("emr.optional") }" />
            </span>
            <br/>
            <input type="hidden" name="action" value="retire">
            <input type="hidden" id="retire-location-tag-id" name="locationTagId" value="">
            <div>
                <button class="confirm right">${ ui.message("uicommons.confirm") }</button>
                <button class="cancel">${ ui.message("uicommons.cancel") }</button>
            </div>
        </form>
	 </div>
</div>

<div id="adminui-purge-location-tag-dialog" class="dialog" style="display: none">
     <div class="dialog-header">
         <h3>${ui.message('adminui.locationTag.purge')}</h3>
     </div>
     <div class="dialog-content">
         <h4 id="purgeLocationTagMessage">${ui.message('adminui.purge')}</h4>
         <form id="purgeLocationTagForm" method="post">
             <input type="hidden" name="action" value="purge" />
             <input type="hidden" id="purge-location-tag-id" name="locationTagId" value="" />
             <div>
                 <button class="confirm right">${ ui.message("uicommons.confirm") }</button>
                 <button class="cancel">${ ui.message("uicommons.cancel") }</button>
             </div>
         </form>
     </div>
</div>