package com.axiell.ehub.support.log;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class LogFilesRetriever {

    private LogFilesRetriever() {
    }

    static List<File> getFiles() {
        final File logFilesFolder = new File(System.getProperty("catalina.base") + File.separator + "logs");
        if (logFilesFolder.exists()) {
            final File[] files = logFilesFolder.listFiles();
            final List<File> fileList = Lists.newArrayList(files);
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            return fileList;
        }
        return Lists.newArrayList();
    }
}
