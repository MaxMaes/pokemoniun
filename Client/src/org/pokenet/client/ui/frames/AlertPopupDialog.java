package org.pokenet.client.ui.frames;

import mdes.slick.sui.Button;
import mdes.slick.sui.Container;
import mdes.slick.sui.Label;
import mdes.slick.sui.event.ActionListener;
import org.pokenet.client.GameClient;

public class AlertPopupDialog extends mdes.slick.sui.Frame
{
	private Button m_okebutton;
	
	/**
	 * Default Constructor
	 * 
	 * @param text
	 */

	public AlertPopupDialog(String title, String text)
	{
		super(title);
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
		Container m_label = new Container();
		String[] m_lines = text.split("\n");

		int maxWidth = 0;
		int maxHeight = 0;

		for(String s : m_lines)
		{
			Label line = new Label(s);
			line.pack();

			int lineWidth = (int) line.getWidth();
			int lineHeight = (int) line.getHeight();

			if(lineWidth > maxWidth)
				maxWidth = lineWidth;

			line.setY(maxHeight);
			maxHeight += lineHeight;

			m_label.add(line);
		}
		m_label.setSize(maxWidth, maxHeight);

		m_okebutton = new Button();

		m_okebutton.setText("Oke"); //TODO: Translated.get(*)
		m_okebutton.setSize(50, 25);
		m_okebutton.setY(m_label.getY() + m_label.getHeight() + 20);


		getContentPane().add(m_label);
		getContentPane().add(m_okebutton);

		m_label.setLocation(5, 15);

		setResizable(false);
		this.setSize(m_label.getWidth() + 10, m_label.getHeight() + 80);
		m_okebutton.setX(getWidth() / 2 - 105 / 2);

		setCenter();
		setVisible(true);
		GameClient.getInstance().getDisplay().add(this);
		setAlwaysOnTop(true);
	}

	/**
	 * Constructor
	 * 
	 * @param text
	 * @param yes
	 * @param no
	 */
	public AlertPopupDialog(String title, String text, ActionListener yes)
	{
		this(title, text);
		addYesListener(yes);
	}
	/**
	 * Sets the Yes action
	 */
	public void addYesListener(ActionListener yes)
	{
		m_okebutton.addActionListener(yes);
	}

	/**
	 * Centers the frame
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getDisplay().getHeight();
		int width = (int) GameClient.getInstance().getDisplay().getWidth();
		int x = width / 2 - (int) getWidth() / 2;
		int y = height / 2 - (int) getHeight() / 2;
		this.setLocation(x, y);
	}
}
