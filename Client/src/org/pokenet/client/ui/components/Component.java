package org.pokenet.client.ui.components;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public abstract class Component
{
	protected float xLocation = 0;
	protected float yLocation = 0;
	protected float height = 0;
	protected float width = 0;
	protected boolean isVisible = true;
	
	public Component() {}
	
	public void setXLocation(float newlocation)
	{
		this.xLocation = newlocation;
	}
	
	public float getXLocation()
	{
		return this.xLocation;
	}
	
	public void setYLocation(float newlocation)
	{
		this.yLocation = newlocation;
	}
	
	public float getYLocation()
	{
		return this.yLocation;
	}
	
	public void setHeight(float newHeight)
	{
		this.height = newHeight;
	}
	
	public float getHeight()
	{
		return this.height;
	}
	
	public void setWidth(float newWidth)
	{
		this.width = newWidth;
	}
	
	public float getWidth()
	{
		return this.width;
	}
	
	public boolean isVisible()
	{
		return this.isVisible;
	}
	
	public void hide()
	{
		this.setVisible(false);
	}
	
	public void show()
	{
		this.setVisible(true);
	}
	
	public void setVisible(boolean toSet)
	{
		this.isVisible = toSet;
	}
	
	/**
	 * Sets the size of this components
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
	 * @param newX This components X-location in its parent.
	 * @param newY This components Y-location in its parent.
	 * @param newWidth This labels width.
	 * @param newHeight This labels height.
	 */
	public void setBounds(float newX, float newY, float newWidth, float newHeight)
	{
		this.setPosition(newX, newY);
		this.setSize(newWidth, newHeight);
	}
	
	public void setPosition(float newX, float newY)
	{
		this.setXLocation(newX);
		this.setYLocation(newY);
	}
	
	/**
     * Renders this component
     */
	public abstract void render(GUIContext gc, Graphics g);
}
