/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.fragment.controller.systemadmin.accounts;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.OpenmrsObject;
import org.openmrs.Provider;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.action.FailureResult;
import org.openmrs.ui.framework.fragment.action.FragmentActionResult;
import org.openmrs.ui.framework.fragment.action.SuccessResult;
import org.openmrs.validator.ValidateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProviderTabContentPaneFragmentController {
	
	public void get() {
	}
	
	public FragmentActionResult process(@RequestParam(value = "uuid", required = false) @BindParams Provider provider,
	                                    @RequestParam(value = "action", required = false) String action,
	                                    @RequestParam(value = "reason", required = false) String reason,
	                                    @SpringBean("personService") PersonService personService,
	                                    @SpringBean("providerService") ProviderService providerService,
	                                    HttpServletRequest request, UiUtils ui) {
		
		try {
			String successMessage = "adminui.savedChanges";
			if ("retire".equals(action)) {
				successMessage = "adminui.retired";
				providerService.retireProvider(provider, reason);
			} else if ("restore".equals(action)) {
				successMessage = "adminui.restored";
				providerService.unretireProvider(provider);
			} else {
				if (provider.getProviderId() == null) {
					successMessage = "adminui.saved";
				}
				Errors errors = new BeanPropertyBindingResult(provider, "provider");
				ValidateUtil.validate(provider, errors);
				//A person should have exactly one account with a given provide role
				for (OpenmrsObject o : providerService.getProvidersByPerson(provider.getPerson())) {
					Provider otherProvider = (Provider) o;
					if (provider.equals(otherProvider)) {
						continue;//same provider, skip
					}
					if (provider.getProviderRole().equals(otherProvider.getProviderRole())) {
						errors.rejectValue("providerRole", "adminui.provider.error.other.alreadyHasRole");
						break;
					}
				}
				
				if (errors.hasErrors()) {
					return new FailureResult(errors);
				}
				providerService.saveProvider(provider);
			}
			return new SuccessResult(ui.message(successMessage));
		}
		catch (Exception e) {
			String failMessage = "adminui.saveChanges.fail";
			if ("retire".equals(action)) {
				failMessage = "adminui.retire.fail";
				providerService.retireProvider(provider, reason);
			} else if ("restore".equals(action)) {
				failMessage = "adminui.restore.fail";
				providerService.unretireProvider(provider);
			} else if (provider.getProviderId() == null) {
				failMessage = "adminui.save.fail";
			}
			return new FailureResult(ui.message(failMessage));
		}
	}
}
