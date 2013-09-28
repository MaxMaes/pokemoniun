package org.pokemonium.client.ui;

import org.pokemonium.client.ui.frames.AlertDialog;
import org.pokemonium.client.ui.frames.ConfirmationDialog;
import de.matthiasmann.twl.DesktopArea;

/**
 * This is the main GUI file.
 * All components and their show/hide functions will be added here.
 * 
 * @author Myth1c
 */
public class GUIPane extends DesktopArea
{
	private LoginScreen loginScreen;
	private ConfirmationDialog confirmationdialog;
	// private AlertDialog alertDialog;
	private AlertDialog messageDialog;
	private LoadingScreen loadingScreen;

	private HUD hud;

	public GUIPane()
	{
		hud = new HUD();
		add(hud);
		hud.setVisible(false);

		loginScreen = new LoginScreen();
		add(loginScreen);

		confirmationdialog = new ConfirmationDialog("", this);

		// alertDialog = new AlertDialog("Alert!", "", loginScreen);

		messageDialog = new AlertDialog("Message!", "", loginScreen);

		loadingScreen = new LoadingScreen();
		add(loadingScreen);

		setTheme("guipane");
		setSize(800, 600);
	}

	public void showLoginScreen()
	{
		loginScreen.loadBackground(getGUI());
		loginScreen.setVisible(true);
	}

	public void hideLoginScreen()
	{
		loginScreen.setVisible(false); // Hide the GUI elements
		getGUI().setBackground(null);  // Empty the background since the main game doesnt use TWL GUI background.
	}

	public LoginScreen getLoginScreen()
	{
		return loginScreen;
	}

	public void showLanguageSelect()
	{
		loginScreen.showLanguageSelect();
	}

	public void showServerSelect()
	{
		loginScreen.showServerSelect();
	}

	public void showHUD()
	{
		hud.setVisible(true);
	}

	public HUD getHUD()
	{
		return hud;
	}

	public void hideHUD()
	{
		hud.setVisible(false);
	}

	/**
	 * functions for the confirmation dialog
	 */
	public void showConfirmationDialog(String text, Runnable yes, Runnable no)
	{
		confirmationdialog.setText(text);
		confirmationdialog.setYesListener(yes);
		confirmationdialog.setNoListener(no);
		confirmationdialog.setVisible(true);
	}

	public void hideConfirmationDialog()
	{
		confirmationdialog.setVisible(false);
	}

	public ConfirmationDialog getConfirmationDialog()
	{
		return confirmationdialog;
	}

	// /**
	// * functions for the alert dialog
	// */
	// public void showAlertDialog(String title, String text, Runnable ok)
	// {
	// alertDialog.setText(text);
	// alertDialog.setTitle(title);
	// alertDialog.addOkListener(ok);
	// alertDialog.setVisible(true);
	// }
	//
	// public void hideAlertDialog()
	// {
	// alertDialog.setVisible(false);
	// }
	//
	// public AlertDialog getAlertDialog()
	// {
	// return alertDialog;
	// }

	/**
	 * functions for the message dialog
	 */
	public void showMessageDialog(String text, Runnable ok)
	{
		messageDialog.setText(text);
		messageDialog.setOkListener(ok);
		messageDialog.setVisible(true);
	}

	public void hideMessageDialog()
	{
		messageDialog.setVisible(false);
	}

	public AlertDialog getMessageDialog()
	{
		return messageDialog;
	}

	public void hideLoadingScreen()
	{
		loadingScreen.setVisible(false);
	}

	public void showLoadingScreen()
	{
		loadingScreen.setVisible(true);
	}

	public boolean isLoadingScreenVisible()
	{
		return loadingScreen.isVisible();
	}
}
