package org.openmrs.module.adminui.utils;

import org.junit.Assert;
import org.junit.Test;

public class RegexUtilTest {

	@Test
	public void shouldEvaluateToFalseForStringWithNoDigit(){
		RegexUtil util = new RegexUtil("thisissomething", RegexUtil.MUST_CONTAIN_DIGIT_REGEX);
		Assert.assertFalse("String should have at least a digit", util.matcher());
	}

	@Test
	public void shouldEvaluateToTrueForStringWithDigits(){
		RegexUtil util = new RegexUtil("thisis12something", RegexUtil.MUST_CONTAIN_DIGIT_REGEX);
		Assert.assertTrue("Could not match string with at least a digit", util.matcher());
	}

	@Test
	public void shouldEvaluateToTrueForStringWithDigitsAndSpecialChars(){
		RegexUtil util = new RegexUtil("thisis12something@#", RegexUtil.MUST_CONTAIN_DIGIT_REGEX);
		Assert.assertTrue("Could not match string with a digit and special chars", util.matcher());
	}

	@Test
	public void shouldEvaluateToFalseForStringWithoutNoneDigit(){
		RegexUtil util = new RegexUtil("12345678", RegexUtil.MUST_CONTAIN_NONE_DIGIT_REGEX);
		Assert.assertFalse("String should have at least a none-digit", util.matcher());
	}

	@Test
	public void shouldEvaluateToTrueForStringWithAtLeastANoneDigit(){
		RegexUtil util = new RegexUtil("12345678thisfollowsdigits", RegexUtil.MUST_CONTAIN_NONE_DIGIT_REGEX);
		Assert.assertTrue("Should pass for string with a none-digit", util.matcher());
	}

	@Test
	public void shouldEvaluateToTrueForStringWithBothCases(){
		RegexUtil util = new RegexUtil("MustHaveThis", RegexUtil.MUST_CONTAIN_MIXED_CASES_REGEX);
		Assert.assertTrue("Passes for string with lower and upper case characters", util.matcher());
	}

	@Test
	public void shouldEvaluateToFalseForStringWithLowerCaseOnly(){
		RegexUtil util = new RegexUtil("thisisalllowercase", RegexUtil.MUST_CONTAIN_MIXED_CASES_REGEX);
		Assert.assertFalse("Expects string with both upper and lower cases. You have only lower case", util.matcher());
	}

	@Test
	public void shouldEvaluateToFalseForStringWithUpperCaseOnly(){
		RegexUtil util = new RegexUtil("THISISALLUPPERCASE", RegexUtil.MUST_CONTAIN_MIXED_CASES_REGEX);
		Assert.assertFalse("Expects string with both upper and lower cases. You have only Upper case", util.matcher());
	}


}