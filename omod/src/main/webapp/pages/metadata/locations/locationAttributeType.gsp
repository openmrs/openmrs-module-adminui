<%
    context.requirePrivilege('Manage Location Attribute Types')
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("adminui", "jquery.validate.js")
    ui.includeCss("adminui", "metadata/locations/locationAttributeTypes.css")

    def createLocationAttributeType = (locationAttributeType.locationAttributeTypeId == null ? true : false);

    def datatypeOptions = []
    datatypesMap. each { k, v ->
        datatypeOptions.push([ label: v, value: k ])
    }

    def handlerOptions = []
    handlersMap. each { k, v ->
        handlerOptions.push([ label: v, value: k])
    }
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('adminui.app.configureMetadata.label')}" , link: '${ui.pageLink("adminui", "metadata/configureMetadata")}'},
        { label: "${ ui.message("adminui.manageLocationAttributeTypes.label")}", link: '${ui.pageLink("adminui","metadata/locations/manageLocationAttributeTypes")}'},
        { label: "${ ui.message((createLocationAttributeType) ? "adminui.addNewLocationAttributeType.label" : "adminui.editLocationAttributeType.label")}" }

    ];
</script>

<script type="text/javascript">

    jq().ready(function () {

        jq("#locationAttributeTypeForm").validate({
            rules: {
                "name": {
                    required: true,
                    maxlength: 255
                },
                "description": {
                    maxlength: 1024
                },
                "minOccurs": {
                    required: true
                },
                "datatypeClassname": {
                    required: true,
                }
            },
            errorClass: "error",
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

<h2>${ ui.message((createLocationAttributeType) ? "adminui.addNewLocationAttributeType.label" : "adminui.editLocationAttributeType.label") }</h2>

<% if(!createLocationAttributeType) { %>
<fieldset class="right adminui-auditInfo">
    <legend>${ui.message('adminui.auditInfo')}</legend>
    <p>
        <label class="adminui-label">${ui.message('general.uuid')}:</label> ${ locationAttributeType.uuid }
    </p>
    <% if(locationAttributeType.creator) { %>
    <p>
        <span class="adminui-label">${ui.message('general.createdBy')}:</span> ${ ui.format(locationAttributeType.creator) }
        <label class="adminui-label">${ui.message('general.onDate')}</label> ${ ui.format(locationAttributeType.dateCreated) }
    </p>
    <% } %>
    <% if(locationAttributeType.changedBy) { %>
    <p>
        <span class="adminui-label">${ui.message('general.changedBy')}:</span> ${ ui.format(locationAttributeType.changedBy) }
        <label class="adminui-label">${ui.message('general.onDate')}</label> ${ ui.format(locationAttributeType.dateChanged) }
    </p>
    <% } %>
</fieldset>
<% } %>

<form class="simple-form-ui" id="locationAttributeTypeForm" method="post">

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("general.name")+"<span class='adminui-text-red'>*</span>",
            formFieldName: "name",
            id           : "name",
            maxLength    : 101,
            initialValue : ui.encodeHtmlAttribute((locationAttributeType.name ?: ''))
    ])}

    ${ui.includeFragment("uicommons", "field/textarea", [
            label        : ui.message("general.description"),
            formFieldName: "description",
            id           : "description",

            initialValue : ui.encodeHtmlContent((locationAttributeType.description ?: '')),
            cols         : 54,
            rows: 3
    ])}
    <div style="width: 45%">
        <table id="adminui-occurences-table" cellpadding="0" cellspacing="0">
            <tr>
                <td valign="top">
                ${ui.includeFragment("uicommons", "field/text", [
                    label        : ui.message("adminui.minOccurs")+"<span class='adminui-text-red'>*</span>",
                    formFieldName: "minOccurs",
                    id           : "minOccurs",
                    maxLength    : 101,
                    initialValue : (locationAttributeType.minOccurs ?: '0')
                ])}
                </td>
                <td valign="top">
                ${ui.includeFragment("uicommons", "field/text", [
                    label        : ui.message("adminui.maxOccurs"),
                    formFieldName: "maxOccurs",
                    id           : "maxOccurs",
                    maxLength    : 101,
                    initialValue : (locationAttributeType.maxOccurs ?: '')
                ])}
                </td>
            </tr>
        </table>
    </div>
    ${ ui.includeFragment("uicommons", "field/dropDown", [
            label: ui.message("adminui.datatype")+"<span class='adminui-text-red'>*</span>",
            emptyOptionLabel: ui.message("adminui.chooseOne"),
            formFieldName: "datatypeClassname",
            id           : "datatypeClassname",
            options: datatypeOptions,
            initialValue : (locationAttributeType.datatypeClassname ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/textarea", [
            label        : ui.message("adminui.datatypeConfig"),
            formFieldName: "datatypeConfig",
            id           : "datatypeConfig",
            initialValue : (locationAttributeType.datatypeConfig ?: ''),
            cols         : 54,
            rows: 3
    ])}

    ${ ui.includeFragment("uicommons", "field/dropDown", [
            label: ui.message("adminui.preferredHandler"),
            emptyOptionLabel: ui.message("adminui.chooseOne"),
            formFieldName: "preferredHandlerClassname",
            id           : "preferredHandlerClassname",
            options: handlerOptions,
            initialValue : (locationAttributeType.preferredHandlerClassname ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/textarea", [
            label        : ui.message("adminui.handlerConfig"),
            formFieldName: "handlerConfig",
            id           : "handlerConfig",
            initialValue : ui.encodeHtmlContent((locationAttributeType.handlerConfig ?: '')),
            cols         : 54,
            rows: 3
    ])}

    <div>
        <input type="button" class="cancel" value="${ui.message("general.cancel")}" onclick="window.location='/${ contextPath }/adminui/metadata/locations/manageLocationAttributeTypes.page'"/>
        <input type="submit" class="confirm" name="save" id="save-button" value="${ui.message("general.save")}"/>
    </div>

</form>