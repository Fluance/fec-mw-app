package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class NoteListOrderByEnumTests {
	@Test
	public void test_get_value() {
		String value = NoteListOrderByEnum.TITLE.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("title"));
	}

	@Test
	public void test_set_value_not_exist() {
		NoteListOrderByEnum orderByEnum = NoteListOrderByEnum.permissiveValueOf("hello");
		Assert.assertTrue("Is function getValue working?",orderByEnum == null);
	}
	
	@Test
	public void test_set_value_exist() {
		NoteListOrderByEnum orderByEnum = NoteListOrderByEnum.permissiveValueOf("title");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("title"));
	}
	
	@Test
	public void test_set_value_null() {
		NoteListOrderByEnum orderByEnum = NoteListOrderByEnum.permissiveValueOf(null);
		Assert.assertTrue("Is function getValue working?", orderByEnum == null);
	}
}
