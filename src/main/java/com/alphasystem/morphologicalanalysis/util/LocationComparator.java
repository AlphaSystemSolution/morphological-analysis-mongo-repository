/**
 * 
 */
package com.alphasystem.morphologicalanalysis.util;

import com.alphasystem.morphologicalanalysis.wordbyword.model.Location;

import java.util.Comparator;

/**
 * @author sali
 * 
 */
public class LocationComparator implements Comparator<Location> {

	@Override
	public int compare(Location o1, Location o2) {
		int result = 0;
		if (o1 == null && o2 == null) {
			result = 0;
		} else if (o1 == null) {
			result = -1;
		} else if (o2 == null) {
			result = 1;
		} else {
			result = o1.getTokenNumber().compareTo(o2.getTokenNumber());
			if (result == 0) {
				Integer li1 = o1.getLocationNumber();
				Integer li2 = o2.getLocationNumber();
				result = li1.compareTo(li2);
			}
		}
		return result;
	}

}
