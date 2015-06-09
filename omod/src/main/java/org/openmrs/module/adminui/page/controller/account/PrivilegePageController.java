/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.adminui.page.controller.account;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Privilege;
import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.PrivilegeValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

public class PrivilegePageController {
	
	public void get(PageModel model, @RequestParam(value = "privilegeName", required = false) String privilegeName,
	                @SpringBean("userService") UserService userService) {
		
		Privilege privilege;
		if (StringUtils.isBlank(privilegeName)) {
			privilege = new Privilege();
		} else {
			privilege = userService.getPrivilege(privilegeName);
			if (privilege == null) {
				throw new APIException("No privilege found with name '" + privilegeName + "'");
			}
		}
		
		model.addAttribute("privilege", privilege);
	}
	
	/**
	 * @param model
	 * @param privilege
	 * @param userService
	 * @param request @return the url to redirect or forward to
	 * @should update an existing privilege if isNew is set to false and name has not changed
	 * @should fail if there is a privilege with a matching name and isNew is set to true
	 * @should fail if the name is changed to a duplicate and isNew is set to false
	 */
	public String post(PageModel model, @BindParams Privilege privilege, @SpringBean("userService") UserService userService,
	                   HttpServletRequest request) {
		
		Errors errors = new BeanPropertyBindingResult(privilege, "privilege");
		new PrivilegeValidator().validate(privilege, errors);
		
		if (!errors.hasErrors()) {
			try {
				userService.savePrivilege(privilege);
				InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.privilege.saved");
				return "redirect:/adminui/account/viewPrivileges.page";
			}
			catch (Exception e) {
				InfoErrorMessageUtil.flashErrorMessage(request.getSession(), "adminui.error.privilege.save.fail");
			}
		}
		model.addAttribute("errors", errors);
		model.addAttribute("privilege", privilege);
		
		return "account/privilege";
		
	}
}
