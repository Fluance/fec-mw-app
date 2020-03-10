package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class BenefitListOrderByEnumTests {
	@Test
	public void test_get_value() {
		String value = BenefitListOrderByEnum.BENEFITDT.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("benefitdt"));
	}
	@Test
	public void test_set_value_not_exist() {
		BenefitListOrderByEnum orderByEnum = BenefitListOrderByEnum.permissiveValueOf("hello");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_set_value_exist() {
		BenefitListOrderByEnum orderByEnum = BenefitListOrderByEnum.permissiveValueOf("benefitdt");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("benefitdt"));
	}
	
	@Test
	public void test_set_value_null() {
		BenefitListOrderByEnum orderByEnum = BenefitListOrderByEnum.permissiveValueOf(null);
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
}
