package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class PatientAccessLogShortOrderEnumTest {
	@Test
	public void test_get_value() {
		String value = PatientAccessLogShortOrderEnum.DESC.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("desc"));
	}

	@Test
	public void test_set_value_not_exist() {
		PatientAccessLogShortOrderEnum orderByEnum = PatientAccessLogShortOrderEnum.permissiveValueOf("foo");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_set_value_exist() {
		PatientAccessLogShortOrderEnum orderByEnum = PatientAccessLogShortOrderEnum.permissiveValueOf("desc");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("desc"));
	}

	@Test
	public void test_set_value_null() {
		PatientAccessLogShortOrderEnum orderByEnum = PatientAccessLogShortOrderEnum.permissiveValueOf(null);
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_get_values() {
		int size = PatientAccessLogShortOrderEnum.values().length;
		Assert.assertTrue("Is function getValue working?", size > 0);
	}
}
