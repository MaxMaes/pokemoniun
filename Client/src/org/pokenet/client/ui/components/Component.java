package org.pokenet.client.ui.components;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

public abstract class Component
{
	protected float xLocation;
	protected float yLocation;
	protected float height;
	protected float width;
	protected boolean isVisible;
	
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
     * Renders this component
     */
	public abstract void render(GUIContext ctx, Graphics g);
}
