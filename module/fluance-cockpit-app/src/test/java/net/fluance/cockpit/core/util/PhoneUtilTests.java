package net.fluance.cockpit.core.util;

import org.junit.Assert;
import org.junit.Test;

public class PhoneUtilTests {
	@Test
	public void getCleanPhoneNumber() {
		String phone = PhoneUtils.getCleanPhoneNumber("adsadsda07");
		Assert.assertTrue("Is phone correct?",phone.equals("07"));
	}
	
	@Test
	public void getCleanPhoneNumber_2() {
		String phone = PhoneUtils.getCleanPhoneNumber("adsadsda07asa");
		Assert.assertTrue("Is phone correct?",phone.equals("07"));
	}
	
	@Test
	public void getCleanPhoneNumber_3() {
		String phone = PhoneUtils.getCleanPhoneNumber("+41797001812");
		Assert.assertTrue("Is phone correct?",phone.equals("41797001812"));
	}
	@Test
	public void getCleanPhoneNumber_4() {
		String phone = PhoneUtils.getCleanPhoneNumber("+41 79 700 18 12");
		Assert.assertTrue("Is phone correct?",phone.equals("41797001812"));
	}
}
