<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("adminui.myaccount") ])

    ui.includeCss("adminui", "account.css")

    ui.includeJavascript("adminui", "account/changePassword.js")

%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.myAccount.label")}", link: '${ui.pageLink("adminui", "account/myAccount")}' },
        { label: "${ ui.message("adminui.myAccount.changePassword.label")}" }

    ];

    var errorMessageOldPassword = "${ui.message("adminui.account.changePassword.oldPassword.required")}";
    var errorMessageNewPassword = "${ui.message("adminui.account.changePassword.newPassword.required")}";
    var errorMessageConfirmPassword = "${ui.message("adminui.account.changePassword.confirmPassword.required")}";
    var errorMessageNewAndConfirmPassword = "${ui.message("adminui.account.changePassword.newAndConfirmPassword.DoesNotMatch")}";
</script>

<h3>${ui.message("adminui.myAccount.changePassword")}</h3>


<form method="post" id="accountForm">
    <fieldset>
        <legend>${ ui.message("adminui.account.details") }</legend>

        <p id="oldPasswordSection" class="emr_passwordDetails">
            <label class="form-header" for="oldPassword">${ ui.message("adminui.account.oldPassword") }</label>
            <input type="password" id="oldPassword" name="oldPassword"  autocomplete="off"/>
            ${ ui.includeFragment("uicommons", "fieldErrors", [ fieldName: "oldPassword" ])}
        </p>

        <p id="newPasswordSection" class="emr_passwordDetails">
            <label class="form-header" for="newPassword">${ ui.message("adminui.account.newPassword") }</label>
            <input type="password" id="newPassword" name="newPassword"  autocomplete="off"/>
            <label id="format-password">${ ui.message("adminui.account.passwordFormat") }</label>
            ${ ui.includeFragment("uicommons", "fieldErrors", [ fieldName: "newPassword" ])}
        </p>

        <p id="confirmPasswordSection" class="emr_passwordDetails">
            <label class="form-header" for="confirmPassword">${ ui.message("adminui.user.confirmPassword") }</label>
            <input type="password" id="confirmPassword" name="confirmPassword"  autocomplete="off"/>
            ${ ui.includeFragment("uicommons", "fieldErrors", [ fieldName: "confirmPassword" ])}
        </p>
    </fieldset>

    <div>
        <input type="button" class="cancel" value="${ ui.message("adminui.cancel") }" onclick="javascript:window.location='/${ contextPath }/adminui/account/myAccount.page'" />
        <input type="submit" class="confirm" id="save-button" value="${ ui.message("adminui.save") }"  />
    </div>

</form>

