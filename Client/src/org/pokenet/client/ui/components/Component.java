package org.pokenet.client.ui.components;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

public abstract class Component
{
	protected Rectangle bounds;
	protected boolean isVisible = true;
	
	public Component() {}
	
	public void setX(float newlocation)
	{
		this.getBounds().setX(newlocation);
	}
	
	public float getX()
	{
		return this.getBounds().getX();
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}

	public void setY(float newlocation)
	{
		this.getBounds().setY(newlocation);
	}
	
	public float getY()
	{
		return this.getBounds().getY();
	}
	
	public void setHeight(float newHeight)
	{
		this.getBounds().setHeight(newHeight);
	}
	
	public float getHeight()
	{
		return this.getBounds().getHeight();
	}
	
	public void setWidth(float newWidth)
	{
		this.getBounds().setWidth(newWidth);
	}
	
	public float getWidth()
	{
		return this.getBounds().getWidth();
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
		this.getBounds().setSize(newWidth, newHeight);
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
		this.bounds = new Rectangle(newX, newY, newWidth, newHeight);
	}
	
	public void setPosition(float newX, float newY)
	{
		this.getBounds().setLocation(newX, newY);
	}
	
	/**
     * Renders this component
     */
	public abstract void render(GUIContext gc, Graphics g);
}
