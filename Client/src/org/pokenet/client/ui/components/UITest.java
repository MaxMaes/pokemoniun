package org.pokenet.client.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
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
		g.setWorldClip(new Rectangle(0,  0, 800,  600));
		
		Frame fr1 = new Frame(100, 100, 500, 500);
		Frame fr2 = new Frame(10, 10, 450, 450);
		Frame fr3 = new Frame(10, 10, 400, 400);
		
		fr1.setBackgroundColor(Color.yellow);
		fr2.setBackgroundColor(Color.pink);
		//fr3.setBackgroundColor(Color.lightGray);
		
		Label maxLabel = new Label("2 label", 0, 0, gc.getDefaultFont(), Color.white, Color.transparent);
		maxLabel.fitToText();

		fr1.addComponent(maxLabel);
		fr1.addComponent(fr2);
		//fr2.addComponent(fr3);
		
		
		
		//fr3.addComponent(maxLabel2);
		
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
