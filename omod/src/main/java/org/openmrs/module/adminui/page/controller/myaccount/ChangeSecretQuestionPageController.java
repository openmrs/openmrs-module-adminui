/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.myaccount;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.LoginCredential;
import org.openmrs.api.db.UserDAO;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

public class ChangeSecretQuestionPageController {
	
	public void get(PageModel model) {
		String secretQuestion = fetchExistingSecretQuestionOrEmpty();
		model.put("secretQuestion", secretQuestion);
	}
	
	public String post(PageModel model, @RequestParam("password") String password,
	                   @RequestParam("question") String question, @RequestParam("answer") String answer,
	                   @RequestParam("confirmAnswer") String confirmAnswer,
	                   @SpringBean("userService") UserService userService, HttpServletRequest request) {
		if (!answer.equals(confirmAnswer)) {
			model.put("secretQuestion", fetchExistingSecretQuestionOrEmpty());
			request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
			    Context.getMessageSourceService().getMessage("adminui.account.answerAndConfirmAnswer.doesNotMatch"));
			
			return "myaccount/changeSecretQuestion";
		} else {
			return changeSecretQuestion(password, question, answer, userService, request, model);
		}
	}
	
	private String changeSecretQuestion(String password, String question, String answer, UserService userService,
	                                    HttpServletRequest request, PageModel model) {
		try {
			userService.changeQuestionAnswer(password, question, answer);
			InfoErrorMessageUtil.flashInfoMessage(request.getSession(),
			    Context.getMessageSourceService().getMessage("adminui.account.secretQuestion.success"));
		}
		catch (Exception ex) {
			request.getSession().setAttribute(
			    UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
			    Context.getMessageSourceService().getMessage("registrationapp.save.fail") + "<br />"
			            + ex.getLocalizedMessage());
			model.put("secretQuestion", question);
			
			return "myaccount/changeSecretQuestion";
		}
		return "myaccount/myAccount";
	}
	
	public String fetchExistingSecretQuestionOrEmpty() {
		LoginCredential credential = getComponent(UserDAO.class).getLoginCredential(Context.getAuthenticatedUser());
		String secretQuestion = credential.getSecretQuestion();
		
		return secretQuestion == null ? "" : secretQuestion;
	}
	
	private <T> T getComponent(Class<T> clazz) {
		List<T> list = Context.getRegisteredComponents(clazz);
		if (list == null || list.size() == 0)
			throw new RuntimeException("Cannot find component of " + clazz);
		return list.get(0);
	}
}
