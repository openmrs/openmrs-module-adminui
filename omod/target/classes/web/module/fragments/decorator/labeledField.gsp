<%
    config.require("label")

    def labelPosition = config.labelPosition ?: "left"
%>

<% if (labelPosition == 'fieldset') { %>

    <fieldset>
        <legend>${ config.label }</legend>
        <%= config.content %>
    </fieldset>

<% } else if (labelPosition == "top") { %>

    <label for="${ config.id }-field">
        ${ config.label }
    </label>
    <div id="${ config.id}-field">
        <%= config.content %>
    </div>

<% } else { %>

    <label for="${ config.id }-field">
        ${ config.label }
    </label>
    <span id="${ config.id}-field">
        <%= config.content %>
    </span>

<% } %>
