package org.pokenet.client.twl.ui;

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
	
	public void showLoginScreen() {
		loginScreen.loadBackground(getGUI());
		loginScreen.setVisible(true);
	}
	
	public void hideLoginScreen() {
		loginScreen.setVisible(false); //Hide the GUI elements
		getGUI().setBackground(null);  //Empty the background since the main game doesnt use TWL GUI background.
	}
	
	public LoginScreen getLoginScreen() {
		return loginScreen;
	}
	
	public void showLanguageSelect() {
		loginScreen.showLanguageSelect();
	}

	public void showServerSelect() {
		
	}
}
