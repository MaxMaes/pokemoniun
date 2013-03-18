package org.pokenet.client.ui.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

/**
 * 
 * @author Myth1c
 *
 * This class describes a standard text label with colored background.
 */

public class Label extends Component
{
	private String text;
	private Font font;
	private Color textColor = Color.white;
	
	public Label()
	{
		this.setText("");
	}
	
	public Label(String text)
	{
		this.setText(text);
	}
	
	public Label(String text, Font font)
	{
		this.setText(text);
		this.setFont(font);
	}
	
	public Label(String text, Color textColor)
	{
		this.setText(text);
		this.setTextColor(textColor);
	}
	
	public Label(String text, Font font, Color textColor)
	{
		this.setText(text);
		this.setFont(font);
		this.setTextColor(textColor);
		this.fitToText();
	}
	
	public Label(String text, Color backColor, Font font)
	{
		this.setText(text);
		this.setFont(font);
		this.setBackgroundColor(backColor);
		this.fitToText();
	}
	
	public Label(String text, float x, float y, Font font, Color textColor, Color backColor)
	{
		this.setText(text);
		this.setFont(font);
		this.setTextColor(textColor);
		this.setBackgroundColor(backColor);
		this.setBounds(x, y, 0, 0);
	}
	
	public Label(String text, float x, float y, float width, float height, Font font, Color textColor, Color backColor)
	{
		this.setText(text);
		this.setFont(font);
		this.setTextColor(textColor);
		this.setBackgroundColor(backColor);
		this.setBounds(x, y, width, height);
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Font getFont()
	{
		return font;
	}

	public void setFont(Font font)
	{
		this.font = font;
	}

	public Color getTextColor()
	{
		return textColor;
	}

	public void setTextColor(Color color)
	{
		this.textColor = color;
	}
	
	/**
	 * Makes the bounds of this label fit to its text. Only works if the label's font has been set first!
	 */
	public void fitToText()
	{
		if(getFont() == null)
		{
			//System.out.println("WARNING: Attempting to fit the label to text but font is not set. Please set the font before fitting.");
			return;
		}
		if(this.getBounds() == null)
		{
			this.setBounds(0, 0, getTextWidth(), getTextHeight());
		} else
		{
			this.setWidth(getTextWidth());
			this.setHeight(getTextHeight());
		}
	}
	
	/**
	 * Gets the height of the text in pixels.
	 * @return The height of the text in pixels.
	 */
	public float getTextHeight() {
		if(getFont() == null)
		{
			System.out.println("WARNING: Attempting to get text height but font is not set.");
			return 0;
		}
		
        String str = getText()!=null ? getText() : "";
        return getFont().getHeight(str);
    }
    
	/**
	 * Gets the width of the text in pixels.
	 * @return The width of the text in pixels.
	 */
    public float getTextWidth() {
    	if(getFont() == null)
    	{
    		System.out.println("WARNING: Attempting to get text width but font is not set.");
			return 0;
    	}
    	
        String str = getText()!=null ? getText() : "";
        return getFont().getWidth(str);
    }
    
    /**
     * Renders this label
     */
	@Override
	public void render(GUIContext gc, Graphics g)
	{
		super.render(gc, g);
		//Save the old situation
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		
		//Set the font
		if(getFont() == null)
		{
			setFont(gc.getDefaultFont());
			fitToText();
		}
		
		//Render the text
		g.setFont(getFont());
		g.setColor(getTextColor());
		g.drawString(getText(), getX(), getY());
		
		//Restore start situation
		g.setColor(oldColor);
		g.setFont(oldFont);
	}
}
