<%
    ui.decorateWith("appui", "standardEmrPage", ["title": "Change Secret Question"])
    ui.includeJavascript("uicommons", "navigator/validators.js", Integer.MAX_VALUE - 19)
    ui.includeJavascript("uicommons", "navigator/navigator.js", Integer.MAX_VALUE - 20)
    ui.includeJavascript("uicommons", "navigator/navigatorHandlers.js", Integer.MAX_VALUE - 21)
    ui.includeJavascript("uicommons", "navigator/navigatorModels.js", Integer.MAX_VALUE - 21)
    ui.includeJavascript("uicommons", "navigator/exitHandlers.js", Integer.MAX_VALUE - 22);
%>

${ ui.includeFragment("uicommons", "validationMessages")}
<script type="text/javascript">

    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.myAccount.label")}", link: '${ui.pageLink("adminui", "myaccount/myAccount")}' },
        { label: "${ ui.message("adminui.myAccount.changeSecretQuestion.label")}" }

    ];

    jQuery(function(){
        KeyboardController();
    });
</script>

<form class="simple-form-ui" method="post">
    <section id="secret-question">
        <span class="title">${ui.message("adminui.account.secretQuestion")}</span>
        <fieldset>
            <legend>${ui.message("adminui.account.secretQuestionAndAnswer")}</legend>
            ${ ui.includeFragment("uicommons", "field/passwordField", [
                    id: "password",
                    label: ui.message("adminui.account.password"),
                    formFieldName: "password",
                    classes: ["required"]
            ]) }
            ${ ui.includeFragment("uicommons", "field/text", [
                    id: "secret-question",
                    label: ui.message("adminui.account.secretQuestion"),
                    formFieldName: "question",
                    classes: ["required"]
            ]) }
            ${ ui.includeFragment("uicommons", "field/text", [
                    id: "new-secret-answer",
                    label: ui.message("adminui.account.newSecretAnswer"),
                    formFieldName: "answer",
                    classes: ["required"]
            ]) }
            ${ ui.includeFragment("uicommons", "field/text", [
                    id: "confirm-secret-answer",
                    label: ui.message("adminui.account.confirmSecretAnswer"),
                    formFieldName: "confirmAnswer",
                    classes: ["required"]
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