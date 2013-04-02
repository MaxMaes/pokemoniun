package org.pokenet.client.ui.frames;

import java.util.List;
import mdes.slick.sui.Frame;
import mdes.slick.sui.TextArea;
import org.newdawn.slick.Color;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.Translator;

/**
 * A window with information about the game
 * 
 * @author shadowkanji
 */
public class AboutDialog extends Frame
{
	private Color m_bg, m_white;
	private TextArea m_info;

	/**
	 * Default constructor
	 */
	public AboutDialog()
	{
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
		m_bg = new Color(0, 0, 0, 140);
		m_white = new Color(255, 255, 255);
		List<String> translated = Translator.translate("_LOGIN");
		setTitle(translated.get(34));
		this.setLocation(128, 256);
		setBackground(m_bg);
		setResizable(false);

		m_info = new TextArea();
		m_info.setSize(280, 320);
		m_info.setLocation(4, 4);
		m_info.setWrapEnabled(true);
		m_info.setText("This is a small test message. It's not too long.\n" +
                "\tThis is a small test message. It's not too long.\n" +
                "This\tis a small test message. It's not too long.\n" +
                "This is\ta small test message. It's not too long.\n" +
                "This is a\tsmall test message. It's not too long.\n" +
                "This is a small\ttest message. It's not too long.\n" +
                "This is a small test\tmessage. It's not too long.\n" +
                "This is a small test message.\tIt's not too long.\n" +
                "This is a small test message. It's\tnot too long.\n" +
                "This is a small test message. It's not\ttoo long.\n" +
                "This is a small test message. It's not too\tlong.");
		m_info.setFont(GameClient.getInstance().getFontSmall());
		m_info.setBackground(m_bg);
		m_info.setForeground(m_white);
		this.add(m_info);

		this.setSize(288, 320);

		setVisible(false);
	}

	public void reloadStrings()
	{
		List<String> translated = Translator.translate("_LOGIN");
		setTitle(translated.get(34));
		m_info.setText(translated.get(35) + "\n" + translated.get(36) + "\n" + translated.get(37) + "\n" + translated.get(38) + "\n" + translated.get(39) + "\n");
	}
}
