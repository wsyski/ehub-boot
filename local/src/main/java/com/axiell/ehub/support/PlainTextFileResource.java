package com.axiell.ehub.support;

import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.protocol.http.WebResponse;

import java.io.File;

class PlainTextFileResource extends DynamicWebResource {
    private final File file;

    PlainTextFileResource(File file) {
        this.file = file;
    }

    @Override
    protected ResourceState getResourceState() {
        return new PlainTextFileResourceState(file);
    }

    @Override
    protected void setHeaders(final WebResponse response) {
        super.setHeaders(response);
        setContentDisposition(response);
    }

    private void setContentDisposition(final WebResponse response) {
        final String fileName = file.getName();
        response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
    }
}
