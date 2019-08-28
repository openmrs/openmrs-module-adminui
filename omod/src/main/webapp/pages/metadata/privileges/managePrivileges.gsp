<%
    ui.decorateWith("appui", "standardEmrPage")
	ui.includeJavascript("adminui", "metadata/managePrivileges.js")
	ui.includeCss("adminui", "adminui.css")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('adminui.app.configureMetadata.label')}" , link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.managePrivileges.title")}" }
    ];
</script>

<h3>${ ui.message("adminui.managePrivileges.title") }</h3>

<input type="submit" class="button" value="${ui.message("adminui.addNewPrivilege.label")}" onclick="window.location='${ui.pageLink("adminui","metadata/privileges/privilege", [action: 'add'])}'"/>
<br />
<br />
<table class="table table-sm table-responsive-sm table-responsive-md table-responsive-lg table-responsive-xl" id="list-privileges" cellspacing="0" cellpadding="2">
	<thead>
		<tr>
			<th class="adminui-name-column">${ ui.message("general.name") }</th>
			<th>${ ui.message("general.description") }</th>
			<th class="adminui-action-column">${ ui.message("general.action") }</th>
		</tr>
	</thead>
	<tbody>
		<% privileges.each{  %>
	 	<tr >
	 		<td valign="top">
				 ${ ui.encodeHtmlContent(ui.format(it.name)) }
			</td>
			<td valign="top">
                ${ ui.encodeHtmlContent(ui.format(it.description)) }
            </td>
			<td valign="top">
                <form id="adminui-restore-form-${ui.encodeHtmlContent(it.name)}" method="POST">
                    <i class="icon-pencil edit-action${''}" title="${ ui.message("general.edit") }"
                       onclick="location.href='${ui.pageLink("adminui", "metadata/privileges/privilege",[privilegeName: ui.encodeJavaScriptAttribute(it.name), action: 'edit'])}'"></i>
                    <i class="icon-trash delete-action right" title="${ ui.message("general.purge") }" onclick="adminui_purgePrivilege('${ui.encodeJavaScriptAttribute(it.name)}', '${ui.encodeJavaScriptAttribute(it.name)}')"></i>
                    <input type="hidden" name="privilegeName" value="${ui.encodeHtmlContent(it.name)}" />
                    <input id="adminui-restore-action" type="hidden" name="action" value="restore" />
                </form>
        	</td>
		</tr>
		<% } %>
	</tbody>
</table>

<div id="adminui-purge-privilege-dialog" class="dialog" style="display: none">
     <div class="dialog-header">
         <h3>${ui.message('adminui.privilege.purge')}</h3>
     </div>
     <div class="dialog-content">
         <h4 id="purgePrivilegeMessage">${ui.message('adminui.purge')}</h4>
         <form id="purgePrivilegeForm" method="post">
             <input type="hidden" name="action" value="purge" />
             <input type="hidden" id="purge-privilege-name" name="privilegeName" value="" />
             <input type="hidden" id="purgePrivilegeMessageTemplate" value="${ui.message('adminui.purge')}"/>
             <br />
             <div>
                 <button class="confirm right">${ ui.message("uicommons.confirm") }</button>
                 <button class="cancel">${ ui.message("uicommons.cancel") }</button>
             </div>
         </form>
     </div>
</div>


<% if ( (privileges != null) && (privileges.size() > 0) ) { %>
${ ui.includeFragment("uicommons", "widget/dataTable", [ object: "#list-privileges",
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
