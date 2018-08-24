<%
    ui.decorateWith("appui", "standardEmrPage", ["title": ui.message("adminui.myAccount.myLanguages.title")])
    
    ui.includeCss("adminui", "adminui.css")
    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("adminui","myaccount/changeLanguage.js")
    
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
        {label: "${ ui.message("adminui.myAccount.myLanguages.label")}"}

    ];
   
</script>

<h2>${ui.message("adminui.myAccount.myLanguages.label")}</h2>
<div ng-app="changeLanguage" ng-controller="changeLanguageController">
<form class="simple-form-ui" method="post" name="selectLanguageForm" >

    ${ui.includeFragment("uicommons", "field/dropDown", [
        id           : "default-locale",
        label        : ui.message("adminui.account.defaultLocale"),
        formFieldName: "defaultLocale",
        options      : primaryLocaleOptions,
        initialValue : userDefaults.defaultLocale
    ])}

    <label title="${ui.message("adminui.account.proficientLang.desc")}">${ui.message("adminui.account.proficientLocales")}</label>
    <div class="adminui-section-padded-top">
    <% proficientLocales.each { %>
        <input type="checkbox" name="proficientLocales" value="${it.toString()}"
            <% if (userDefaults.proficientLocales && userDefaults.proficientLocales.contains(it.toString())) { %> checked='checked'<% } %>>
            ${it.displayName} <br />
    <% } %>
    </div>

    <div class="adminui-section-padded-top">
        <input type="submit" class="confirm right" value="${ui.message("general.save")}"
        		onclick="document.selectLanguageForm.submit()"/>
        <input type="button" id="cancel-button" class="cancel" value="${ui.message("general.cancel")}"
               onclick="window.location='${ui.pageLink("adminui", "myaccount/myAccount")}'" />
    </div>

</form>
</div>