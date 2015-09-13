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

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.LocationService;
import org.openmrs.module.adminui.ByRetiredComparator;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ManageLocationAttributeTypesPageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @param model
	 * @param locationService
	 */
	public void get(PageModel model, @SpringBean("locationService") LocationService locationService) {
		List<LocationAttributeType> locationAttributeTypes = locationService.getAllLocationAttributeTypes();
		Collections.sort(locationAttributeTypes, new ByRetiredComparator());
		model.addAttribute("locationAttributeTypes", locationAttributeTypes);
	}
	
	public String post(PageModel model,
	                   @RequestParam("locationAttributeTypeId") LocationAttributeType locationAttributeType,
	                   @RequestParam("action") String action,
	                   @RequestParam(value = "reason", required = false) String reason,
	                   @SpringBean("locationService") LocationService locationService, HttpServletRequest request) {
		
		try {
			if ("retire".equals(action)) {
				locationService.retireLocationAttributeType(locationAttributeType, reason);
			} else if ("restore".equals(action)) {
				locationService.unretireLocationAttributeType(locationAttributeType);
			} else if ("purge".equals(action)) {
				locationService.purgeLocationAttributeType(locationAttributeType);
			}
			InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.locationAttributeType." + action
			        + ".success");
			return "redirect:/adminui/metadata/locations/manageLocationAttributeTypes.page";
		}
		catch (Exception e) {
			log.error("Failed to " + action + " location attribute type:", e);
		}
		
		request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
		    "adminui.locationAttributeType." + action + ".fail");
		model.addAttribute("locationAttributeTypes", locationService.getAllLocationAttributeTypes());
		
		return "metadata/locations/manageLocationAttributeTypes";
	}
}
