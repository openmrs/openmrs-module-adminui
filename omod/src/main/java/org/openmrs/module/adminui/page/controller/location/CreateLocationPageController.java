package org.openmrs.module.adminui.page.controller.location;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.module.adminui.location.LocationValidator;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public class CreateLocationPageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	 /*public Location getLocation(@RequestParam(value = "locationId", required = false) Integer locationId,
             					@SpringBean("locationService") LocationService locationService) {
		 
		 Location location = null;
			
		 if (locationId != null)
			 location = locationService.getLocation(Integer.valueOf(locationId));
			
		 if (location == null)
			 location = new Location();
			
		 return location;

	 }	*/
	
	/**
	 * @param model
	 * @param locationId
	 * @param locationService
	 */
	public void get(PageModel model, @RequestParam(value = "locationId", required = false) Integer locationId, 
					@SpringBean ("locationService") LocationService locationService) {
		
		Location location = new Location();
		/*if (locationId != null) {
			location =  locationService.getLocation(Integer.valueOf(locationId));
		}*/
		
		model.addAttribute("location", location);
		model.addAttribute("existingLocations", locationService.getAllLocations());
		model.addAttribute("locationTags", locationService.getAllLocationTags());
		model.addAttribute("attributeTypes", locationService.getAllLocationAttributeTypes());
	}
	
	/**
	 * 
	 * @param model
	 * @param location
	 * @param errors
	 * @param locationService
	 * @param locationValidator
	 * @param messageSource
	 * @param messageSourceService
	 * @param request
	 * @return
	 */
	public String post(PageModel model, @ModelAttribute("location") @BindParams Location location,
						BindingResult errors, 
						@SpringBean("locationService") LocationService locationService,
						@SpringBean("locationValidator") LocationValidator locationValidator,
						@SpringBean("messageSource") MessageSource messageSource,
	                    @SpringBean("messageSourceService") MessageSourceService messageSourceService,
						HttpServletRequest request ) {
		
		if (!errors.hasErrors()) {
			System.err.println("HAS NO ERRORS");
		}

		//locationValidator.validate(procedure, newErrors);
		
		Errors newErrors = new BindException(location, "location");
		
		if (!newErrors.hasErrors()) {
			System.err.println("HAS NO ERRORS");
			try {
				locationService.saveLocation(location);
				System.err.println("saved successfully: redirecting");
				request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE,
                        messageSourceService.getMessage("adminui.location.saved"));
				return "redirect:/referenceapplication/home.page";
			}
			catch (Exception e) {
				log.warn("Some error occurred while saving location details:", e);
				request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                        "adminui.save.fail");
			}
		}

		else {
            sendErrorMessage(errors, messageSource, request);
        }

		model.addAttribute("errors", newErrors);
		model.addAttribute("location", location);
		
		return "location/location";
		
	}
	
	private void sendErrorMessage(BindingResult errors, MessageSource messageSource, HttpServletRequest request) {
        List<ObjectError> allErrors = errors.getAllErrors();
        String message = getMessageErrors(messageSource, allErrors);
        request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                message);
    }

    private String getMessageErrors(MessageSource messageSource, List<ObjectError> allErrors) {
        String message = "";
        for (ObjectError error : allErrors) {
            Object[] arguments = error.getArguments();
            String errorMessage = messageSource.getMessage(error.getCode(), arguments, Context.getLocale());
            message = message.concat(replaceArguments(errorMessage, arguments).concat("<br>"));
        }
        return message;
    }

    private String replaceArguments(String message, Object[] arguments) {
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                String argument = (String) arguments[i];
                message = message.replaceAll("\\{" + i + "\\}", argument);
            }
        }
        return message;
    }

}
