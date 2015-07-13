<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("adminui", "jquery.validate.js")

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

<h2>${ ui.message((createLocationTag) ? "adminui.addLocationTag.label" : "adminui.editLocationTag.label") }</h2>


<form class="simple-form-ui" method="post" id="locationTagForm" autocomplete="off">
<fieldset>
    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("general.name")+"*",
            formFieldName: "name",
            id           : "name",
            maxLength    : 101,
            initialValue : (locationTag.name ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/textarea", [
            label        : ui.message("general.description"),
            formFieldName: "description",
            id           : "description",
            initialValue : (locationTag.description ?: '')
    ])}

    <div>
        <input type="button" class="cancel" value="${ui.message("general.cancel")}" onclick="window.location='/${ contextPath }/adminui/metadata/locations/manageLocationTags.page'"/>
        <input type="submit" class="confirm" name="save" id="save-button" value="${ui.message("general.save")}"/>
    </div>
    </fieldset>

    <% if(!createLocationTag) { %>
            <div>
                <fieldset>
                    ${ui.includeFragment("uicommons", "field/text", [
                        label        : ui.message("general.retireReason"),
                        formFieldName: "retireReason",
                        id           : "retireReason",
                        initialValue : (locationTag.retireReason ?: '')
                    ])}
                    <input type="submit" class="button" name="retire" id="retire-button" value="${ui.message("adminui.locationTag.retire")}"/>
                </fieldset>
            </div>
    <% } %>

</form>