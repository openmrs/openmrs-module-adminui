<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeCss("adminui", "account.css")
    ui.includeJavascript("adminui", "account/account.js")
    ui.includeJavascript("adminui", "account/addTab.js")
    ui.includeJavascript("adminui", "account/jquery-1.4.4.min.js")
    ui.includeJavascript("adminui", "account/jquery.easyui.min.js")
    ui.includeCss("adminui", "account/easyui.css")
    ui.includeCss("adminui", "account/icon.css")


def genderOptions = [ [label: ui.message("adminui.gender.M"), value: 'M'],
                          [label: ui.message("adminui.gender.F"), value: 'F'] ]

%>


<div style="margin-bottom:10px">
<% userNo = 1 %>
    <a href="#" class="easyui-linkbutton" onclick="addTab('User ${ ++userNo} ','')">Add User</a>

</div>


<div id="tt" class="easyui-tabs" style="width:400px;height:250px;">
    <div title="User 1">

    </div>
</div>