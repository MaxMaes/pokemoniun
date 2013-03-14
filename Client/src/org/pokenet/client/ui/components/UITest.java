package org.pokenet.client.ui.components;

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
		
		Frame fr1 = new Frame(100, 100, 100, 100);
		ContentPanel fr2 = new ContentPanel(10, 10, 100, 100);
		//ContentPanel fr3 = new ContentPanel(0, 0, 50, 50);
		
		fr1.setBackgroundColor(Color.yellow);
		fr2.setBackgroundColor(Color.pink);
		//fr3.setBackgroundColor(Color.lightGray);
		
		Label maxLabel = new Label("2 label", 0, 0, gc.getDefaultFont(), Color.white, Color.transparent);
		maxLabel.fitToText();

		//fr2.addComponent(maxLabel);
		fr1.addComponent(fr2);
		//fr2.addComponent(fr3);
		
		Label maxLabel2 = new Label("3 label");
		maxLabel2.setBounds(20, 20, 0, 0);
		maxLabel2.setFont(gc.getDefaultFont());
		maxLabel2.fitToText();
		maxLabel2.setTextColor(Color.blue);
		maxLabel2.setBackgroundColor(Color.white);
		
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		ImageIcon test = new ImageIcon(new Image(respath + "res/pokemon/front/normal/006-2.png"), 0, 0);
		fr1.addComponent(test);
		
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
