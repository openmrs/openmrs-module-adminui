<%
    ui.decorateWith("appui", "standardEmrPage", ["title": "Change User Defaults"])
    
    ui.includeCss("adminui", "adminui.css")

    def primaryLocaleOptions = []
    primaryLocales.each {
        primaryLocaleOptions.add([label: it.displayName, value: it.toString()])
    }
%>

<script type="text/javascript">

    var breadcrumbs = [
        {icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm'},
        {
            label: "${ ui.message("adminui.app.myAccount.label")}",
            link: '${ui.pageLink("adminui", "myaccount/myAccount")}'
        },
        {label: "${ ui.message("adminui.myAccount.defaultSettings.label")}"}

    ];
</script>

<h2>${ui.message("adminui.myAccount.defaultSettings.label")}</h2>

<form class="simple-form-ui" method="post">

    ${ui.includeFragment("uicommons", "field/dropDown", [
        id           : "default-locale",
        label        : ui.message("adminui.account.defaultLocale"),
        formFieldName: "defaultLocale",
        options      : primaryLocaleOptions,
        initialValue : userDefaults.defaultLocale
    ])}

    <label>${ui.message("adminui.account.proficientLocales")}</label>
    <div class="adminui-section-padded-top">
    <% proficientLocales.each { %>
        <input type="checkbox" name="proficientLocales" value="${it.toString()}"
            <% if (userDefaults.proficientLocales && userDefaults.proficientLocales.contains(it.toString())) { %> checked='checked'<% } %>>
            ${it.displayName} <br />
    <% } %>
    </div>

    <div class="adminui-section-padded-top">
        <input type="submit" class="confirm right" value="${ui.message("general.save")}"/>
        <input type="button" id="cancel-button" class="cancel" value="${ui.message("general.cancel")}"
               onclick="window.location='${ui.pageLink("adminui", "myaccount/myAccount")}'" />
    </div>

</form>