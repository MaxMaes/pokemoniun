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
		
		Button maxLabel2 = new Button("Label not in frame");
		maxLabel2.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) 
			{
				System.out.println("clicked");
			}

			@Override
			public void mouseEntered(MouseEvent e) 
			{
				System.out.println("Entered");
			}

			@Override
			public void mouseExited(MouseEvent e) 
			{
				System.out.println("exited");
			}

			@Override
			public void mousePressed(MouseEvent e) 
			{
				System.out.println("pressed");
			}

			@Override
			public void mouseReleased(MouseEvent e) 
			{
				System.out.println("Released");
			}
		});
		maxLabel2.setBounds(20, 20, 100, 100);
		maxLabel2.setFont(gc.getDefaultFont());
		//maxLabel2.fitToText();
		maxLabel2.setTextColor(Color.red);
		maxLabel2.setBackgroundColor(Color.white);
		
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		Button b = new Button((new Image(respath + "res/pokemon/front/normal/004-2.png")));
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("KOEKJES");
				
			}
		});
		b.setHoverImage(new Image(respath + "res/pokemon/front/normal/005-2.png"));
		fr1.addComponent(b);
		
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
