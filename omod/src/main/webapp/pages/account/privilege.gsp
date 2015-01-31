<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeJavascript("adminui", "jquery.validate.js")

    def createPrivilege = (privilege.name == null ? true : false);
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.administrationTools.label")}" , link: '${ui.pageLink("adminui", "adminUiHome")}'},
        { label: "${ ui.message("adminui.app.accountManager.label")}" , link: '${ui.pageLink("adminui", "account/manageAccounts")}'},
        { label: "${ ui.message("adminui.createprivilege")}" }

    ];
</script>

<script type="text/javascript">

    jq().ready(function () {

        jq("#privilegeForm").validate({
            rules: {
                "name": {
                    required: true,
                    minlength: 3,
                    maxlength: 255
                },
                "description": {
                    required: false,
                    maxlength: 1024
                }
            },
            errorClass: "error",
            validClass: "",
            onfocusout: function (element) {
                jq(element).valid();
            },
            errorPlacement: function (error, element) {
                element.next().text(error.text());
            },
            highlight: function (element, errorClass, validClass) {
                jq(element).addClass(errorClass);
                jq(element).next().addClass(errorClass);
                jq(element).next().show();
            },
            unhighlight: function (element, errorClass, validClass) {
                jq(element).removeClass(errorClass);
                jq(element).next().removeClass(errorClass);
                jq(element).next().hide();
            }
        });
    });
</script>

<h1>
    <h3>${ (createPrivilege) ? ui.message("adminui.createPrivilege") : ui.message("adminui.editPrivilege") }</h3>
</h1>

<form class="simple-form-ui" method="post" id="privilegeForm" autocomplete="off">
<fieldset>
    ${ui.includeFragment("uicommons", "field/text", [
            label        : ui.message("adminui.privilege.name")+"*",
            formFieldName: "name",
            id           : "name",
            maxLength    : 101,
            initialValue : (privilege.name ?: '')
    ])}

    ${ui.includeFragment("uicommons", "field/textarea", [
            label        : ui.message("adminui.privilege.description"),
            formFieldName: "description",
            id           : "description",
            initialValue : (privilege.description ?: '')
    ])}

    <div>
        <input type="button" class="cancel" value="${ui.message("adminui.cancel")}" onclick="javascript:window.location='/${ contextPath }/adminui/account/managePrivileges.page'"/>
        <input type="submit" class="confirm" name="save" id="save-button" value="${ui.message("adminui.save")}"/>
    </div>
    </fieldset>