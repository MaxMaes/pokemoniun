package org.pokenet.client.twl.ui;

import org.pokenet.client.twl.ui.frames.AlertDialog;
import org.pokenet.client.twl.ui.frames.ConfirmationDialog;
import org.pokenet.client.twl.ui.frames.PlayerPopupDialog;

import de.matthiasmann.twl.DesktopArea;

/**
 * This is the main GUI file. 
 * All components and their show/hide functions will be added here.
 * @author Myth1c
 *
 */
public class GUIPane extends DesktopArea {
	private LoginScreen loginScreen;
	private ConfirmationDialog confirmationDialog;
	private AlertDialog alertDialog;
	private AlertDialog messageDialog;
	private PlayerPopupDialog playerPopupDialog;
	
	private HUD hud;
	
	public GUIPane() {
		loginScreen = new LoginScreen();
		add(loginScreen);
		
		confirmationDialog = new ConfirmationDialog("");
		confirmationDialog.setVisible(false);
		add(confirmationDialog);
		
		alertDialog = new AlertDialog("Alert!","");
		alertDialog.setVisible(false);
		add(alertDialog);
		
		messageDialog = new AlertDialog("Message!", "");
		messageDialog.setVisible(false);
		add(messageDialog);
		
		
		HUD hud = new HUD();
		add(hud);
		hud.setVisible(false);
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
		loginScreen.showServerSelect();
	}
	
	public void showHUD() {
		hud.setVisible(true);
	}
	
	/**
	 * functions for the confirmation dialog 
	 */
	public void showConfirmationDialog(String text, Runnable yes, Runnable no)
	{
		confirmationDialog.setText(text);
		confirmationDialog.addYesListener(yes);
		confirmationDialog.addNoListener(no);
		confirmationDialog.setVisible(true);
	}
	
	public void hideConfirmationDialog()
	{
		confirmationDialog.setText("");
		confirmationDialog.setVisible(false);
	}
	
	public ConfirmationDialog getConfirmationDialog()
	{
		return confirmationDialog;
	}
	
	/**
	 * functions for the alert dialog
	 */
	public void showAlertDialog(String title, String text, Runnable ok)
	{
		alertDialog.setText(text);
		alertDialog.setTitle(title);
		alertDialog.addOkListener(ok);
		alertDialog.setVisible(true);
	}
	
	public void hideAlertDialog()
	{
		alertDialog.setVisible(false);
		alertDialog.setText("");
		alertDialog.setTitle("Alert!");
	}
	
	public AlertDialog getAlertDialog()
	{
		return alertDialog;
	}
	
	/**
	 * functions for the message dialog
	 */
	public void showMessageDialog(String text, Runnable ok)
	{
		messageDialog.setText(text);
		messageDialog.addOkListener(ok);
		messageDialog.setVisible(true);
	}
	
	public void hideMessageDialog()
	{
		messageDialog.setVisible(false);
		messageDialog.setText("");
		messageDialog.setTitle("Message!");
	}
	
	public AlertDialog getMessageDialog()
	{
		return messageDialog;
	}
	
	/**
	 * functions for player popup
	 */
	public void createPlayerPopupDialog(String player)
	{
		playerPopupDialog = new PlayerPopupDialog(player);
		playerPopupDialog.setVisible(false);
		add(playerPopupDialog);
	}
	
	public PlayerPopupDialog getPlayerPopupDialog()
	{
		return playerPopupDialog;
	}
	
	public void destroyPlayerPopupDialog()
	{
		
	}
	
	public void showPlayerPopupDialogAt(int x, int y)
	{
		
	}
}
