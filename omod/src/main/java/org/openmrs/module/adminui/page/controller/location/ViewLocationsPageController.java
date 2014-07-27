package org.openmrs.module.adminui.page.controller.location;

import java.util.List;

import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

public class ViewLocationsPageController {
	
	/**
	 * @param model
	 * @param locationService
	 */
    public void get(PageModel model, @SpringBean("locationService") LocationService locationService) {
        List<Location> locations = locationService.getAllLocations(false);
    	model.addAttribute("locations", locations);
    }
}
