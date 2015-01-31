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

import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ChangeSecretQuestionPageController {

	public String get(PageModel model) {
		return "account/changeSecretQuestion";
	}

	public String post(@MethodParam("createSecretQuestion") @BindParams SecretQuestion secretQuestion,
	                   BindingResult errors,
	                   @SpringBean("userService") UserService userService,
	                   @SpringBean("messageSourceService") MessageSourceService messageSourceService,
	                   @SpringBean("messageSource") MessageSource messageSource,
	                   HttpServletRequest request,
	                   PageModel model) {
		validateSecretQuestion(secretQuestion, errors, messageSourceService);
		if (errors.hasErrors()) {
			sendErrorMessage(errors, messageSource, request, model);
			return "account/changeSecretQuestion";
		}
		return changeSecretQuestion(secretQuestion, userService, messageSourceService, request);
	}

	public SecretQuestion createSecretQuestion(String password, String question, String answer, String confirmAnswer) {
		return new SecretQuestion(password, question, answer, confirmAnswer);
	}

	private String changeSecretQuestion(SecretQuestion secretQuestion, UserService userService,
	                                    MessageSourceService messageSourceService, HttpServletRequest request) {
		try {
			userService.changeQuestionAnswer(secretQuestion.getPassword(), secretQuestion.getQuestion(), secretQuestion.getAnswer());
			request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE,
					messageSourceService.getMessage("adminui.account.secretQuestion.success", null, Context.getLocale()));
			request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_TOAST_MESSAGE, "true");
		} catch (Exception ex) {
			request.getSession().setAttribute(
					AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
					messageSourceService.getMessage("adminui.account.secretQuestion.fail", new Object[]{ex.getMessage()},
							Context.getLocale()));
			return "account/changeSecretQuestion";
		}
		return "account/myAccount";
	}

	private void validateSecretQuestion(SecretQuestion secretQuestion, BindingResult errors,
	                                    MessageSourceService messageSourceService) {
		if (!secretQuestion.getAnswer().equals(secretQuestion.getConfirmAnswer())) {
			errors.rejectValue("confirmAnswer", "adminui.account.answerAndConfirmAnswer.doesNotMatch",
					new Object[]{messageSourceService
							.getMessage("adminui.account.answerAndConfirmAnswer.doesNotMatch")}, null);
		}
	}

	private void sendErrorMessage(BindingResult errors, MessageSource messageSource, HttpServletRequest request, PageModel model) {
		List<ObjectError> allErrors = errors.getAllErrors();
		String message = getMessageErrors(messageSource, allErrors);
		request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, message);
		model.addAttribute("errors", errors);
	}

	private String getMessageErrors(MessageSource messageSource, List<ObjectError> allErrors) {
		String message = "";
		if (allErrors != null && allErrors.isEmpty()) {
			ObjectError error = allErrors.get(0);
			Object[] arguments = error.getArguments();
			message = messageSource.getMessage(error.getCode(), arguments, Context.getLocale());
		}
		return message;
	}

	public class SecretQuestion {

		private String password;
		private String question;
		private String answer;
		private String confirmAnswer;

		public SecretQuestion(String password, String question, String answer, String confirmAnswer) {
			this.password = password;
			this.question = question;
			this.answer = answer;
			this.confirmAnswer = confirmAnswer;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getQuestion() {
			return question;
		}

		public void setQuestion(String question) {
			this.question = question;
		}

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}

		public String getConfirmAnswer() {
			return confirmAnswer;
		}

		public void setConfirmAnswer(String confirmAnswer) {
			this.confirmAnswer = confirmAnswer;
		}

		@Override
		public String toString() {
			return "Secret Question:Question=" + question + ", Answer=" + answer;
		}
	}
}
