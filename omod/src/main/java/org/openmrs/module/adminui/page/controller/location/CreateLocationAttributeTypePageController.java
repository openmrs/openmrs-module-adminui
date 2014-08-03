package org.openmrs.module.adminui.page.controller.location;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.LocationAttributeType;
import org.openmrs.api.LocationService;
import org.openmrs.customdatatype.CustomDatatypeUtil;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.LocationAttributeTypeValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public class CreateLocationAttributeTypePageController {

	public void get(PageModel model, @RequestParam(value = "locationAttributeTypeId", required = false) Integer locationAttributeTypeId, 
			@SpringBean ("locationService") LocationService locationService) {

			LocationAttributeType locationAttributeType = new LocationAttributeType();
			if (locationAttributeTypeId != null) {
				locationAttributeType =  locationService.getLocationAttributeType(Integer.valueOf(locationAttributeTypeId));
			}

			model.addAttribute("locationAttributeType", locationAttributeType);
			model.addAttribute("datatypes", getDatatypes());
			model.addAttribute("handlers", getHandlers());
	}
	
	public String post(PageModel model, @ModelAttribute("locationAttributeType") @BindParams LocationAttributeType locationAttributeType,
			BindingResult errors, 
			@SpringBean("locationService") LocationService locationService,
			@SpringBean("locationAttributeTypeValidator") LocationAttributeTypeValidator locationAttributeTypeValidator,
			@RequestParam(required=false , value = "save") String saveFlag,
			@RequestParam(required=false , value = "retire") String retireFlag,
			@RequestParam(required=false , value = "purge") String purgeFlag,
			HttpServletRequest request ) {
		
		Errors newErrors = new BindException(locationAttributeType, "locationAttributeType");
		locationAttributeTypeValidator.validate(locationAttributeType, newErrors);
		
		if (!newErrors.hasErrors()) {
			try {
				if(saveFlag.length() > 3) {
					locationService.saveLocationAttributeType(locationAttributeType);
					request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationAttributeType.saved");
				}
				else if(retireFlag.length() > 3) {
					String reason = request.getParameter("retireReason");
					locationService.retireLocationAttributeType(locationAttributeType, reason);
					request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationAttributeType.retired");
					}
				else if(purgeFlag.length() > 3) {
					locationService.purgeLocationAttributeType(locationAttributeType);
					request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationAttributeType.purged");
					}
				return "redirect:/adminui/location/manageLocationAttributeTypes.page";
			}
			catch (Exception e) {
				request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.save.fail");
			}
		}

		else {
        }
	
		model.addAttribute("errors", newErrors);
		model.addAttribute("locationAttributeType", locationAttributeType);
		
		return "location/ccreateLocationAttributeType";
	}
	
	public Collection<String> getDatatypes() {
		return CustomDatatypeUtil.getDatatypeClassnames();
	}
	
	public Collection<String> getHandlers() {
		return CustomDatatypeUtil.getHandlerClassnames();
	}
}