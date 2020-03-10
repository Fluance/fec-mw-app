package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class PatientListFoodEnumTests {
	@Test
	public void test_get_value() {
		String value = PatientListFoodEnum.DD.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("DD"));
	}

	@Test
	public void test_set_value_not_exist() {
		PatientListFoodEnum orderByEnum = PatientListFoodEnum.permissiveValueOf("hello");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_set_value_exist() {
		PatientListFoodEnum orderByEnum = PatientListFoodEnum.permissiveValueOf("DD");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("DD"));
	}

	@Test
	public void test_set_value_null() {
		PatientListFoodEnum orderByEnum = PatientListFoodEnum.permissiveValueOf(null);
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_get_values() {
		int size = PatientListFoodEnum.values().length;
		Assert.assertTrue("Is function getValue working?", size > 0);
	}
	
}
