package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class PatientAccessLogOrderByEnumTests {
	@Test
	public void test_get_value() {
		String value = PatientAccessLogOrderByEnum.APPUSER.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("appuser"));
	}
	
	@Test
	public void test_getValueWithSortOrder_sort_order_appuser() {
		String value = PatientAccessLogOrderByEnum.APPUSER.getValueWithSortOrder("ASC");
		Assert.assertTrue("Contains order?", value.contains("ASC"));
		Assert.assertTrue("Contains firstname?", value.contains("firstname"));
	}
	
	@Test
	public void test_getValueWithSortOrder_sort_order_logdate() {
		String value = PatientAccessLogOrderByEnum.LOGDATE.getValueWithSortOrder("ASC");
		Assert.assertTrue("Contains order?", value.contains("ASC"));
		Assert.assertTrue("Contains firstname?", value.contains("logdt"));
	}
	
	@Test
	public void test_getValueWithSortOrder_sort_order_lastname() {
		String value = PatientAccessLogOrderByEnum.LASTNAME.getValueWithSortOrder("ASC");
		Assert.assertTrue("Contains order?", value.contains("ASC"));
	}
	

	@Test
	public void test_set_value_not_exist() {
		PatientAccessLogOrderByEnum orderByEnum = PatientAccessLogOrderByEnum.permissiveValueOf("hello");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_set_value_exist() {
		PatientAccessLogOrderByEnum orderByEnum = PatientAccessLogOrderByEnum.permissiveValueOf("lastname");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("lastname"));
	}
	
	@Test
	public void test_set_value_null() {
		PatientAccessLogOrderByEnum orderByEnum = PatientAccessLogOrderByEnum.permissiveValueOf(null);
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
}
