package org.pokenet.client.ui.twl;

import de.matthiasmann.twl.DesktopArea;

/**
 * This is the main GUI file. 
 * All components and their show/hide functions will be added here.
 * @author Myth1c
 *
 */
public class GUIPane extends DesktopArea {
	private LoginScreen loginScreen;
	
	public GUIPane() {
		loginScreen = new LoginScreen();
		add(loginScreen);
		setTheme("guipane");
	}
	
	public LoginScreen getLoginScreen() {
		return loginScreen;
	}
	
	public void showLanguageSelect() {
		loginScreen.showLanguageSelect();
	}
}
