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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.web.taglib.LocationAndDepth;
import org.springframework.web.bind.annotation.RequestParam;

public class ManageLocationsPageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @param model
	 * @param locationService
	 */
    public void get(PageModel model, @SpringBean("locationService") LocationService locationService) {
    	List<LocationAndDepth> locationAndDepths = new ArrayList<LocationAndDepth>();
		List<Location> locations = locationService.getRootLocations(true);
		populateLocationAndDepthList(locationAndDepths, locations, 0);
    	model.addAttribute("locations", locationAndDepths);
    }
    
    public String post(@RequestParam("locationId") Location location,
            @RequestParam(value = "action") String action,
            @RequestParam(value = "reason", required =  false) String reason,
            HttpSession session, @SpringBean("locationService") LocationService locationService) {

		if (StringUtils.isNotBlank(action)) {
			try {
				String message = null;
				
				if ("purge".equals(action)) {
					message = "adminui.purged";
					locationService.purgeLocation(location);
				}
				else if ("retire".equals(action)) {
					message = "adminui.retired";
					if (StringUtils.isBlank(reason)) {
						reason = Context.getMessageSourceService().getMessage("general.default.retireReason");
					}
					locationService.retireLocation(location, reason);
				}
				else if ("restore".equals(action)) {
					message = "adminui.restored";
					locationService.unretireLocation(location);
				}

				InfoErrorMessageUtil.flashInfoMessage(session, message);			
			}
			catch (Exception e) {
				log.error(e.getMessage(), e);
				session.setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui." + action + ".fail");	
			}
		}
		
		return "redirect:adminui/metadata/locations/manageLocations.page";
	}
    
    private void populateLocationAndDepthList(List<LocationAndDepth> locationAndDepths, 
    		Collection<Location> locations, int depth) {
    	
		for (Location location : locations) {
			locationAndDepths.add(new LocationAndDepth(depth, location));
			if (location.getChildLocations() != null && location.getChildLocations().size() > 0) {
				populateLocationAndDepthList(locationAndDepths, location.getChildLocations(), depth + 1);
			}	
		}
	}
}
