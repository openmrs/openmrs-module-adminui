package org.openmrs.module.adminui.utils;

/**
 * Evaluates a string for a given pattern
 */
public class RegexUtil {

	/**
	 * TODO: Confirm the correct pattern for the following regex.
	 */
	public static final String MUST_CONTAIN_DIGIT_REGEX = "(?=.*\\d).+";
	public static final String MUST_CONTAIN_NONE_DIGIT_REGEX = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+";//"(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9])";
	public static final String MUST_CONTAIN_MIXED_CASES_REGEX = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])";

	private String toEvaluate;
	private String regex;

	public RegexUtil(){}

	public RegexUtil(String toEvaluate){
		this.toEvaluate = toEvaluate;
	}

	public RegexUtil(String toEvaluate, String regex) {
		this.toEvaluate = toEvaluate;
		this.regex = regex;
	}

	public String getToEvaluate() {
		return toEvaluate;
	}

	public void setToEvaluate(String toEvaluate) {
		this.toEvaluate = toEvaluate;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public boolean matcher (){
		if (MUST_CONTAIN_DIGIT_REGEX.equals(regex)){
			return toEvaluate.matches(MUST_CONTAIN_DIGIT_REGEX);
		}

		if (MUST_CONTAIN_NONE_DIGIT_REGEX.equals(regex)) {
			return toEvaluate.matches(MUST_CONTAIN_NONE_DIGIT_REGEX);
		}

		if (MUST_CONTAIN_MIXED_CASES_REGEX.equals(regex)) {
			return MUST_CONTAIN_MIXED_CASES_REGEX.matches(regex);
		}

		return false;
	}
}
