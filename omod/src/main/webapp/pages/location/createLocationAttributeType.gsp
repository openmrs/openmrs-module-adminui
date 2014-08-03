<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("adminui", "jquery.validate.js")

    def createLocationAttributeType = (locationAttributeType.locationAttributeTypeId == null ? true : false);

    def datatypeOptions = []
    datatypes. each {
        datatypeOptions.push([ label: ui.format(it), value: it ])
    }

    def handlerOptions = []
    handlers. each {
        handlerOptions.push([ label: ui.format(it), value: it])
    }
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.locationManager.label")}" , link: '${ui.pageLink("adminui", "location/manageLocations")}'},
        { label: "${ ui.message("adminui.createLocationAttributeType.locationManagement.label")}" }

    ];
</script>

<script type="text/javascript">

    jq().ready(function () {

        jq("#locationAttributeTypeForm").validate({
            rules: {
                "name": {
                    required: true,
                    minlength: 3,
                    maxlength: 255
                },
                "description": {
                    required: false,
                    maxlength: 1024
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

<h1>
    <h3>${ (createLocationAttributeType) ? ui.message("adminui.createLocationAttributeType") : ui.message("adminui.editLocationAttributeType") }</h3>
</h1>

<form class="simple-form-ui" method="post" id="locationAttributeTypeForm" autocomplete="off">
<fieldset>
    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.locationAttributeType.name")+"*",
            formFieldName: "name",
            id           : "name",
            maxLength    : 101,
            initialValue : (locationAttributeType.name ?: '')
    ])}

    ${ui.includeFragment("adminui", "field/textarea", [
            label        : ui.message("adminui.locationAttributeType.description"),
            formFieldName: "description",
            id           : "description",
            initialValue : (locationAttributeType.description ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.locationAttributeType.minOccurs")+"*",
            formFieldName: "minOccurs",
            id           : "minOccurs",
            maxLength    : 101,
            initialValue : (locationAttributeType.minOccurs ?: '0')
    ])}

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.locationAttributeType.maxOccurs"),
            formFieldName: "maxOccurs",
            id           : "maxOccurs",
            maxLength    : 101,
            initialValue : (locationAttributeType.maxOccurs ?: '')
    ])}

    ${ ui.includeFragment("uicommons", "field/dropDown", [
            label: ui.message("adminui.locationAttributeType.datatype"), 
            emptyOptionLabel: ui.message("adminui.chooseOne"), 
            formFieldName: "datatypeClassname", 
            options: datatypeOptions,
            initialValue : (locationAttributeType.datatypeClassname ?: '')
    ])}

    ${ui.includeFragment("adminui", "field/textarea", [
            label        : ui.message("adminui.locationAttributeType.datatypeConfig"),
            formFieldName: "datatypeConfig",
            id           : "datatypeConfig",
            initialValue : (locationAttributeType.datatypeConfig ?: '')
    ])}

    ${ ui.includeFragment("uicommons", "field/dropDown", [
            label: ui.message("adminui.locationAttributeType.preferredHandler"), 
            emptyOptionLabel: ui.message("adminui.chooseOne"), 
            formFieldName: "preferredHandlerClassname", 
            options: handlerOptions,
            initialValue : (locationAttributeType.preferredHandlerClassname ?: '')
    ])}

    ${ui.includeFragment("adminui", "field/textarea", [
            label        : ui.message("adminui.locationAttributeType.handlerConfig"),
            formFieldName: "handlerConfig",
            id           : "handlerConfig",
            initialValue : (locationAttributeType.handlerConfig ?: '')
    ])}

    <div>
        <input type="button" class="cancel" value="${ui.message("adminui.cancel")}" onclick="javascript:window.location='/${ contextPath }/adminui/location/manageLocationAttributeTypes.page'"/>
        <input type="submit" class="confirm" name="save" id="save-button" value="${ui.message("adminui.save")}"/>
    </div>
    </fieldset>

    <% if(!createLocationAttributeType) { %>
            <div>
                <fieldset>
                    ${ui.includeFragment("uicommons", "field/text", [
                        label        : ui.message("adminui.locationAttributeType.retireReason"),
                        formFieldName: "retireReason",
                        id           : "retireReason",
                        initialValue : (locationAttributeType.retireReason ?: '')
                    ])}
                    
                    <div>
                    <input type="submit" class="button" name="retire" id="retire-button" value="${ui.message("adminui.locationAttributeType.retire")}"/>
                </fieldset>

                <fieldset>
                    
                    <div>
                    <input type="submit" class="button" name="purge" id="purge-button" value="${ui.message("adminui.locationAttributeType.purge")}"/>
                </fieldset>

            </div>
    <% } %>

</form>