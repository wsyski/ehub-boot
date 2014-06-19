package com.axiell.ehub.support;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ResourceLink;

import java.io.File;

class PlainTextFileLink extends ResourceLink<Void> {

    PlainTextFileLink(final String id, final File file) {
        super(id, new PlainTextFileResource(file));
        addLabel(file);
    }

    private void addLabel(File file) {
        final String fileName = file.getName();
        final Label fileLabel = new Label("fileName", fileName);
        add(fileLabel);
    }
}
