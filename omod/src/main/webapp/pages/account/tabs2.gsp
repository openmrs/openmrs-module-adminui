<%
    ui.decorateWith("appui", "standardEmrPage")


%>

<script type="text/javascript">

var tabs = 1;

function addTab() {

    ++tabs;
    
    // hide other tabs
    jq("#tabs li").removeClass("current ui-tabs-active ui-state-active");
    jq("#tabContent div").hide();

    // add new tab and related tabContent
    jq("#tabs").append("<li class='current ui-tabs-active ui-state-active'><a class='tab' href='#' id='user"+tabs+"'>User "+(tabs)+"</a><a href='#' class='remove'>x</a></li>");
    
    jq("#tabContent").append("<div id='user"+tabs+"_tabContent'>"+
        "<form id='user"+tabs+"_form'>" +
            "<p><label>Username</label> <input type='text' id='user"+tabs+"_username' id='user"+tabs+"_username'></p>" +
            "<p><label>Password</label> <input type='password' id='user"+tabs+"_password' id='user"+tabs+"_password'></p>" +
            "<p><label>Confirm Password</label> <input type='password' id='user"+tabs+"_confirmPassword' id='user"+tabs+"_confirmPassword'></p>" +
               
            <% context.authenticatedUser.roles.findAll { !it.retired }.each { %> 
            "$it.role<input type='checkbox'>" +
            <% } %> 
            "</form>" +
            "</div>");
    
    // set the newly added tab as current
    jq("#user"+tabs+"_tabContent").show();

}

\$(document).ready(function() {

    \$('#tabs a.tab').live('click', function() {
        // Get the tab name
        var tabContentname = \$(this).attr("id") + "_tabContent";
   
        // hide all other tabs
        \$("#tabContent div").hide();
        \$("#tabs li").removeClass("current ui-tabs-active ui-state-active");
    
        // show current tab
        \$("#" + tabContentname).show();
        \$(this).parent().addClass("current ui-tabs-active ui-state-active");
    });

    \$('#tabs a.remove').live('click', function() {
    
        // Get the tab name
        var tabid = \$(this).parent().find(".tab").attr("id");
    
        // remove tab and related tabContent
        var tabContentname = tabid + "_tabContent";
    
        \$("#" + tabContentname).remove();
        \$(this).parent().remove();

        // if there is no current tab and if there are still tabs left, show the first one
        if (\$("#tabs li.(current ui-tabs-active ui-state-active)").length == 0 && \$("#tabs li").length > 0) {
            // find the first tab
            var firsttab = \$("#tabs li:first-child");
            firsttab.addClass("current ui-tabs-active ui-state-active");
    
            // get its link name and show related tabContent
            var firsttabid = \$(firsttab).find("a.tab").attr("id");
            \$("#" + firsttabid + "_tabContent").show();
        } 
    });
});


</script>

<div>
    <a class="button task" href="#" onclick="addTab()">
        <i class="icon-plus"></i>
        Add User
    </a>

    <br><br>

    <div class="ui-tabs">
        <ul id="tabs" class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" role="tablist">
            
            <li class="ui-state-default ui-corner-top ui-tabs-active ui-state-active">
                <a class="tab" href="#" id="user1">
                    User 1
                </a>
            </li>
        
        </ul>
    
        <div id="tabContent" class="ui-tabs-panel ui-widget-tabContent ui-corner-bottom">
            <div id="user1_tabContent">
                <form id="user1_form">
                Name<input type="text"></input>
                Password<input type="password"></input>
                <input type="submit"></input>
                </form>
            </div>
        </div>

    </div>

</div>