package net.fluance.cockpit.core.util;

import java.util.Arrays;
import java.util.List;

/**
 * Sex Patient values available in DDBB
 *
 */
public enum Sex {

    FEMALE(new String[] {"f","Femminile","weiblich","Féminin"}),
    MALE(new String[] {"m","Masculin","männlich","Maschile"}),
    UNDEFINED(new String[]{});
	
	private List<String> sexNames;

	private Sex(String... sexNames) {
        this.sexNames = Arrays.asList(sexNames);
	}

	public List<String> getSexNames() {
		return sexNames;
	}
	
}
