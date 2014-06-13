<%
    config.require("uuid")
    config.require("id")
    config.require("label")
%>

<p id="${config.uuid}" style="display: none">
    <label for="${ config.id }-field">${ ui.message(config.label) }</label>
    <select id="locations-field" name="locations-field">
        <option value="8">Emergency</option>
        <option value="Others">Others</option>
    </select>
</p>