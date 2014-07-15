<%
    ui.decorateWith("appui", "standardEmrPage")

    def createLocation = (location.locationId == null ? true : false);

    def parentLocationOptions = []
    existingLocations. each {
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

<h1>
    <h3>${ (createLocation) ? ui.message("adminui.createLocation") : ui.message("adminui.editLocation") }</h3>
</h1>

<form class="simple-form-ui" method="post" id="locationForm" autocomplete="off">

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.name"),
            formFieldName: "name",
            id           : "name",
            maxLength    : 101,
            initialValue : (location.name ?: '')
    ])}

    ${ui.includeFragment("adminui", "field/textarea", [
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
            initialValue : (location.name ?: '')
    ])}    

	${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.address2"),
            formFieldName: "address2",
            id           : "address2",
            initialValue : (location.name ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.city_village"),
            formFieldName: "cityVillage",
            id           : "cityVillage",
            initialValue : (location.name ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.state_province"),
            formFieldName: "stateProvince",
            id           : "stateProvince",
            initialValue : (location.name ?: '')
    ])}


    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.country"),
            formFieldName: "country",
            id           : "country",
            initialValue : (location.name ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.location.postalCode"),
            formFieldName: "postalCode",
            id           : "postalCode",
            initialValue : (location.name ?: '')
    ])}
    </p>

    ${ ui.includeFragment("uicommons", "field/dropDown", [
            label: ui.message("adminui.location.parentLocation"), 
            emptyOptionLabel: ui.message("adminui.chooseOne"), 
            formFieldName: "parentLocation", 
            options: parentLocationOptions,
    ])}

    <% attributeTypes.each{ %>
	${ ui.includeFragment("uicommons", "field/text", [ 
	       	label: ui.format(it),
	        formFieldName: "attribute."+it.id+"", 
		    value: it.id, 
		    checked: false
    ])}
    <% } %>



    <div>
        <input type="button" class="cancel" value="${ui.message("adminui.cancel")}" onclick="javascript:window.location='/${ contextPath }/adminui/location/manageLocations.page'"/>
        <input type="submit" class="confirm" id="save-button" value="${ui.message("adminui.save")}"/>
    </div>

</form> 
