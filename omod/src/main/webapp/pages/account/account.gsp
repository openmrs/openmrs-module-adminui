<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeCss("adminui", "account.css")
    ui.includeJavascript("adminui", "account/account.js")

    def createAccount = (account.person.personId == null ? true : false);

    def genderOptions = [ [label: ui.message("adminui.gender.M"), value: 'M'],
                          [label: ui.message("adminui.gender.F"), value: 'F'] ]

    def privilegeLevelOptions = []
    privilegeLevels.each {
        privilegeLevelOptions.push([ label: ui.format(it), value: it.name ])
    }

    def providerRolesOptions = []
    providerRoles. each {
        providerRolesOptions.push([ label: ui.format(it), value: it.id ])
    }

%>

<script type="text/javascript">
    if(createAccount)
    	document.accountForm.createUserAccountButton.click();
</script>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.accountManager.label")}" , link: '${ui.pageLink("adminui", "account/manageAccounts")}'},
        { label: "${ ui.message("adminui.createAccount.accountManagement.label")}" }

    ];
</script>

<style type="text/css">
    #unlock-button {
        margin-top: 1em;
    }
</style>



<h3>${ (createAccount) ? ui.message("adminui.createAccount") : ui.message("adminui.editAccount") }</h3>

<form method="post" id="accountForm">
	<fieldset>
		<legend><b>${ ui.message("adminui.person.details") }</b></legend>

        ${ ui.includeFragment("uicommons", "field/text", [
            label: ui.message("adminui.person.familyName"),
            formFieldName: "familyName", 
            initialValue: (account.familyName ?: '')
        ])}

        ${ ui.includeFragment("uicommons", "field/text", [
                label: ui.message("adminui.person.givenName"),
                formFieldName: "givenName",
                initialValue: (account.givenName ?: '')
        ])}

        ${ ui.includeFragment("uicommons", "field/radioButtons", [
            label: ui.message("adminui.gender"),
            formFieldName: "gender", 
            initialValue: (account.gender ?: 'M'), 
            options: genderOptions 
        ])}
	</fieldset>
	
	<fieldset>
		<legend><b>${ ui.message("adminui.user.account.details") }</b></legend>
		
		${ ui.includeFragment("adminui", "field/checkbox", [ 
                label: ui.message("adminui.user.enabled"), 
                id: "userEnabled", 
                formFieldName: "userEnabled",
                value: "true", 
                checked: (account.user ?: '') 
            ])}
		
		<div class="emr_userDetails" <% if (!account.user) { %> style="display: none" <% } %>>
			${ ui.includeFragment("uicommons", "field/text", [
                label: ui.message("adminui.user.username"), 
                formFieldName: "username", 
                initialValue: (account.username ?: '') 
            ])}

            <% if (!account.password && !account.confirmPassword) { %>
                <button class="emr_passwordDetails emr_userDetails" type="button" onclick="javascript:jQuery('.emr_passwordDetails').toggle()">${ ui.message("adminui.user.changeUserPassword") }</button>
                <p></p>
            <% } %>

            <p class="emr_passwordDetails" <% if(!account.password && !account.confirmPassword) { %>style="display: none"<% } %>>
                <label class="form-header" for="password">${ ui.message("adminui.user.password") }</label>
                <input type="password" id="password" name="password" value="${ account.password ?: ''}" autocomplete="off"/>
                <label id="format-password">${ ui.message("adminui.account.passwordFormat") }</label>
                ${ ui.includeFragment("uicommons", "fieldErrors", [ fieldName: "password" ])}
            </p>

            <p class="emr_passwordDetails" <% if(!account.password && !account.confirmPassword) { %>style="display: none"<% } %>>
                <label class="form-header" for="confirmPassword">${ ui.message("adminui.user.confirmPassword") }</label>
                <input type="password" id="confirmPassword" name="confirmPassword" value="${ account.confirmPassword ?: '' }" autocomplete="off" />
                ${ ui.includeFragment("uicommons", "fieldErrors", [ fieldName: "confirmPassword" ])}
            </p>

            ${ ui.includeFragment("uicommons", "field/dropDown", [
                label: ui.message("adminui.user.privilegeLevel"), 
                emptyOptionLabel: ui.message("adminui.chooseOne"), 
                formFieldName: "privilegeLevel", 
                initialValue: (account.privilegeLevel ? account.privilegeLevel.getName() : ''), 
                options: privilegeLevelOptions
            ])}


            <p>
                <strong>${ ui.message("adminui.user.Capabilities") }</strong>
            </p>

			<% capabilities.each{ %>
                ${ ui.includeFragment("adminui", "field/checkbox", [ 
                    label: ui.format(it),
                    formFieldName: "capabilities", 
                    value: it.name, 
                    checked: account.capabilities?.contains(it) 
                ])}
            <% } %>
		</div>
		<div class="emr_userDetails">
			<% if(!account.user) { %>
				<button id="createUserAccountButton" type="button" onclick="javascript:emr_createUserAccount()"> ${ ui.message("adminui.user.createUserAccount") }</button>
			<% } %>
		</div>
	</fieldset>
	
	<fieldset>
		<legend><b>${ ui.message("adminui.provider.details") }</b></legend>
		
		${ ui.includeFragment("adminui", "field/checkbox", [ 
                label: ui.message("adminui.provider.enabled"), 
                id: "providerEnabled", 
                formFieldName: "providerEnabled", 
                value: "true", 
                checked: (account.provider ?: '')  
            ])}
            
		<div class="emr_providerDetails" <% if (!account.provider) { %> style="display: none" <% } %> >
            <p>
                ${ ui.includeFragment("uicommons", "field/dropDown", [
                        label: ui.message("adminui.providerRole.label"),
                        emptyOptionLabel: ui.message("adminui.chooseOne"),
                        formFieldName: "providerRole",
                        initialValue: (account.providerRole?.id ?: ''),
                        options: providerRolesOptions
                ])}
            </p>
        </div>

		<div class="emr_providerDetails">
			<% if(!account.provider) { %>
				<button id="createProviderAccountButton" type="button" onclick="javascript:emr_createProviderAccount()">${ ui.message("adminui.provider.createProviderAccount") }</button>
			<% } %>
		</div>
		

	</fieldset>

    <div>
        <input type="button" class="cancel" value="${ ui.message("adminui.cancel") }" onclick="javascript:window.location='/${ contextPath }/adminui/account/manageAccounts.page'" />
        <input type="submit" class="confirm" id="save-button" value="${ ui.message("adminui.save") }"  />
    </div>

</form>
