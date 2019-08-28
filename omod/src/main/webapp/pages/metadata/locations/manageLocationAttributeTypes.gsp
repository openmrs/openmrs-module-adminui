 <%
    context.requirePrivilege('Manage Location Attribute Types')
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeJavascript("adminui", "metadata/manageLocationAttributeTypes.js")
    ui.includeCss("adminui", "adminui.css")
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

 <table class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl" id="list-attributeTypes" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th class="adminui-name-column">${ ui.message("general.name")}</th>
			<th>${ ui.message("general.description") }</th>
			<th class="adminui-action-column">${ ui.message("general.action") }</th>
		</tr>
	</thead>
	<tbody>
		<% locationAttributeTypes.each{  %>
	 	<tr>
            <td valign="top" ${it.retired ? 'class="retired"' : ''}>${ ui.encodeHtmlContent(ui.format(it.name)) }</td>
            <td valign="top" ${it.retired ? 'class="retired"' : ''}>${ ui.encodeHtmlContent(ui.format(it.description)) }</td>
            <td valign="top">
                <form id="adminui-restore-form-${it.id}" method="POST">
                    <i class="icon-pencil edit-action${it.retired ? ' invisible' : ''}" title="${ ui.message("general.edit") }"
                    onclick="location.href='${ui.pageLink("adminui", "metadata/locations/locationAttributeType",[locationAttributeTypeId: it.id])}'"></i>
                    <% if(!it.retired) { %>
                    <i class="icon-remove delete-action" title="${ ui.message("general.retire") }" onclick="adminui_retireLocationAttributeType(${it.id}, '${ui.encodeJavaScriptAttribute(it.name)}')"></i>
                    <% } else { %>
                    <i class="icon-reply edit-action" title="${ ui.message("general.restore") }" onclick="adminui_restoreLocationAttributeType(${it.id})"></i>
                    <% } %>
                    <% if(context.hasPrivilege('Purge Location Attribute Types')) { %>
                    <i class="icon-trash delete-action right" title="${ ui.message("general.purge") }" onclick="adminui_purgeLocationAttributeType(${it.id}, '${ui.encodeJavaScriptAttribute(it.name)}')"></i>
                    <% } %>
                    <input type="hidden" name="locationAttributeTypeId" value="${it.id}" />
                    <input id="adminui-restore-action" type="hidden" name="action" value="restore" />
                </form>
            </td>
        </tr>
		<% } %>
	</tbody>
</table>

 <div id="adminui-retire-location-attribute-type-dialog" class="dialog" style="display: none">
     <div class="dialog-header">
         <h3>${ui.message('adminui.locationAttributeType.retire')}</h3>
     </div>
     <div class="dialog-content">
         <h4 id="retireLocationAttributeTypeMessage">${ui.message('adminui.retire')}</h4>
         <form id="retireLocationAttributeTypeForm" method="POST">
             <span>
                 ${ui.message('general.reason')}: <input type="text" name="reason" placeholder="${ ui.message("emr.optional") }" />
             </span>
             <br/>
             <input type="hidden" name="action" value="retire">
             <input type="hidden" id="retire-location-attribute-type-id" name="locationAttributeTypeId" value="">
             <input type="hidden" id="retireLocationAttributeTypeMessageTemplate" value="${ui.message('adminui.retire')}">
             <div>
                 <button class="confirm right">${ ui.message("uicommons.confirm") }</button>
                 <button class="cancel">${ ui.message("uicommons.cancel") }</button>
             </div>
         </form>
     </div>
 </div>

 <div id="adminui-purge-location-attribute-type-dialog" class="dialog" style="display: none">
     <div class="dialog-header">
         <h3>${ui.message('adminui.locationAttributeType.purge')}</h3>
     </div>
     <div class="dialog-content">
         <h4 id="purgeLocationAttributeTypeMessage">${ui.message('adminui.purge')}</h4>
         <form id="purgeLocationAttributeTypeForm" method="post">
             <input type="hidden" name="action" value="purge" />
             <input type="hidden" id="purge-location-attribute-type-id" name="locationAttributeTypeId" value="" />
             <input type="hidden" id="purgeLocationAttributeTypeMessageTemplate" value="${ui.message('adminui.purge')}" />
             <br />
             <div>
                 <button class="confirm right">${ ui.message("uicommons.confirm") }</button>
                 <button class="cancel">${ ui.message("uicommons.cancel") }</button>
             </div>
         </form>
     </div>
 </div>
