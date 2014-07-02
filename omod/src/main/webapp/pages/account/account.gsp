<%
    ui.decorateWith("appui", "standardEmrPage")

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
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.accountManager.label")}" , link: '${ui.pageLink("adminui", "account/manageAccounts")}'},
        { label: "${ ui.message("adminui.createAccount.accountManagement.label")}" }

    ];
</script>



<script type="text/javascript">

var tabs = 1;

function addTab() {

    ++tabs;
    jq('#countTabs').val(parseInt(jq('#countTabs').val(),10)+1);
    
    // hide other tabs
    jq("#tabs li").removeClass("current ui-tabs-active ui-state-active");
    jq("#tabContent div").hide();

    // add new tab and related tabContent
    jq("#tabs").append("<li class='current ui-tabs-active ui-state-active'><a class='tab' id='user"+tabs+"' href='#ui-tabs'>User "+(tabs)+"</a><a href='#ui-tabs' class='remove'>x</a></li>");
    
    jq("#tabContent").append("<div id='user"+tabs+"_tabContent'>"+
        
            "<p><label>Username</label> <input type='text' id='user"+tabs+"_username' name='user"+tabs+"_username'></p>" +
            "<p><label>Password</label> <input type='password' id='user"+tabs+"_password' name='user"+tabs+"_password'></p>" +
            "<p><label>Confirm Password</label> <input type='password' id='user"+tabs+"_confirmPassword' name='user"+tabs+"_confirmPassword'></p>" +
            "<p><label>Privilege Level</label> <select name='user"+tabs+"_privilegeLevel'>" + <% privilegeLevels.each{ %> "<option value='$it'>$it</option>" + <% } %> "</select></p>" +
            "<p><label>Roles</label>" + <% capabilities.each{ %> "<p><input type='checkbox' name='user"+tabs+"_capabilities' value='$it'>$it</p>" + <% } %> "</p>" +
               
            "</div>");
    
    // set the newly added tab as current
    jq("#user"+tabs+"_tabContent").show();

    // focus on new tabs's username field
    jq("#user"+tabs+"_username").focus();
    
}

jq(document).ready(function() {

    jq('#tabs a.tab').live('click', function() {
        // Get the tab name
        var tabContentname = \$(this).attr("id") + "_tabContent";
   
        // hide all other tabs
        jq("#tabContent div").hide();
        jq("#tabs li").removeClass("current ui-tabs-active ui-state-active");
    
        // show current tab
        jq("#" + tabContentname).show();
        jq(this).parent().addClass("current ui-tabs-active ui-state-active")
    });



    jq('#tabs a.remove').live('click', function() {
    
        --tabs;
        jq("#countTabs").val(tabs);

        // Get the tab name
        var tabid = \$(this).parent().find(".tab").attr("id");
    
        // remove tab and related tabContent
        var tabContentname = tabid + "_tabContent";
    
        jq("#" + tabContentname).remove();
        jq(this).parent().remove();

        // if there is no current tab and if there are still tabs left, show the first one
        if (\$("#tabs li.ui-state-active").length == 0 && \$("#tabs li").length > 0) {
            // find the first tab
            var firsttab = \$("#tabs li:first-child");
            firsttab.addClass("current ui-tabs-active ui-state-active");
    
            // get its link name and show related tabContent
            var firsttabid = \$(firsttab).find("a.tab").attr("id");
            \$("#" + firsttabid + "_tabContent").show();
        } 
    });


    // change the tab's name to its corresponding username
    jq('[id\$=_username]').live('change', function(){     
        var fieldID = this.id;
        var tabID = fieldID.replace(/_username/,'');
        jq("#"+tabID).text(\$("#"+fieldID).val());
    });

});

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
		<br>
			
			
			<div>
                <a class="button task" href="#ui-tabs" onclick="addTab()">
                <i class="icon-plus"></i>
                    Add User
                </a>

                <br><br>

                <input type="hidden" id="countTabs" name="countTabs" value="1">

                <div class="ui-tabs">
                    <ul id="tabs" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" role="tablist">
            
                        <li class="ui-state-default ui-corner-top ui-tabs-active ui-state-active">
                            <a class="tab" href="#ui-tabs" id="user1">
                                User 1
                            </a>
                        </li>
        
                    </ul>
    
                    <div id="tabContent" class="ui-tabs-panel ui-widget-tabContent ui-corner-bottom">
                        <div id="user1_tabContent">

                            <p><label>Username</label> 
                                <input type='text' id='user1_username' name='user1_username'>
                            </p> 
                            
                            <p><label>Password</label> 
                                <input type='password' id='user"1_password' name='user1_password'>
                            </p>

                            <p><label>Confirm Password</label> 
                                <input type='password' id='user1_confirmPassword' name='user1_confirmPassword'>
                            </p>

                            <p><label>Privilege Level</label>
                                <select name='user1_privilegeLevel'>
                                <% privilegeLevels.each{ %>
                                    <option value="$it">$it</option>
                                <% } %>
                                </select>
                            </p>

                            <p><label>Roles</label>
                                <% capabilities.each{ %>
                                    <p>
                                        <input type='checkbox' name='user1_capabilities' value="$it">$it
                                    </p>
                                <% } %>
                            </p>

                        </div>
                    </div>

                </div>

            </div>

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
                 
            ])}
            
		<div class="emr_providerDetails" <% if (1) { %> style="display: none" <% } %> >
            <br>
            <p>
                ${ ui.message("Provider Role (You can choose more than one)") }
            </p>
            
            <% providerRoles.each{ %>
                ${ ui.includeFragment("adminui", "field/checkbox", [ 
                    label: ui.format(it),
                    formFieldName: "providerRoles", 
                    value: it.id, 
                    checked: account.providerSet?.contains(it)
                ])}
            <% } %>
            
        </div>

		<div class="emr_providerDetails">
			<% if(1) { %>
				<button id="createProviderAccountButton" type="button" onclick="javascript:emr_createProviderAccount()">${ ui.message("adminui.provider.createProviderAccount") }</button>
			<% } %>
		</div>
		

	</fieldset>

    <div id>
        <input type="button" class="cancel" value="${ ui.message("adminui.cancel") }" onclick="javascript:window.location='/${ contextPath }/adminui/account/manageAccounts.page'" />
        <input type="submit" class="confirm" id="save-button" value="${ ui.message("adminui.save") }"  />
    </div>

</form>
