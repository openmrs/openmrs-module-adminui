<%
    ui.decorateWith("appui", "standardEmrPage", ["title": "Change User Defaults"])
    ui.includeJavascript("uicommons", "navigator/validators.js", Integer.MAX_VALUE - 19)
    ui.includeJavascript("uicommons", "navigator/navigator.js", Integer.MAX_VALUE - 20)
    ui.includeJavascript("uicommons", "navigator/navigatorHandlers.js", Integer.MAX_VALUE - 21)
    ui.includeJavascript("uicommons", "navigator/navigatorModels.js", Integer.MAX_VALUE - 21)
    ui.includeJavascript("uicommons", "navigator/exitHandlers.js", Integer.MAX_VALUE - 22);
    
     ui.includeCss("adminui", "systemadmin/account.css")

    def localeOptions = []
    locales.each {
        localeOptions.add([label: it.displayName, value: it.toString()])
    }
%>

${ui.includeFragment("uicommons", "validationMessages")}
<script type="text/javascript">

    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {
            label: "${ ui.message("adminui.app.myAccount.label")}",
            link: '${ui.pageLink("adminui", "myaccount/myAccount")}'
        },
        {label: "${ ui.message("adminui.myAccount.defaultSettings.label")}"}

    ];

    jQuery(function () {
        KeyboardController();
    });
</script>

<form class="simple-form-ui" method="post">
    <section id="user-defaults">
        <span class="title">${ui.message("adminui.account.userDefaults")}</span>
        <fieldset>
            <legend>${ui.message("adminui.account.userDefaults")}</legend>
            ${ui.includeFragment("uicommons", "field/dropDown", [
                    id           : "default-locale",
                    label        : ui.message("adminui.account.defaultLocale"),
                    formFieldName: "defaultLocale",
                    options      : localeOptions,
                    initialValue : userDefaults.defaultLocale
            ])}

			<br/>
			<p>
            	<label>${ui.message("adminui.account.proficientLocales")}</label>
            
	            <table class="adminui-capabilities" cellspacing="0" cellpadding="0">
					<tr>
			    	<% locales.eachWithIndex { locale, index -> %>
						<td>
					        ${ ui.includeFragment("uicommons", "field/checkbox", [
					            label: locale.displayName,
					            formFieldName: "proficientLocales",
					            value: locale.toString(),
					            checked: userDefaults.proficientLocales.contains(locale.toString()),
					            valueIsNotBoolean: true
					        ])}
				        </td>
				        <% if ((index + 1) % 2 == 0) { %> </tr> <tr> <% } %>
			    	<% } %>
			    	</tr>
				</table>
			</p>
			
        </fieldset>
    </section>

    <div id="confirmation">
        <span class="title">${ui.message("adminui.confirm")}</span>

        <div id="confirmationQuestion">
            ${ui.message('adminui.confirmSubmission')}
            <p style="display: inline">
                <input type="submit" class="confirm right" value="${ui.message("adminui.confirm")}"/>
            </p>

            <p style="display: inline">
                <input id="cancelSubmission" class="cancel" type="button" value="${ui.message("general.cancel")}"/>
            </p>
        </div>
    </div>
</form>