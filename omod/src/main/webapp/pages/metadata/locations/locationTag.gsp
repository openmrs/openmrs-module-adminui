<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeJavascript("adminui", "jquery.validate.js")
    ui.includeCss("adminui", "adminui.css")

    def createLocationTag = (locationTag.locationTagId == null ? true : false);
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('adminui.app.configureMetadata.label')}" , link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.manageLocationTags.label")}", link: '${ui.pageLink("adminui","metadata/locations/manageLocationTags")}'},
        { label: "${ ui.message((createLocationTag) ? "adminui.addLocationTag.label" : "adminui.editLocationTag.label")}" }

    ];
</script>

<script type="text/javascript">

    jq().ready(function () {

        jq("#locationTagForm").validate({
            rules: {
                "name": {
                    required: true,
                    maxlength: 255
                },
                "description": {
                    maxlength: 1024
                }
            },
            errorClass: "field-error",
            validClass: "",
            onfocusout: function (element) {
                jq(element).valid();
            },
            errorPlacement: function (error, element) {
                element.next().text(error.text());
            },
            highlight: function (element, errorClass, validClass) {
                jq(element).addClass(errorClass);
                jq(element).next().addClass(errorClass);
                jq(element).next().show();
            },
            unhighlight: function (element, errorClass, validClass) {
                jq(element).removeClass(errorClass);
                jq(element).next().removeClass(errorClass);
                jq(element).next().hide();
            }
        });
    });
</script>

<h2>${ ui.message((createLocationTag) ? "adminui.addLocationTag.label" : "adminui.editLocationTag.label") }</h2>

<% if(!createLocationTag) { %>
<fieldset class="right adminui-auditInfo">
    <legend>${ui.message('adminui.auditInfo')}</legend>
    <p>
        <label class="adminui-label">${ui.message('general.uuid')}:</label> ${ locationTag.uuid }
    </p>
    <% if(locationTag.creator) { %>
    <p>
        <span class="adminui-label">${ui.message('general.createdBy')}:</span> ${ ui.format(locationTag.creator) }
        <label class="adminui-label">${ui.message('general.onDate')}</label> ${ ui.format(locationTag.dateCreated) }
    </p>
    <% } %>
    <% if(locationTag.changedBy) { %>
    <p>
        <span class="adminui-label">${ui.message('general.changedBy')}:</span> ${ ui.format(locationTag.changedBy) }
        <label class="adminui-label">${ui.message('general.onDate')}</label> ${ ui.format(locationTag.dateChanged) }
    </p>
    <% } %>
</fieldset>
<% } %>

<form class="simple-form-ui" id="locationTagForm" method="post">

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("general.name")+"<span class='adminui-text-red'>*</span>",
            formFieldName: "name",
            id           : "name",
            maxLength    : 101,
            initialValue : ui.encodeHtmlAttribute((locationTag.name ?: ''))
    ])}

    ${ui.includeFragment("uicommons", "field/textarea", [
            label        : ui.message("general.description"),
            formFieldName: "description",
            id           : "description",
            initialValue : ui.encodeHtmlContent((locationTag.description ?: '')),
            cols         : 54
    ])}

    <p>
        <input type="button" class="cancel" value="${ui.message("general.cancel")}" onclick="window.location='/${ contextPath }/adminui/metadata/locations/manageLocationTags.page'"/>
        <input type="submit" class="confirm" name="save" id="save-button" value="${ui.message("general.save")}"/>
    </p>

</form>