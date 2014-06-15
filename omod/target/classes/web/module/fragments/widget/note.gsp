<%
    config.require("noteType", "message") // noteType = success, error, warning
    def icon = ""
    if (config.noteType == "success")
        icon = "icon-ok"
    else if (config.noteType == "warning")
        icon = "icon-warning-sign"
%>
<div id="${config.id}" class="note-container">
    <div class="note ${ config.noteType }">
        <div class="text">
            <i class="${ icon } medium"></i>
            ${ config.message }
        </div>

        <% if (config.additionalContent) { %>
            ${ config.additionalContent }
        <% } %>

        <% if (config.showCloseIcon) { %>
            <div class="close-icon"><i class="icon-remove"></i></div>
        <% } %>
    </div>
</div>