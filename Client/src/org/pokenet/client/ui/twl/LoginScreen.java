package org.pokenet.client.ui.twl;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.Translator;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.renderer.DynamicImage;
import de.matthiasmann.twl.renderer.DynamicImage.Format;
import de.matthiasmann.twl.renderer.Renderer;


/**
 * The complete login screen
 * @author Myth1c
 *
 */

public class LoginScreen extends DesktopArea {
	private LanguageDialog m_languageDialog;
	private AboutDialog m_about;
	
	private Label m_serverRev, m_clientRev;
	private Button m_openAbout;
	private Button m_openToS;
	
	
	public LoginScreen() {
		setSize(800,600);
		setPosition(0, 0);
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		
		List<String> translated = new ArrayList<String>();
		translated = Translator.translate("_LOGIN");
		
		loadBackground(respath);
		m_languageDialog = new LanguageDialog();
		add(m_languageDialog);
		
		m_about = new AboutDialog();
		add(m_about);
		
		m_openAbout = new Button(translated.get(3));
		m_openAbout.setVisible(false);
		m_openAbout.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				showAbout();
			}
		});
		add(m_openAbout);

		m_openToS = new Button(translated.get(4));
		m_openToS.setVisible(false);
		m_openToS.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				showToS();
			}
		});
		add(m_openToS);
		
		setClientRevision();
		m_serverRev = new Label("Server Version: ?");
		m_serverRev.setVisible(true);
		add(m_serverRev);
	}
	
	@Override
	public void layout() {
		m_clientRev.setPosition(4, 600 - m_clientRev.getHeight() - 10);
		m_serverRev.setPosition(m_clientRev.getWidth() + m_clientRev.computeTextWidth() + 16, m_clientRev.getY());
		m_openAbout.setSize(64, 32);
		m_openAbout.setPosition(800 - 64 - 8, 8);
		m_openToS.setSize(64, 32);
		m_openToS.setPosition(800 - 64 - 8, 40);
	}
	
	private void loadBackground(String respath) 
	{
		Renderer renderer = TWLTest.getInstance().getRenderer();
		DynamicImage img = renderer.createDynamicImage(800, 600);
		String backgroundPath = "";
		/* Load the background image, NOTE: Months start at 0, not 1 */
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.MONTH) == 1)
		{
			if(cal.get(Calendar.DAY_OF_MONTH) >= 7 && cal.get(Calendar.DAY_OF_MONTH) <= 14)
				backgroundPath = respath + "res/pokenet_valentines.png";
			else
				backgroundPath = respath + "res/pokenet_venonat.png";
		}
		else if(cal.get(Calendar.MONTH) == 2 && cal.get(Calendar.DAY_OF_MONTH) > 14)
			/* If second half of March, show Easter login */
			backgroundPath = respath + "res/pokenet_easter.png";
		else if(cal.get(Calendar.MONTH) == 3 && cal.get(Calendar.DAY_OF_MONTH) < 26)
			/* If before April 26, show Easter login */
			backgroundPath = respath + "res/pokenet_easter.png";
		else if(cal.get(Calendar.MONTH) == 9)
			/* Halloween */
			backgroundPath = respath + "res/pokenet_halloween.png";
		else if(cal.get(Calendar.MONTH) == 11)
			/* Christmas! */
			backgroundPath = respath + "res/pokenet_xmas.png";
		else if(cal.get(Calendar.MONTH) == 0)
			/* January - Venonat Time! */
			backgroundPath = respath + "res/pokenet_venonat.png";
		else if(cal.get(Calendar.MONTH) >= 5 && cal.get(Calendar.MONTH) <= 7)
			/* Summer login */
			backgroundPath = respath + "res/pokenet_summer.png";
		else
			/* Show normal login screen */
			backgroundPath = respath + "res/pokenet_normal.png";
		
		img.update(FileLoader.loadPNG(backgroundPath), Format.RGBA);
		setBackground(img);
	}

	/**
	 * Sets the server version to be displayed
	 * 
	 * @param rev
	 */
	public void setServerRevision(int rev)
	{
		m_serverRev.setText("Server Version: r" + rev);
		m_serverRev.setPosition(m_clientRev.getX() + m_clientRev.getWidth() + 16, m_clientRev.getY());
	}
	
	/**
	 * Displays client version (ThinClient Version) based on rev.txt If rev.txt is not found, ? is displayed
	 */
	private void setClientRevision()
	{
		String path = System.getProperty("res.path");
		if(path == null || path.equalsIgnoreCase("NULL"))
			path = "./";
		File f = new File(path + "/.svn/entries");

		try
		{
			if(f.exists())
			{
				Scanner s = new Scanner(f);
				s.nextLine();
				s.nextLine();
				s.nextLine();

				m_clientRev = new Label("Client Version: svn:" + s.nextLine());
				f = new File(path + "rev.txt");
				s.close();
				s = new Scanner(f);
				m_clientRev.setText(m_clientRev.getText() + " // rev:" + s.nextLine());
				s.close();

			}
			else
			{
				f = new File(path + "rev.txt");
				Scanner s;
				s = new Scanner(f);

				m_clientRev = new Label("Client Version: r" + s.nextLine());
				s.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			m_clientRev = new Label("Client Version: ?");
		}
		this.add(m_clientRev);
	}
	
	/**
	 * Shows about dialog
	 */
	public void showAbout()
	{
		m_about.reloadStrings();
		m_about.setVisible(true);
	}
	
	/**
	 * Shows the terms of service dialog
	 */
	public void showToS()
	{
		//m_terms.reloadStrings();
		//m_terms.setVisible(true);
	}
	
	/**
	 * Shows the server selection dialog
	 */
	public void showServerSelect()
	{
		//m_register.setVisible(false);
		//m_login.setVisible(false);
		//m_select.reloadStrings();
		//m_select.setVisible(true);
		m_openAbout.setVisible(false);
		m_openToS.setVisible(false);
		m_languageDialog.setVisible(false);
	}
	
	/**
	 * Shows the registration dialog
	 */
	public void showRegistration()
	{
		//m_select.setVisible(false);
		//m_login.setVisible(false);
		m_openAbout.setVisible(true);
		m_openToS.setVisible(true);
		m_languageDialog.setVisible(false);
		//m_register.reloadStrings();
		//m_register.setVisible(true);
		//m_register.grabFocus();
	}
	
	/**
	 * Shows the login dialog
	 */
	public void showLogin()
	{
		//m_login.reloadStrings();
		//m_select.setVisible(false);
		//m_register.setVisible(false);
		//m_login.setVisible(true);
		m_openAbout.setVisible(true);
		m_openToS.setVisible(true);
		//m_login.getLoginButton().setEnabled(true);
		m_languageDialog.setVisible(false);
	}
	
	/**
	 * Shows the server selection dialogs
	 */
	public void showLanguageSelect()
	{
		//m_register.setVisible(false);
		//m_login.setVisible(false);
		//m_select.setVisible(false);
		m_languageDialog.setVisible(true);
		m_openAbout.setVisible(false);
		m_openToS.setVisible(false);
	}
}
