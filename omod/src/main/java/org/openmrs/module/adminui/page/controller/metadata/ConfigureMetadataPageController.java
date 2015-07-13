/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p/>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p/>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
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
     * @throws IOException
     */
    public void get(PageModel model, @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService, UiUtils ui) {
        //We need to keep the groups and links sorted by label
        //for consistent ordering every time the page gets loaded
        Comparator comparator = new ExtensionByLabelComparator(ui);
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
