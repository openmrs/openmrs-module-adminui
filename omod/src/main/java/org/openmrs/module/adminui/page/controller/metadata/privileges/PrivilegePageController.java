/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.metadata.privileges;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Privilege;
import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.ValidateUtil;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

public class PrivilegePageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public void get(PageModel model, @RequestParam(value = "privilegeName", required = false) String privilegeName,
	                @RequestParam("action") String action, @SpringBean("userService") UserService userService) {
		
		Privilege privilege = null;
		if ("add".equals(action)) {
			privilege = new Privilege();
		} else if ("edit".equals(action)) {
			privilege = userService.getPrivilege(privilegeName);
		}
		if (privilege == null) {
			throw new APIException("No privilege found with name '" + privilegeName + "'");
		}
		
		model.addAttribute("privilege", privilege);
	}
	
	/**
	 * @param model
	 * @param privilege
	 * @param userService @should update an existing privilege if isNew is set to false and name has
	 *            not changed
     * @param session
	 * @should fail if there is a privilege with a matching name and isNew is set to true
	 * @should fail if the name is changed to a duplicate and isNew is set to false
	 */
	public String post(PageModel model,
	                   @RequestParam(value = "privilegeName", required = false) @BindParams Privilege privilege,
	                   @SpringBean("userService") UserService userService, HttpSession session) {
		
		Errors errors = new BeanPropertyBindingResult(privilege, "privilege");
		ValidateUtil.validate(privilege, errors);
		
		if (!errors.hasErrors()) {
			try {
				String successMsg = "adminui.privilege.saved";
				userService.savePrivilege(privilege);
				InfoErrorMessageUtil.flashInfoMessage(session, successMsg);
				return "redirect:/adminui/metadata/privileges/managePrivileges.page";
			}
			catch (Exception e) {
				log.error("Failed to save privilege:", e);
				session.setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.error.privilege.save.fail");
			}
		}
		
		model.addAttribute("errors", errors);
		model.addAttribute("privilege", privilege);
		
		return "metadata/privileges/privilege";
		
	}
}
