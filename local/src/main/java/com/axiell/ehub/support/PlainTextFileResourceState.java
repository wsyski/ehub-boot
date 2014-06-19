package com.axiell.ehub.support;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.markup.html.DynamicWebResource;

import java.io.*;

class PlainTextFileResourceState extends DynamicWebResource.ResourceState {
    private final File file;

    PlainTextFileResourceState(final File file) {
        this.file = file;
    }

    @Override
    public byte[] getData() {
        try (InputStream is = new FileInputStream(file);) {
            return IOUtils.toByteArray(is);
        } catch (FileNotFoundException e) {
            return new byte[0];
        } catch (IOException e) {
            return new byte[0];
        }
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }
}
