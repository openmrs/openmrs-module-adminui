/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.metadata;

import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appui.ExtensionByLabelComparator;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.ViewException;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Controller
public class ConfigureMetadataPageController {

    public static final String CONFIGURE_METADATA_ADMIN_GROUPS_EXTENSION_POINT_ID = "org.openmrs.module.adminui.adminGroups";
    public static final String CONFIGURE_METADATA_ADMIN_LINKS_EXTENSION_POINT_ID = "org.openmrs.module.adminui.adminLinks";

    /**
     * Process requests to show the home page
     *
     * @param model
     * @param appFrameworkService
     */
    public void get(PageModel model, @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService, UiUtils ui) {
        //We need to keep the groups and links sorted by label
        //for consistent ordering every time the page gets loaded
        ExtensionByLabelComparator comparator = new ExtensionByLabelComparator(ui);
        Map<Extension, Set<Extension>> adminGroupAndLinksMap = new TreeMap<Extension, Set<Extension>>(comparator);
        List<Extension> adminGroups = appFrameworkService.getExtensionsForCurrentUser(CONFIGURE_METADATA_ADMIN_GROUPS_EXTENSION_POINT_ID);
        List<Extension> adminLinks = appFrameworkService.getExtensionsForCurrentUser(CONFIGURE_METADATA_ADMIN_LINKS_EXTENSION_POINT_ID);

        for (Extension adminLink : adminLinks) {
            String groupExtensionId = (String) adminLink.getExtensionParams().get("group");
            Extension group = null;
            for (Extension adminGroup : adminGroups) {
                if (adminGroup.getId().equals(groupExtensionId)) {
                    group = adminGroup;
                }
            }

            if (group == null) {
                throw new ViewException("No admin group extension was found with id: " + groupExtensionId);
            }

            if (adminGroupAndLinksMap.get(group) == null) {
                adminGroupAndLinksMap.put(group, new TreeSet<Extension>(comparator));
            }
            adminGroupAndLinksMap.get(group).add(adminLink);
        }

        model.addAttribute("adminGroupAndLinksMap", adminGroupAndLinksMap);
    }

}
