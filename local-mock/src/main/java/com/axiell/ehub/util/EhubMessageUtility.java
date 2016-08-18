package com.axiell.ehub.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class EhubMessageUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(EhubMessageUtility.class);
    private static String ehubResponsesDir;
    private static int ehubMaxTimeout;
    private static final String FILE_SUFFIX = ".json";
    private static final String KEY_SEPARATOR = "-";

    /**
     * Private constructor that prevent direct instantiation.
     */
    public EhubMessageUtility() {
        configureEhubResponseDir();
    }

    private void configureEhubResponseDir() {
        final String ehubDir = System.getProperty("ehubDir");
        if (ehubDir == null || ehubDir.equals("")) {
            ehubResponsesDir = System.getProperty("catalina.base") + File.separator + "webapp"+ File.separator + "config"+ File.separator + "ehub";
        } else {
            ehubResponsesDir = ehubDir;
        }

        final String ehubTimeout = System.getProperty("ehubMaxTimeout");
        if (ehubTimeout == null || ehubTimeout.equals("")) {
            ehubMaxTimeout = 0;
        } else {
            ehubMaxTimeout = Integer.parseInt(ehubTimeout);
        }
        LOGGER.info("Using max timeout " + ehubMaxTimeout);
    }

    public <T> T getEhubMessage(Class<T> clazz, String... fileNamePart)  {
        final List<String> fileNames = getPossibleFileNames((String[]) fileNamePart);
        final File file = getEhubMessageFile(fileNames);
        try {
            byte[] encoded = Files.readAllBytes(file.toPath());
            String json = new String(encoded, Charset.forName("UTF-8"));
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        }
        catch(IOException ex) {
            LOGGER.error(ex.getMessage(),ex);
            return null;
        }
    }

    /**
     * Creates a list of possible file names based on the fileNameParts.
     *
     * @param fileNamePart parts that constructs the name of the eHUB response file
     * @return a {@link List} with possible file names sorted with the longest (most specific) first
     */
    private List<String> getPossibleFileNames(final String... fileNamePart) {
        final List<String> fileNames = new ArrayList<String>();
        final StringBuilder fileName = new StringBuilder();
        for (String part : fileNamePart) {
            if (part != null) {
                if (fileName.length() > 0) {
                    fileName.append(KEY_SEPARATOR);
                }
                fileName.append(part);
                fileNames.add(0, fileName.toString());
            }
        }
        return fileNames;
    }

    private static File getEhubMessageFile(final List<String> fileNames) {
        for (String fileName : fileNames) {
            LOGGER.info("Look for file: " + fileName);
            final File file = new File(ehubResponsesDir + File.separator + fileName + FILE_SUFFIX);
            if (file.exists() && file.isFile()) {
                LOGGER.info("Found file: " + fileName);
                return file;
            }
        }
        LOGGER.error("Could not find any matching response file");
        return null;
    }
}
