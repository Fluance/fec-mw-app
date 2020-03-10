package net.fluance.cockpit.core.util;

import org.springframework.data.domain.PageRequest;

public class PageUtils {
	private PageUtils() {}
	
	public static PageRequest getPage(int page, int limit) {
		if (page > 0) {
			return new PageRequest(page - 1, limit);
		} else {
			return new PageRequest(0, limit);
		}
	}
}