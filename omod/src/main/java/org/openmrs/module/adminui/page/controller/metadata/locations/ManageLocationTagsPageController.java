/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.metadata.locations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class ManageLocationTagsPageController {

    protected final Log log = LogFactory.getLog(getClass());

    /**
     * @param model
     * @param locationService
     */
    public void get(PageModel model, @SpringBean("locationService") LocationService locationService) {
        model.addAttribute("locationTags", locationService.getAllLocationTags(true));
    }

    public String post(PageModel model, @RequestParam("locationTagId") LocationTag tag,
                       @RequestParam("action") String action,
                       @RequestParam(value = "reason", required = false) String reason,
                       @SpringBean("locationService") LocationService locationService, HttpServletRequest request) {

        try {
            if ("retire".equals(action)) {
                locationService.retireLocationTag(tag, reason);
            } else if ("restore".equals(action)) {
                locationService.unretireLocationTag(tag);
            } else if ("purge".equals(action)) {
                locationService.purgeLocationTag(tag);
            }
            InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.locationTag." + action + ".success");
            return "redirect:/adminui/metadata/locations/manageLocationTags.page";
        } catch (Exception e) {
            log.error("Failed to " + action + " location tag:", e);
        }

        request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                "adminui.locationTag." + action + ".fail");
        model.addAttribute("locationTags", locationService.getAllLocationTags(true));

        return "metadata/locations/manageLocationTags";
    }
}