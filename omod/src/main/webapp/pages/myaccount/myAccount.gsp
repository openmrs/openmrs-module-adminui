<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("adminui.myAccount") ])

%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.myAccount.label")}" }
    ];
</script>

<div id="tasks">
    <a class="button app big" href="${ ui.pageLink("adminui", "myaccount/changePassword") }">
        <div class="task">
            <i class="icon-lock"></i>
            ${ ui.message("adminui.myAccount.changePassword.label") }
        </div>
    </a>
    <%
    /* We actually don't use these anywhere to reset a user's password
    <a class="button app big" href="${ ui.pageLink("adminui", "myaccount/changeSecretQuestion") }">
        <div class="task">
            <i class="icon-unlock"></i>
            ${ ui.message("adminui.myAccount.changeSecretQuestion.label") }
        </div>
    </a>
    */
    %>
    <a class="button app big" href="${ ui.pageLink("adminui", "myaccount/changeDefaults") }">
        <div class="task">
            <i class="icon-cog"></i>
            ${ ui.message("adminui.myAccount.myLanguages.label") }
        </div>
    </a>
</div>
