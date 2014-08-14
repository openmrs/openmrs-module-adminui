package org.openmrs.module.adminui.page.controller.account;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Privilege;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.module.adminui.account.AccountService;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public class CreatePrivilegeLevelPageController {

	public void get(PageModel model, @RequestParam(value = "privilegeLevelName", required = false) String privilegeLevelName, 
			@SpringBean ("accountService") AccountService accountService) {

			Privilege privilegeLevel = new Privilege();
			if (privilegeLevelName != null) {
				/* TODO: Create function getPrivilegeLevelByName() in AccountService class and implement here 
				 * 
				 */
			}
			model.addAttribute("privilegeLevel", privilegeLevel);
	}
	
	public String post(PageModel model, @ModelAttribute("privilegeLevel") @BindParams Privilege privilegeLevel,
			BindingResult errors, 
			@SpringBean("locationService") LocationService locationService,
			@RequestParam(required=false , value = "save") String saveFlag,
			HttpServletRequest request ) {
		
		Errors newErrors = new BindException(privilegeLevel, "privilegeLevel");
		
		if (!newErrors.hasErrors()) {
			try {
				if(saveFlag.length() > 3) {
					Context.getUserService().savePrivilege(privilegeLevel);
					request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.privilegeLevel.saved");
				}
				return "redirect:/adminui/account/managePrivileges.page";
			}
			catch (Exception e) {
				request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.save.fail");
			}
		}

		else {
            
        }
	
		model.addAttribute("errors", newErrors);
		model.addAttribute("privilegeLevel", privilegeLevel);
		
		return "account/managePrivileges";
	}
}
