package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class PatientListOrderByEnumTests {
	@Test
	public void test_get_value() {
		String value = PatientListOrderByEnum.ADMITDT.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("admitdt"));
	}

	@Test
	public void test_set_value_not_exist() {
		PatientListOrderByEnum orderByEnum = PatientListOrderByEnum.permissiveValueOf("hello");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_set_value_exist() {
		PatientListOrderByEnum orderByEnum = PatientListOrderByEnum.permissiveValueOf("admitdt");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("admitdt"));
	}
	
	@Test
	public void test_set_value_null() {
		PatientListOrderByEnum orderByEnum = PatientListOrderByEnum.permissiveValueOf(null);
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
}
