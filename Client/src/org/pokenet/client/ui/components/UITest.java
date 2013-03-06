package org.pokenet.client.ui.components;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.pokenet.client.ui.components.Component;
import org.pokenet.client.ui.components.Label;

public class UITest extends BasicGame
{
	private static AppGameContainer gameContainer;
	
	public static void main(String[] args)
	{
		try
		{
			gameContainer = new AppGameContainer(new UITest("UITest"), 800, 600, false);
			gameContainer.setTargetFrameRate(60);
			gameContainer.setAlwaysRender(true);
			gameContainer.start();
		}
		catch(SlickException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public UITest(String title)
	{
		super(title);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		g.setBackground(Color.black);
		
		Frame fr1 = new Frame(100, 100, 100, 100);
		
		
		Label maxLabel = new Label("Label in frame", 0, 20, gc.getDefaultFont(), Color.white, Color.blue);
		maxLabel.fitToText();

		fr1.addComponent(maxLabel);
		
		Label maxLabel2 = new Label("Label not in frame");
		maxLabel2.setBounds(100, 100, 0, 0);
		maxLabel2.setFont(gc.getDefaultFont());
		maxLabel2.fitToText();
		maxLabel2.setTextColor(Color.blue);
		maxLabel2.setBackgroundColor(Color.white);

		maxLabel2.render(gc, g);
		
		fr1.render(gc, g);
	}

	@Override
	public void init(GameContainer arg0) throws SlickException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException
	{
		// TODO Auto-generated method stub
		
	}

}
