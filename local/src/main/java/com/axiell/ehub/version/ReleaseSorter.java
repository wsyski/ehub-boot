package com.axiell.ehub.version;

import java.util.Collections;
import java.util.List;

final class ReleaseSorter {
    
    private ReleaseSorter() {	
    }

    static void reverseSort(final List<Release> releases) {
	final ReleaseComparator comparator = new ReleaseComparator();
	Collections.sort(releases, comparator);
	Collections.reverse(releases);
    }
}
