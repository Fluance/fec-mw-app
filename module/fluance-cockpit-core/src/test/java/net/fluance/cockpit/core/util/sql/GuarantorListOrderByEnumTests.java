package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class GuarantorListOrderByEnumTests {
	@Test
	public void test_get_value() {
		String value = GuarantorListOrderByEnum.ID.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("id"));
	}

	@Test
	public void test_set_value_not_exist() {
		GuarantorListOrderByEnum orderByEnum = GuarantorListOrderByEnum.permissiveValueOf("hello");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_set_value_exist() {
		GuarantorListOrderByEnum orderByEnum = GuarantorListOrderByEnum.permissiveValueOf("id");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("id"));
	}
	
	@Test
	public void test_set_value_null() {
		GuarantorListOrderByEnum orderByEnum = GuarantorListOrderByEnum.permissiveValueOf(null);
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
}
