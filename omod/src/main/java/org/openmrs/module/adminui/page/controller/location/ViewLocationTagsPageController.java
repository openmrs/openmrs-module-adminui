package org.openmrs.module.adminui.page.controller.location;

import org.openmrs.api.LocationService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

public class ViewLocationTagsPageController {
	
	/**
	 * @param model
	 * @param locationService
	 */
    public void get(PageModel model, @SpringBean("locationService") LocationService locationService) {
        model.addAttribute("locationTags", locationService.getAllLocationTags(true));
    }

}
