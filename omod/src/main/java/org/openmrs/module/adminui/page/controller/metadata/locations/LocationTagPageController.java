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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.ValidateUtil;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

public class LocationTagPageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public void get(PageModel model, @RequestParam(value = "locationTagId", required = false) LocationTag locationTag) {
		
		if (locationTag == null) {
			locationTag = new LocationTag();
		}
		
		model.addAttribute("locationTag", locationTag);
	}
	
	public String post(PageModel model,
	                   @RequestParam(value = "locationTagId", required = false) @BindParams LocationTag locationTag,
	                   @SpringBean("locationService") LocationService locationService, HttpServletRequest request) {
		
		Errors errors = new BeanPropertyBindingResult(locationTag, "locationTag");
		ValidateUtil.validate(locationTag, errors);
		//This check should be in LocationTagValidator in core
		if (StringUtils.isNotBlank(locationTag.getName())) {
			LocationTag duplicate = locationService.getLocationTagByName(locationTag.getName());
			if (duplicate != null) {
				if (locationTag.getLocationTagId() == null || !locationTag.equals(duplicate)) {
					errors.rejectValue("name", "LocationTag.error.name.duplicate");
				}
			}
		}
		
		if (!errors.hasErrors()) {
			try {
				locationService.saveLocationTag(locationTag);
				InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.locationTag.save.success");
				return "redirect:/adminui/metadata/locations/manageLocationTags.page";
			}
			catch (Exception e) {
				log.error("Failed to save location tag:", e);
				request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    "adminui.locationTag.save.fail");
			}
		}
		
		model.addAttribute("errors", errors);
		model.addAttribute("locationTag", locationTag);
		
		return "metadata/locations/locationTag";
	}
}
