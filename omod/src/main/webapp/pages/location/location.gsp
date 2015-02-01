<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("adminui", "jquery.validate.js")

    def createLocation = (location.locationId == null ? true : false);

    def parentLocationOptions = []
    existingLocations.each {
        parentLocationOptions.push([ label: ui.format(it), value: it.id ])
    }
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.locationManager.label")}" , link: '${ui.pageLink("adminui", "location/manageLocations")}'},
        { label: "${ ui.message("adminui.createLocation.locationManagement.label")}" }

    ];
</script>

<script type="text/javascript">

    jq().ready(function () {

        jq("#locationForm").validate({
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

<h1>
    <h3>${ (createLocation) ? ui.message("adminui.createLocation") : ui.message("adminui.editLocation") }</h3>
</h1>

<form class="simple-form-ui" method="post" id="locationForm" autocomplete="off">
<fieldset>
    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.name")+"*",
            formFieldName: "name",
            id           : "name",
            maxLength    : 101,
            initialValue : (location.name ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/textarea", [
            label        : ui.message("adminui.location.description"),
            formFieldName: "description",
            id           : "description",
            initialValue : (location.description ?: '')
    ])}

    <p>
	${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.address1"),
            formFieldName: "address1",
            id           : "address1",
            initialValue : (location.address1 ?: '')
    ])}    

	${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.address2"),
            formFieldName: "address2",
            id           : "address2",
            initialValue : (location.address2 ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.city_village"),
            formFieldName: "cityVillage",
            id           : "cityVillage",
            initialValue : (location.cityVillage ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.state_province"),
            formFieldName: "stateProvince",
            id           : "stateProvince",
            initialValue : (location.stateProvince ?: '')
    ])}


    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.country"),
            formFieldName: "country",
            id           : "country",
            initialValue : (location.country ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.postalCode"),
            formFieldName: "postalCode",
            id           : "postalCode",
            initialValue : (location.postalCode ?: '')
    ])}
    </p>

    ${ ui.includeFragment("uicommons", "field/dropDown", [
            label: ui.message("adminui.location.parentLocation"), 
            emptyOptionLabel: ui.message("adminui.chooseOne"), 
            formFieldName: "parentLocation", 
            options: parentLocationOptions,
            initialValue : (location.parentLocation?.id  ?: '')
    ])}

    <% attributeTypes.each{ %>
	${ ui.includeFragment("uicommons", "field/text", [ 
	       	label: ui.format(it),
	        formFieldName: "attribute."+it.id+"", 
		    value: it.id, 
		    checked: false
    ])}
    <% } %>

    <% locationTags.each{ %>
        ${ ui.includeFragment("uicommons", "field/checkbox", [
            label: ui.format(it),
            formFieldName: "locTags", 
            value: it, 
            checked: location.tags?.contains(it)
        ])}

    <% } %>

    <div>
        <input type="button" class="cancel" value="${ui.message("adminui.cancel")}" onclick="javascript:window.location='/${ contextPath }/adminui/location/manageLocations.page'"/>
        <input type="submit" class="confirm" name="save" id="save-button" value="${ui.message("adminui.save")}"/>
    </div>
    </fieldset>

    <% if(!createLocation) { %>
            <div>
                <fieldset>
                    ${ui.includeFragment("uicommons", "field/text", [
                        label        : ui.message("adminui.location.retireReason"),
                        formFieldName: "retireReason",
                        id           : "retireReason",
                        initialValue : (location.retireReason ?: '')
                    ])}
                    
                    <div>
                    <input type="submit" class="button" name="retire" id="retire-button" value="${ui.message("adminui.location.retire")}"/>
                </fieldset>
            </div>
    <% } %>

</form> 
