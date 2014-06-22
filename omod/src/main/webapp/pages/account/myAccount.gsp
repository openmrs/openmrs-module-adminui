<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("emr.myAccount") ])

    ui.includeCss("mirebalais", "app.css")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.myAccount.label")}" }
    ];
</script>

<div id="tasks">
    <a class="button app big" href="${ ui.pageLink("adminui", "account/changePassword") }">
        <div class="task">
            <i class="icon-unlock"></i>
            ${ ui.message("adminui.myAccount.changePassword.label") }
        </div>
    </a>
</div>
