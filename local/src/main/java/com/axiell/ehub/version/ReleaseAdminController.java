package com.axiell.ehub.version;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

public class ReleaseAdminController implements IReleaseAdminController {
    
    @Autowired(required = true)
    private IReleaseRepository releaseRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Release getLatestDatabaseRelease() throws EmptyReleaseTableException {
	final List<Release> releases = findAllReleases();
	if (releases.isEmpty()) {
	    throw new EmptyReleaseTableException();
	}
	ReleaseSorter.reverseSort(releases);
	final Iterator<Release> releaseIterator = releases.iterator();
        return releaseIterator.next();
    }

    private List<Release> findAllReleases() {
	final Iterable<Release> releases = releaseRepository.findAll();
	return Lists.newArrayList(releases);
    }
}
