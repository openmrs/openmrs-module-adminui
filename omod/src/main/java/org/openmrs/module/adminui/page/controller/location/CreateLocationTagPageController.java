package org.openmrs.module.adminui.page.controller.location;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.LocationTagValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public class CreateLocationTagPageController {

	public void get(PageModel model, @RequestParam(value = "locationTagId", required = false) Integer locationTagId, 
			@SpringBean ("locationService") LocationService locationService) {

			LocationTag locationTag = new LocationTag();
			if (locationTagId != null) {
				locationTag =  locationService.getLocationTag(Integer.valueOf(locationTagId));
			}

			model.addAttribute("locationTag", locationTag);
	}
	
	public String post(PageModel model, @ModelAttribute("locationTag") @BindParams LocationTag locationTag,
			BindingResult errors, 
			@SpringBean("locationService") LocationService locationService,
			@SpringBean("locationTagValidator") LocationTagValidator locationTagValidator,
			@RequestParam(required=false , value = "save") String saveFlag,
			@RequestParam(required=false , value = "retire") String retireFlag,
			HttpServletRequest request ) {
		
		Errors newErrors = new BindException(locationTag, "locationTag");
		locationTagValidator.validate(locationTag, newErrors);
		
		if (!newErrors.hasErrors()) {
			try {
				if(saveFlag.length() > 3) {
					locationService.saveLocationTag(locationTag);
					request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationTag.saved");
				}
				else if(retireFlag.length() > 3) {
					String reason = request.getParameter("retireReason");
					locationService.retireLocationTag(locationTag, reason);
					request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationTag.retired");
					}
				return "redirect:/adminui/location/manageLocationTags.page";
			}
			catch (Exception e) {
				request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.save.fail");
			}
		}

		else {
            
        }
	
		model.addAttribute("errors", newErrors);
		model.addAttribute("locationTag", locationTag);
		
		return "location/locationTag";
	}
}
