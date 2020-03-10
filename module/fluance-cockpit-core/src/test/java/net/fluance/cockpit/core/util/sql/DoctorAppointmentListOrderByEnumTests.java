package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class DoctorAppointmentListOrderByEnumTests {
	@Test
	public void test_get_value() {
		String value = DoctorAppointmentListOrderByEnum.APPOINT_ID.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("appointment_id"));
	}

	@Test
	public void test_set_value_not_exist() {
		DoctorAppointmentListOrderByEnum orderByEnum = DoctorAppointmentListOrderByEnum.permissiveValueOf("hello");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_set_value_exist() {
		DoctorAppointmentListOrderByEnum orderByEnum = DoctorAppointmentListOrderByEnum.permissiveValueOf("APPOINT_ID");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("appointment_id"));
	}
	
	@Test
	public void test_set_value_null() {
		DoctorAppointmentListOrderByEnum orderByEnum = DoctorAppointmentListOrderByEnum.permissiveValueOf(null);
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
}
