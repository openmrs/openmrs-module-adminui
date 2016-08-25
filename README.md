[![Build Status](https://travis-ci.org/openmrs/openmrs-module-adminui.svg?branch=master)](https://travis-ci.org/openmrs/openmrs-module-adminui)

Admin UI Module
================

Administration tools for the OpenMRS Reference Application.

It is written using the new [UI framework](https://wiki.openmrs.org/x/0wAJAg), some pages
are written with the Groovy Server Page technology being backed up by controllers while other pages are written using
angular and communicating with the server via the REST API provided by the web services module.

The project tree is set up as follows:

<table>
    <tr>
        <td>
            api
        </td>
        <td>
         	Java and resource files for building the java api jar file. It also contains the message
         	resource bundles files for translation and the moduleApplicationContext.xml file
        </td>
    </tr>
    <tr>
        <td>
            omod
        </td>
        <td>
            Files for the web layer reside in this directory like GSPs, html, Css, Javascript files etc.
            It also contains the apps, extensions, config.xml and the webModuleApplicationContext.xml file.
        </td>
    </tr>
    <tr>
        <td>
            pom.xml
        </td>
        <td>
            The main maven file used to build and package the .omod file
        </td>
    </tr>
</table>