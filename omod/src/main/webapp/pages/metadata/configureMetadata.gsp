<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("adminui.app.configureMetadata.label") ])
    ui.includeCss("adminui", "adminui.css")
    ui.includeCss("adminui", "configureMetadata.css")

    def htmlSafeId = { extension ->
        "${ extension.id.replace(".", "-") }-${ extension.id.replace(".", "-") }-extension"
    }
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.configureMetadata.label")}" }
    ];
</script>

<table id="adminui-configure-metadata-table">
    <tbody>
    <tr>
    <%
        def maxGroupNumber = 8;
        def maxColumnNumber = 3;
        def groupCount = 1
        def columnCount = 1
    %>
        <td valign="top">
    <% adminGroupAndLinksMap.each { group, links -> %>
    <% if(groupCount > maxGroupNumber && columnCount < maxColumnNumber) { %>
        <%
            columnCount++
            groupCount = 1
        %>
        </td>
        <td valign="top">
        <% } %>
            <h6><% if (group.icon) { %><i class="${ group.icon }"></i> <% } %> ${ ui.message(group.label) }</h6>
            <ul id="adminui-menu-item">
                <% links.each { link -> %>
                <li>
                    <a id="${ htmlSafeId(link) }" href="/${ contextPath }/${ link.url }">
                        ${ ui.message(link.label) }
                    </a>
                </li>
                <% } %>
            </ul>
    <% groupCount++ %>
    <% } %>
        </td>
    </tr>
    </tbody>
</table>
