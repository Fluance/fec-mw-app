package net.fluance.cockpit.core.util.sql;

import org.junit.Assert;
import org.junit.Test;

public class NoteListSortOrderEnumTests {
	@Test
	public void test_get_value() {
		String value = NoteListSortOrderEnum.ASC.getValue();
		Assert.assertTrue("Is function getValue working?", value.equals("asc"));
	}

	@Test
	public void test_set_value_not_exist() {
		NoteListSortOrderEnum orderByEnum = NoteListSortOrderEnum.permissiveValueOf("hello");
		Assert.assertTrue("Is function getValue working?",orderByEnum == null);
	}
	
	@Test
	public void test_set_value_exist() {
		NoteListSortOrderEnum orderByEnum = NoteListSortOrderEnum.permissiveValueOf("asc");
		String result = orderByEnum.getValue();
		Assert.assertTrue("Is function getValue working?", result.equals("asc"));
	}
	
	@Test
	public void test_set_value_null() {
		NoteListSortOrderEnum orderByEnum = NoteListSortOrderEnum.permissiveValueOf(null);
		Assert.assertTrue("Is function getValue working?", orderByEnum == null);
	}
}
