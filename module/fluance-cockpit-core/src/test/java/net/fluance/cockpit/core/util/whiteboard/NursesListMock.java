package net.fluance.cockpit.core.util.whiteboard;

import java.util.ArrayList;
import java.util.List;

public class NursesListMock {
	
	private NursesListMock() {}
	
	public static List<String> getList(int howMany){
		List<String> nurses = new ArrayList<String>();
		int i = 0;
		
		while(i < howMany) {
			nurses.add("" + i);
			i++;
		}
		
		return nurses;
	}

}
