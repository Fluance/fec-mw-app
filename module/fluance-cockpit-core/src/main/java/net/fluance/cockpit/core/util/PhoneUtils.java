package net.fluance.cockpit.core.util;

public class PhoneUtils {

	private PhoneUtils() {}
	
	public static String getCleanPhoneNumber(String phone) {
		return removeAllSpaces(getOnlyNumbers(phone));
	}
	
	private static String getOnlyNumbers(String str) {
		return str.replaceAll("\\D+","");
	}
	private static String removeAllSpaces(String str) {
		return str.replaceAll("[\\s|\\u00A0]+", "");
	}
}
