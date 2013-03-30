package org.pokenet.client.ui.twl;

import de.matthiasmann.twl.DesktopArea;

public class GUIPane extends DesktopArea {
	private LanguageDialog languageDialog;
	
	public GUIPane() {
		languageDialog = new LanguageDialog();
		add(languageDialog);
		setTheme("guipane");
	}
}
