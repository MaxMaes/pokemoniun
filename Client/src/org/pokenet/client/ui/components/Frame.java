package org.pokenet.client.ui.components;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

public class Frame extends Component
{
	private ArrayList<Component> components = new ArrayList<Component>();
	
	public Frame()
	{
		
	}
	
	public Frame(float x, float y)
	{
		this.setPosition(x, y);
	}
	
	public Frame(float x, float y, float width, float height)
	{
		this.setBounds(x,  y,  width,  height);
	}
	
	public void addComponent(Component c)
	{
		this.components.add(c);
	}
	
	public void removeComponent(Component c)
	{
		this.components.remove(c);
	}
	
	public void removeComponent(int index)
	{
		this.components.remove(index);
	}
	
	
	@Override
	public void render(GUIContext gc, Graphics g)
	{
		Color original = g.getColor();
		Rectangle oldClip = g.getWorldClip();
		
		g.setColor(Color.green);
		g.drawRect(getXLocation(), getYLocation(), getWidth(), getHeight());
		g.setColor(original);
		
		g.translate(getXLocation(), getYLocation());	
		g.setWorldClip(0, 0, getWidth(), getHeight());
		
		for(Component c : components)
			c.render(gc, g);
		
		g.setWorldClip(oldClip);
		g.translate(-getXLocation(), -getYLocation());
	}

}
