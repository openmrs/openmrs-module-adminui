<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeJavascript("adminui", "jsTree/modernizr-latest.js")
    ui.includeJavascript("adminui", "jsTree/jquery-1.9.1.min.js")
    ui.includeJavascript("adminui", "jsTree/jquery-ui.min.js")
    ui.includeJavascript("adminui", "jsTree/infragistics.core.js")
    ui.includeJavascript("adminui", "jsTree/infragistics.lob.js")
    ui.includeCss("adminui", "jsTree/infragistics.theme.css")
    ui.includeCss("adminui", "jsTree/infragistics.css")

    def no = "hiu";
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("adminui.app.administrationTools.label")}" , link: '${ui.pageLink("adminui", "adminUiHome")}'},
        { label: "${ ui.message("adminui.app.locationManager.label")}", link: '${ui.pageLink("adminui", "location/manageLocations")}' },
        { label: "${ ui.message("adminui.viewLocationHierarchy.locationManagement.label")}" }
    ];

</script>

<script type="text/javascript">


    \$(function () {

        //var data = jsonData;

            \$("#hierarchicalGrid").igHierarchicalGrid({
                maxDataBindDepth: 10,
                initialExpandDepth: -1,
                initialDataBindDepth: 1,
                expandColWidth: 50,
                width: "100%",
                dataSource: ${jsonData}, //Array of objects defined above
                dataSourceType: "json",
                responseDataKey: "d",
                autoGenerateLayouts: true
            });

            \$(".selector").igHierarchicalGrid("dataBind");

        });
    </script>

<h3>${ ui.message("Location Hierarchy") }

<table id="hierarchicalGrid"></table>

