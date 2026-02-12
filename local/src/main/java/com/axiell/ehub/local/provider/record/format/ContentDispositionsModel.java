package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.provider.record.format.ContentDisposition;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.Arrays;
import java.util.List;

class ContentDispositionsModel extends LoadableDetachableModel<List<ContentDisposition>> {

    @Override
    protected List<ContentDisposition> load() {
        return Arrays.asList(ContentDisposition.values());
    }
}
