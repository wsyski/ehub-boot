package com.axiell.ehub.support.log;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;

class LogFilesRetriever {

    private LogFilesRetriever() {
    }

    static List<File> getFiles() {
        final File logFilesFolder = new File(System.getProperty("catalina.base") + File.separator + "logs");
        if (logFilesFolder.exists()) {
            final File[] files = logFilesFolder.listFiles();
            return Lists.newArrayList(files);
        }
        return Lists.newArrayList();
    }
}
