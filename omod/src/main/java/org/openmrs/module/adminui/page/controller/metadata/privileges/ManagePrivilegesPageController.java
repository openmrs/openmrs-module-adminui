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
import org.openmrs.api.UserService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ManagePrivilegesPageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @param model
	 * @param userService
	 */
	public void get(PageModel model, @SpringBean("userService") UserService userService) {
		model.addAttribute("privileges", userService.getAllPrivileges());
	}
	
	public String post(PageModel model,
	                   @RequestParam(value = "privilegeName", required = false) @BindParams Privilege privilege,
	                   @SpringBean("userService") UserService userService, HttpSession session) {
		
		String action = "purge";
		try {
			userService.purgePrivilege(privilege);
			InfoErrorMessageUtil.flashInfoMessage(session, "adminui.privilege." + action + ".success");
			return "redirect:/adminui/metadata/privileges/managePrivileges.page";
		}
		catch (Exception e) {
			log.error("Failed to " + action + " privilege:", e);
		}
		
		session.setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.privilege." + action + ".fail");
		model.addAttribute("privileges", userService.getAllPrivileges());
		
		return "metadata/privileges/managePrivileges";
	}
	
}
