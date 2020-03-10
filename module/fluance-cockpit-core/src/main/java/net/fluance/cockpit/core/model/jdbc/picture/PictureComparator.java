package net.fluance.cockpit.core.model.jdbc.picture;

import java.util.Comparator;

public class PictureComparator implements Comparator<PictureDetail>{

	@Override
	public int compare(PictureDetail picture1, PictureDetail picture2) {
		return picture1.getOrder() - picture2.getOrder();
	}

}
