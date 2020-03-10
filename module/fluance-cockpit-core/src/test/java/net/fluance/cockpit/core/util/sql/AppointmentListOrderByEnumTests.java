package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class AppointmentListOrderByEnumTests {
	@Test
	public void test_get_value() {
		String value = AppointmentListOrderByEnum.APPOINT_ID.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("appoint_id"));
	}

	@Test
	public void test_set_value_not_exist() {
		AppointmentListOrderByEnum orderByEnum = AppointmentListOrderByEnum.permissiveValueOf("hello");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_set_value_exist() {
		AppointmentListOrderByEnum orderByEnum = AppointmentListOrderByEnum.permissiveValueOf("nb");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("nb"));
	}
	
	@Test
	public void test_set_value_null() {
		AppointmentListOrderByEnum orderByEnum = AppointmentListOrderByEnum.permissiveValueOf(null);
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
}
