<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("adminui.myAccount") ])

%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.administrationTools.label")}" , link: '${ui.pageLink("adminui", "adminUiHome")}'},
        { label: "${ ui.message("adminui.app.myAccount.label")}" }
    ];
</script>

<div id="tasks">
    <a class="button app big" href="${ ui.pageLink("adminui", "myAccount/changePassword") }">
        <div class="task">
            <i class="icon-unlock"></i>
            ${ ui.message("adminui.myAccount.changePassword.label") }
        </div>
    </a>
    <a class="button app big" href="${ ui.pageLink("adminui", "myAccount/changeSecretQuestion") }">
        <div class="task">
            <i class="icon-unlock"></i>
            ${ ui.message("adminui.myAccount.changeSecretQuestion.label") }
        </div>
    </a>
    <a class="button app big" href="${ ui.pageLink("adminui", "myAccount/changeDefaults") }">
        <div class="task">
            <i class="icon-unlock"></i>
            ${ ui.message("adminui.myAccount.changeDefaults.label") }
        </div>
    </a>
</div>
