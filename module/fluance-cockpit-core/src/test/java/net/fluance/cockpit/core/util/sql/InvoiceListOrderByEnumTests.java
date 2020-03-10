package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class InvoiceListOrderByEnumTests {
	@Test
	public void test_get_value() {
		String value = InvoiceListOrderByEnum.ID.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("id"));
	}

	@Test
	public void test_set_value_not_exist() {
		InvoiceListOrderByEnum orderByEnum = InvoiceListOrderByEnum.permissiveValueOf("hello");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
	
	@Test
	public void test_set_value_exist() {
		InvoiceListOrderByEnum orderByEnum = InvoiceListOrderByEnum.permissiveValueOf("id");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("id"));
	}
	
	@Test
	public void test_set_value_null() {
		InvoiceListOrderByEnum orderByEnum = InvoiceListOrderByEnum.permissiveValueOf(null);
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals(""));
	}
}
