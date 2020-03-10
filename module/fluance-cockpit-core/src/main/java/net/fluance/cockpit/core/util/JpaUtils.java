package net.fluance.cockpit.core.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class JpaUtils {
	private JpaUtils() {}
	
	public static Pageable createPageableFromLimitAndOffset(Integer limit, Integer offset) {
		if(limit == null || limit <= 0) {
			throw new IllegalArgumentException("\"limit\" must be not null and be a value greatter than 0"); 
		}
		if(offset == null || offset < 0) {
			throw new IllegalArgumentException("\"offset\" must be not null and be 0 or higher");
		}
		
		return  new PageRequest(offset/limit, limit);
	}

}
