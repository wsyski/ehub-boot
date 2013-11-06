package com.axiell.ehub.language;

import java.io.Serializable;

class LanguagesMediator implements Serializable {
    private LanguagesPanel languagesPanel;
    
    void registerLanguagesPanel(LanguagesPanel languagesPanel) {
	this.languagesPanel = languagesPanel;
    }

    void afterDeleteLanguage() {
	languagesPanel.activate(languagesPanel);
    }

    void afterSaveLanguage() {
	languagesPanel.activate(languagesPanel);
    }
}
