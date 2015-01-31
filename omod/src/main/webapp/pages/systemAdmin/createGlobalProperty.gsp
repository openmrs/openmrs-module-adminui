<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("adminui", "jquery.validate.js")

    def createGlobalProperty = (globalProperty == null ? true : false);

    if(globalProperty != null) {
        initialProperty = globalProperty.property;
        initialPropertyValue = globalProperty.propertyValue;
        initialDescription = globalProperty.description;
    }
    else {
        initialProperty = '';
        initialPropertyValue = '';
        initialDescription = '';
    }

%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.administrationTools.label")}" , link: '${ui.pageLink("adminui", "adminUiHome")}'},
        { label: "${ ui.message("adminui.app.systemAdministration.label")}" , link: '${ui.pageLink("adminui", "systemAdmin/home")}'},
        { label: "${ ui.message("adminui.createGlobalProperty.systemAdministration.label")}" }

    ];
</script>

<script type="text/javascript">

    jq().ready(function () {

        jq("#GlobalPropertyForm").validate({
            rules: {
                "property": {
                    required: true,
                    minlength: 3,
                    maxlength: 255
                },
                "propertyValue": {
                    required: true,
                    minlength: 3,
                    maxlength: 255
                },
                "description": {
                    required: false,
                    maxlength: 1024
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
    <h3>${ (createGlobalProperty) ? ui.message("adminui.createGlobalProperty") : ui.message("adminui.editGlobalProperty") }</h3>
</h1>

<form class="simple-form-ui" method="post" id="GlobalPropertyForm" autocomplete="off">
<fieldset>

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.globalProp.name")+"*",
            formFieldName: "property",
            id           : "property",
            maxLength    : 101,
            initialValue : initialProperty
    ])}

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.globalProp.value")+"*",
            formFieldName: "propertyValue",
            id           : "propertyValue",
            maxLength    : 101,
            initialValue : initialPropertyValue
    ])}

    ${ui.includeFragment("adminui", "field/textarea", [
            label        : ui.message("adminui.globalProp.description"),
            formFieldName: "description",
            id           : "description",
            initialValue : initialDescription
    ])}

    <div>
        <input type="button" class="cancel" value="${ui.message("adminui.cancel")}" onclick="javascript:window.location='/${ contextPath }/adminui/systemAdmin/globalProps.page'"/>
        <input type="submit" class="confirm" name="save" id="save-button" value="${ui.message("adminui.save")}"/>
    </div>
    </fieldset>

    <% if(!createGlobalProperty) { %>
            <div>
                <fieldset>
                    <div>
                    <input type="submit" class="button" name="purge" id="purge-button" value="${ui.message("adminui.purgeGlobalProperty")}"/>
                </fieldset>
            </div>
    <% } %>

</form>