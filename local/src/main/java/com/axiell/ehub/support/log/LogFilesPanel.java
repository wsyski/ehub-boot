package com.axiell.ehub.support.log;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import java.io.File;
import java.util.List;

class LogFilesPanel extends BreadCrumbPanel {

    LogFilesPanel(String id, IBreadCrumbModel breadCrumbModel) {
        super(id, breadCrumbModel);
        addLogFileListView();
    }

    @Override
    public String getTitle() {
        return getString("txtBreadCrumbPanelTitle");
    }

    private void addLogFileListView() {
        final List<File> logFiles = LogFilesRetriever.getFiles();
        final ListView<File> filesView = new ListView<File>("files", logFiles) {
            @Override
            protected void populateItem(ListItem<File> item) {
                final File file = item.getModelObject();
                final PlainTextFileLink fileLink = new PlainTextFileLink("fileLink", file);
                item.add(fileLink);
            }
        };
        add(filesView);
    }
}
