<%
    config.require("uuid")
    config.require("id")
    config.require("label")
    def date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date());
%>

<p id="${config.id}">
    <label for="${ config.id }-field">${ ui.message(config.label) }</label>
    <input type="date" id="${ config.id }-field" name="${ config.id }" value="${date}"  />
</p>