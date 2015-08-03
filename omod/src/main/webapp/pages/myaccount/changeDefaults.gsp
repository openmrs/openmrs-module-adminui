<%
    ui.decorateWith("appui", "standardEmrPage", ["title": "Change User Defaults"])
    ui.includeJavascript("uicommons", "navigator/validators.js", Integer.MAX_VALUE - 19)
    ui.includeJavascript("uicommons", "navigator/navigator.js", Integer.MAX_VALUE - 20)
    ui.includeJavascript("uicommons", "navigator/navigatorHandlers.js", Integer.MAX_VALUE - 21)
    ui.includeJavascript("uicommons", "navigator/navigatorModels.js", Integer.MAX_VALUE - 21)
    ui.includeJavascript("uicommons", "navigator/exitHandlers.js", Integer.MAX_VALUE - 22);

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
        {label: "${ ui.message("adminui.myAccount.defaults.label")}"}

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
            ${ui.includeFragment("uicommons", "field/text", [
                    id           : "proficient-locales",
                    label        : ui.message("adminui.account.proficientLocales"),
                    formFieldName: "proficientLocales",
                    initialValue : userDefaults.proficientLocales
            ])} ${ui.message('adminui.example')}: "en_US, en_GB, en, fr_RW"
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