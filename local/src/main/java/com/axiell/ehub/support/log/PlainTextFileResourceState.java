package com.axiell.ehub.support.log;

import org.apache.commons.io.IOUtils;

import java.io.*;

import static org.apache.wicket.markup.html.DynamicWebResource.ResourceState;

class PlainTextFileResourceState extends ResourceState {
    private final File file;

    PlainTextFileResourceState(final File file) {
        this.file = file;
    }

    @Override
    public byte[] getData() {
        try (InputStream is = new FileInputStream(file)) {
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
