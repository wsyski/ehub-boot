package com.axiell.ehub.version;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.eekboom.utils.Strings;

class ReleaseComparator implements Comparator<Release> {
    private static final Locale ENGLISH = Locale.ENGLISH;
    private final Comparator<String> comparator;
    
    ReleaseComparator() {
	final Collator collator = Collator.getInstance(ENGLISH);
        comparator = Strings.getNaturalComparator(collator);
    }

    @Override
    public int compare(final Release release1, final Release release2) {
	final String version1 = release1.getVersion();
	final String version2 = release2.getVersion();
	return comparator.compare(version1, version2);
    }
}
