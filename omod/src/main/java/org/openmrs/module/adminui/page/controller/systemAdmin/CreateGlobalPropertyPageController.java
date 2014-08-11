package org.openmrs.module.adminui.page.controller.systemAdmin;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public class CreateGlobalPropertyPageController {
	
	public void get(PageModel model, @RequestParam(value = "globalProperty", required = false) String globalPropertyName) {

			AdministrationService administrationService = Context.getAdministrationService();
			GlobalProperty globalProperty = new GlobalProperty();
			if (globalPropertyName != null) {
				globalProperty =  administrationService.getGlobalPropertyObject(globalPropertyName);
			}

			model.addAttribute("globalProperty", globalProperty);
	}
	
	public String post(PageModel model, @ModelAttribute("globalProperty") @BindParams GlobalProperty globalProperty,
			BindingResult errors, 
			@RequestParam(required=false , value = "save") String saveFlag,
			@RequestParam(required=false , value = "purge") String purgeFlag,
			HttpServletRequest request ) {
		
		AdministrationService administrationService = Context.getAdministrationService();
		Errors newErrors = new BindException(globalProperty, "globalProperty");
		
		if (!newErrors.hasErrors()) {
			try {
				if(saveFlag.length() > 3) {
					administrationService.saveGlobalProperty(globalProperty);
					request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.globalProperty.saved");
				}
				else if(purgeFlag.length() > 3) {
					administrationService.purgeGlobalProperty(globalProperty);
					request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.globalProperty.purged");
					}
				return "redirect:/adminui/systemAdmin/globalProps.page";
			}
			catch (Exception e) {
				request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.save.fail");
			}
		}

		else {
            
        }
	
		model.addAttribute("errors", newErrors);
		model.addAttribute("globalProperty", globalProperty);
		
		return "systemAdmin/globalProps";
	}

}
