package org.pokenet.client.ui.components;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
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
	private Color backgroundColor = Color.transparent;
	
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
	}
	
	public Label(String text, Font font, Color textColor, Color backColor)
	{
		this.setText(text);
		this.setFont(font);
		this.setTextColor(textColor);
		this.setBackgroundColor(backColor);
	}
	
	public Label(String text, Color backColor, Font font)
	{
		this.setText(text);
		this.setFont(font);
		this.setBackgroundColor(backColor);
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
	
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	
	public void setPosition(float newX, float newY)
	{
		this.setXLocation(newX);
		this.setYLocation(newY);
	}
	
	/**
	 * Sets the size of this label
	 * @param newWidth The width to set
	 * @param newHeight The height to set
	 */
	public void setSize(float newWidth, float newHeight)
	{
		this.setWidth(newWidth);
		this.setHeight(newHeight);
	}
	
	/**
	 * Sets this labels bounds
	 * @param newX This labels X-location in its parent.
	 * @param newY This labels Y-location in its parent.
	 * @param newWidth This labels width.
	 * @param newHeight This labels height.
	 */
	public void setBounds(float newX, float newY, float newWidth, float newHeight)
	{
		this.setPosition(newX, newY);
		this.setSize(newWidth, newHeight);
	}
	
	/**
	 * Makes the bounds of this label fit to its text. Only works if the label's font has been set first!
	 */
	public void fitToText()
	{
		if(getFont() == null)
		{
			System.out.println("WARNING: Attempting to fit the label to text but font is not set. Please set the font before fitting.");
			return;
		}
		this.setWidth(getTextWidth());
		this.setHeight(getTextHeight());
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
	public void render(GUIContext ctx, Graphics g)
	{
		//Save the old situation
		Color oldColor = g.getColor();
		Rectangle oldClip = g.getWorldClip();
		Font oldFont = g.getFont();
		
		//Set the font
		if(getFont() == null)
			setFont(ctx.getDefaultFont());
		
		//Set the clipping
		g.setWorldClip(getXLocation(), getYLocation(), getWidth(), getHeight());
		
		//Render the background
		if(getBackgroundColor() != null)
		{
			g.setColor(getBackgroundColor());
			g.fillRect(getXLocation(), getYLocation(), getWidth(), getHeight());
		}
		
		//Render the text
		g.setFont(getFont());
		g.setColor(getTextColor());
		g.drawString(getText(), getXLocation(), getYLocation());
		
		//Restore start situation
		g.setColor(oldColor);
		g.setWorldClip(oldClip);
		g.setFont(oldFont);
	}
}
