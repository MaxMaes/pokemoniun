package org.pokenet.client.ui.base;

import mdes.slick.sui.Label;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

/**
 * ProgressBar used to show the health of pokemon in battle
 * 
 * @author ZombieBear
 */
public class ProgressBar extends Label
{
	private float m_maxVal;
	private float m_minVal;
	private float m_value;
	private boolean reversed;
	private Color textColor;

	/**
	 * Default constructor, default left to right
	 * 
	 * @param min
	 * @param max
	 */
	public ProgressBar(float m_min, float m_max)
	{
		super();
		m_value = m_maxVal;
		m_minVal = m_min;
		m_maxVal = m_max;
		reversed = false;
		setOpaque(true);
	}

	/**
	 * Constructor with option to reverse.
	 * 
	 * @param min
	 * @param max
	 * @param reversed false = left -> right, true = left <- right
	 */
	public ProgressBar(float m_min, float m_max, boolean reversed)
	{
		super();
		m_value = m_maxVal;
		m_minVal = m_min;
		m_maxVal = m_max;
		this.reversed = reversed;
		setOpaque(true);
	}

	/**
	 * Returns the maximum value
	 * 
	 * @return
	 */
	public float getMaximum()
	{
		return m_maxVal;
	}

	/**
	 * Returns minimum value
	 * 
	 * @return
	 */
	public float getMinimum()
	{
		return m_minVal;
	}

	/**
	 * Returns a double showing the percentage of completion
	 * 
	 * @return
	 */
	public double getPercentComplete()
	{
		double percent = (getValue() - getMinimum()) / (getMaximum() - getMinimum());
		return percent;
	}

	/**
	 * Returns the text held in the progress bar
	 * 
	 * @return
	 */
	public String getString()
	{
		return getText();
	}

	public Color getTextColor()
	{
		return textColor;
	}

	/**
	 * Returns the current value
	 * 
	 * @return
	 */
	public float getValue()
	{
		return m_value;
	}

	/**
	 * Renders the progress bar
	 */
	@Override
	public void render(GUIContext ctx, Graphics g)
	{
		super.render(ctx, g);
		g.setColor(getForeground());
		float val = (m_maxVal - m_value) / (m_maxVal - m_minVal);
		int barWidth = (int) ((int) getWidth() - getWidth() * val);

		if(reversed)
			g.fillRect(getAbsoluteX() + getWidth() - (barWidth > getWidth() ? getWidth() : barWidth), getAbsoluteY(), barWidth > getWidth() ? getWidth() : barWidth, getHeight() - 1);
		else
			g.fillRect(getAbsoluteX(), getAbsoluteY(), barWidth > getWidth() ? getWidth() : barWidth, getHeight() - 1);

		if(getText() != null && !getText().equals(""))
		{
			g.setColor(getTextColor());
			g.drawString(getText(), getAbsoluteX() + (getWidth() / 2 - getFont().getWidth(getText()) / 2), getAbsoluteY() + (getHeight() / 2 - getFont().getHeight(getText()) / 2));
		}
	}

	/**
	 * Sets the maximum value
	 * 
	 * @param m_maxVal
	 */
	public void setMaximum(int m_maxVal)
	{
		this.m_maxVal = m_maxVal;
	}

	/**
	 * Sets the minimum value
	 * 
	 * @param m_minVal
	 */
	public void setMinimum(int m_minVal)
	{
		this.m_minVal = m_minVal;
	}

	/**
	 * Sets text to be displayed inside the progress bar
	 * 
	 * @param string
	 */
	public void setString(String string)
	{
		setText(string);
	}

	public void setTextColor(Color c)
	{
		textColor = c;
	}

	/**
	 * Sets the current value
	 * 
	 * @param currentHP
	 */
	public void setValue(float currentHP)
	{
		m_value = currentHP;
	}
}
