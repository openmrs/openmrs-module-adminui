<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("adminui.changePassword") ])

    ui.includeJavascript("uicommons", "navigator/validators.js", Integer.MAX_VALUE - 19)
    ui.includeJavascript("uicommons", "navigator/navigator.js", Integer.MAX_VALUE - 20)
    ui.includeJavascript("uicommons", "navigator/navigatorHandlers.js", Integer.MAX_VALUE - 21)
    ui.includeJavascript("uicommons", "navigator/navigatorModels.js", Integer.MAX_VALUE - 21)
    ui.includeJavascript("uicommons", "navigator/exitHandlers.js", Integer.MAX_VALUE - 22);
    ui.includeJavascript("adminui", "myaccount/changePassword.js")

%>

${ ui.includeFragment("uicommons", "validationMessages")}

<script type="text/javascript">
    //This variable is defined in changePassword.js
    passwordMinLength = ${ passwordMinLength };

    //emrMessages is in uicommons's validationMessages.gsp
    emrMessages.minLength = '${ui.message("adminui.account.changePassword.password.short", passwordMinLength)}';
    emrMessages.matchedInput = '${ui.message("adminui.account.changePassword.newAndConfirmPassword.should.match", passwordMinLength)}';

    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.myAccount.label")}", link: '${ui.pageLink("adminui", "myaccount/myAccount")}' },
        { label: "${ ui.message("adminui.myAccount.changePassword.label")}" }

    ];

    jQuery(function(){
        KeyboardController();
    });

</script>

<h3>${ui.message("adminui.myAccount.changePassword.label")}</h3>

<form class="simple-form-ui" method="post">
    <section id="passwordDetails">
        <span class="title">${ui.message("adminui.account.password.details")}</span>
        <fieldset>
            <legend>${ ui.message("adminui.account.oldAndNewPassword") }</legend>
            ${ ui.includeFragment("uicommons", "field/passwordField", [
                    id: "oldPassword",
                    label: ui.message("adminui.account.oldPassword"),
                    formFieldName: "oldPassword",
                    classes: ["required"]
            ]) }

            ${ ui.includeFragment("uicommons", "field/passwordField", [
                    id: "newPassword",
                    label: ui.message("adminui.account.newPassword"),
                    formFieldName: "newPassword",
                    classes: ["required", "min-length", "matched-input"],
                    "matched-field-id": "confirmPassword",
                    regex: /^.{${passwordMinLength},}$/
            ]) }

            ${ ui.includeFragment("uicommons", "field/passwordField", [
                    id: "confirmPassword",
                    label: ui.message("adminui.account.confirmPassword"),
                    formFieldName: "confirmPassword",
                    classes: ["required", "matched-input"],
                    "matched-field-id": "newPassword"
            ]) }
        </fieldset>

    </section>

    <div id="confirmation">
        <span class="title">${ui.message("adminui.confirm")}</span>
        <div id="confirmationQuestion">
            ${ui.message('adminui.confirmSubmission')}
            <p style="display: inline">
                <input type="submit" class="confirm right" value="${ui.message("adminui.confirm")}" />
            </p>
            <p style="display: inline">
                <input id="cancelSubmission" class="cancel" type="button" value="${ui.message("general.cancel")}" />
            </p>
        </div>
    </div>

</form>
