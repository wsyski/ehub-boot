package com.axiell.ehub.provider.record.format;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

class ContentDispositionsModel extends LoadableDetachableModel<List<ContentDisposition>> {

    @Override
    protected List<ContentDisposition> load() {
        return Arrays.asList(ContentDisposition.values());
    }
}