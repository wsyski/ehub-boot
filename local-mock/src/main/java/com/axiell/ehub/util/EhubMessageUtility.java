package com.axiell.ehub.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.FilenameUtils.removeExtension;

@Slf4j
@Component
public class EhubMessageUtility {
    public static final String EXCEPTION = "exception";
    private static final String FILE_SUFFIX = ".json";
    private static final String KEY_SEPARATOR = "-";
    @Autowired
    private ServletContext servletContext;

    private String getResponseDir() {
        final String responseDir = System.getProperty("com.axiell.ehub.responseDir");
        if (StringUtils.isBlank(responseDir)) {
            return servletContext.getRealPath("/") + File.separator + "WEB-INF" + File.separator + "config" + File.separator + "default";
        } else {
            return responseDir;
        }
    }

    public <T> T getEhubMessage(final Class<T> clazz, final String... fileNamePart) {
        final List<String> fileNames = getPossibleFileNames((String[]) fileNamePart);
        final File file = getEhubMessageFile(fileNames);
        if (file != null) {
            try {
                byte[] encoded = Files.readAllBytes(file.toPath());
                String json = new String(encoded, Charset.forName("UTF-8"));
                ObjectMapper mapper = new ObjectMapper();

                if (removeExtension(file.getName()).endsWith(EXCEPTION)) {
                    EhubJsonException jsonError = mapper.readValue(json, EhubJsonException.class);
                    throw jsonError.toException();
                }

                return mapper.readValue(json, clazz);
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

    /**
     * Creates a list of possible file names based on the fileNameParts.
     *
     * @param fileNamePart parts that constructs the name of the eHUB response file
     * @return a {@link List} with possible file names sorted with the longest (most specific) first
     */
    private List<String> getPossibleFileNames(String... fileNamePart) {
        List<String> fileNameParts = Lists.newArrayList(fileNamePart);
        fileNameParts.add(EXCEPTION);
        final List<String> fileNames = new ArrayList<String>();
        final StringBuilder fileName = new StringBuilder();
        for (String part : fileNameParts) {
            if (part != null) {
                if (part.toLowerCase().endsWith("elib")) {
                    part = "elib";
                }
                if (fileName.length() > 0) {
                    fileName.append(KEY_SEPARATOR);
                }
                fileName.append(part);
                fileNames.add(0, fileName.toString());
            }
        }
        return fileNames;
    }

    private File getEhubMessageFile(final List<String> fileNames) {
        String responseDir = getResponseDir();
        for (String fileName : fileNames) {
            log.info("Look for file: " + fileName);
            final File file = new File(responseDir + File.separator + fileName.toLowerCase() + FILE_SUFFIX);
            if (file.exists() && file.isFile()) {
                log.info("Found file: " + fileName);
                return file;
            }
        }
        return null;
    }
}
