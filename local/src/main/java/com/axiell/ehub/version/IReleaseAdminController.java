package com.axiell.ehub.version;

public interface IReleaseAdminController {

    Release getLatestDatabaseRelease() throws EmptyReleaseTableException;    
}
