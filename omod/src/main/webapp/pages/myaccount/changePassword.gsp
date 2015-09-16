<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("uicommons", "angular.min.js")
    ui.includeJavascript("uicommons", "angular-app.js")
    ui.includeJavascript("adminui", "directives/shouldMatch.js")

    ui.includeCss("adminui", "adminui.css")

%>

<script type="text/javascript">

    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.myAccount.label")}", link: '${ui.pageLink("adminui", "myaccount/myAccount")}' },
        { label: "${ ui.message("adminui.myAccount.changePassword.label")}" }

    ];

    angular.module("changePassword", ["adminui.should-match"]);

</script>

<h2>${ui.message("adminui.myAccount.changePassword.label")}</h2>

<div id="adminui-changePassword" ng-app="changePassword">

<form name="changePasswordForm" class="simple-form-ui" method="post" action="" novalidate>

    ${ ui.includeFragment("uicommons", "field/passwordField", [
            id: "oldPassword",
            label: ui.message("adminui.account.oldPassword")+"<span class='adminui-text-red'>*</span>",
            formFieldName: "oldPassword",
            otherAttributes: ["ng-model": "oldPassword", required:""]
    ]) }
    <span class="field-error" ng-show="changePasswordForm.oldPassword.\$dirty
            && changePasswordForm.oldPassword.\$invalid">
        <span ng-show="changePasswordForm.oldPassword.\$error.required">
            ${ui.message("adminui.field.required")}
        </span>
    </span>

    ${ ui.includeFragment("uicommons", "field/passwordField", [
            id: "newPassword",
            label: ui.message("adminui.account.newPassword")+"<span class='adminui-text-red'>*</span>",
            formFieldName: "newPassword",
            otherAttributes: ["ng-model": "newPassword", required:"", "ng-minlength": passwordMinLength]
    ]) }
    <span class="field-error" ng-show="changePasswordForm.newPassword.\$dirty
            && changePasswordForm.newPassword.\$invalid">
        <span ng-show="changePasswordForm.newPassword.\$error.required">
            ${ui.message("adminui.field.required")}
        </span>
        <span ng-show="changePasswordForm.newPassword.\$error.minlength">
            ${ui.message("adminui.field.require.minChars", passwordMinLength)}
        </span>
    </span>

    ${ ui.includeFragment("uicommons", "field/passwordField", [
            id: "confirmPassword",
            label: ui.message("User.confirm")+"<span class='adminui-text-red'>*</span>",
            formFieldName: "confirmPassword",
            otherAttributes: ["ng-model": "confirmPassword", "should-match": "newPassword"]
    ]) }

    <span class="field-error" ng-show="changePasswordForm.confirmPassword.\$dirty
            && changePasswordForm.confirmPassword.\$invalid">
        ${ui.message("adminui.account.changePassword.newAndConfirmPassword.dontMatch")}
    </span>

    <div class="adminui-section-padded-top">
        <input type="submit" class="confirm right" name="save" id="save-button" value="${ui.message("general.save")}"
               ng-disabled="changePasswordForm.\$invalid" />
        <input type="button" id="cancel-button" class="cancel" value="${ui.message("general.cancel")}"
               onclick="window.location='${ui.pageLink("adminui", "myaccount/myAccount")}'" />
    </div>

</form>

</div>
