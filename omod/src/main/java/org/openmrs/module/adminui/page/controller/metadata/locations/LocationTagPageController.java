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
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.LocationTagValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class LocationTagPageController {

    protected final Log log = LogFactory.getLog(getClass());

    public void get(PageModel model, @RequestParam(value = "locationTagId", required = false) Integer locationTagId,
                    @SpringBean("locationService") LocationService locationService) {

        LocationTag locationTag = new LocationTag();
        if (locationTagId != null) {
            locationTag = locationService.getLocationTag(Integer.valueOf(locationTagId));
        }

        model.addAttribute("locationTag", locationTag);
    }

    public String post(PageModel model, @RequestParam(value = "locationTagId", required = false) @BindParams LocationTag locationTag,
                       @SpringBean("locationService") LocationService locationService,
                       @SpringBean("locationTagValidator") LocationTagValidator locationTagValidator,
                       HttpServletRequest request) {

        Errors errors = new BeanPropertyBindingResult(locationTag, "locationTag");
        locationTagValidator.validate(locationTag, errors);

        if (!errors.hasErrors()) {
            try {
                locationService.saveLocationTag(locationTag);
                InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.locationTag.save.success");
                return "redirect:/adminui/metadata/locations/manageLocationTags.page";
            } catch (Exception e) {
                log.error("Failed to save location tag:", e);
                request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.locationTag.save.fail");
            }
        }

        model.addAttribute("errors", errors);
        model.addAttribute("locationTag", locationTag);

        return "metadata/locations/locationTag";
    }
}
