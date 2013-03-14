package org.pokenet.client.ui.components;

import java.util.ArrayList;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

public class Frame extends Component {
	private ArrayList<Component> components = new ArrayList<Component>();
	private TitleBar titleBar;

	public Frame() {
		this.titleBar = new TitleBar(this, "test");
	}

	public Frame(float x, float y) {
		this.setPosition(x, y);
		this.titleBar = new TitleBar(this, "test");
	}

	public Frame(float x, float y, float width, float height) {
		this.setBounds(x, y, width, height);
		this.titleBar = new TitleBar(this, "test");
	}

	public void addComponent(Component c) {
		this.components.add(c);
	}

	public void removeComponent(Component c) {
		this.components.remove(c);
	}

	public void removeComponent(int index) {
		this.components.remove(index);
	}
	
	public TitleBar getTitleBar() {
		return this.titleBar;
	}
	
	public void setTitleBar(TitleBar bar) {
		this.titleBar = bar;
	}

	@Override
	public void render(GUIContext gc, Graphics g) {		
		Color original = g.getColor();
		Rectangle oldClip = g.getWorldClip();
		Rectangle oldInOwn = new Rectangle(0, 0, oldClip.getWidth(), oldClip.getHeight());
		Rectangle newClip = Frame.intersect(oldInOwn, getBounds());

		g.translate(oldClip.getX(), oldClip.getY());
		g.setWorldClip(newClip);

		super.render(gc, g);
		
		g.setColor(Color.red);
		//g.draw(newClip);
		
		g.setColor(Color.green);
		//g.draw(getBounds());
		g.setColor(original);

		for (Component c : components)
			c.render(gc, g);
		
		if(titleBar.isVisible())
			titleBar.render(gc, g);

		g.translate(-oldClip.getX(), -oldClip.getY());
		g.setWorldClip(oldClip);
		
	}

	public static Rectangle intersect(Rectangle src1, Rectangle src2) {
		float right = Math.min(src1.getMaxX(), src2.getMaxX());
		float bottom = Math.min(src1.getMaxY(), src2.getMaxY());
		float left = Math.max(src1.getMinX(), src2.getMinX());
		float top = Math.max(src1.getMinY(), src2.getMinY());
		Rectangle intersecting = new Rectangle(left, top, right - left, bottom - top);
		return intersecting;
	}

	public class TitleBar extends Component
	{
		private Label titleLabel;
	    //private Button closeButton;
		private Frame owner;
		
		public TitleBar(Frame owner, String title)
		{
			this.titleLabel = new Label(title);
			this.owner = owner;
			this.setBounds(0, 0, owner.getWidth(), 20);
			
			try {
				String m_filepath = System.getProperty("res.path");
				if(m_filepath == null)
					m_filepath = "";
				Font font = new AngelCodeFont(m_filepath + "res/fonts/dex-large.fnt", m_filepath + "res/fonts/dex-large.png");
				this.titleLabel.setFont(font);
				this.titleLabel.fitToText();
				this.titleLabel.setY(-9);
				this.titleLabel.setTextColor(Color.white);
				this.titleLabel.setBackgroundColor(Color.transparent);
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public Frame getOwner() {
			return owner;
		}
		public void setOwner(Frame owner) {
			this.owner = owner;
		}
		public Label getTitleLabel() {
			return titleLabel;
		}
		public void setTitleLabel(Label titleLabel) {
			this.titleLabel = titleLabel;
		}
		
		/**
	     * Renders this titlebar
	     */
		@Override
		public void render(GUIContext gc, Graphics g)
		{
			Color original = g.getColor();
			Rectangle bounds = new Rectangle(0, 0, getOwner().getWidth(), 20);
			Rectangle oldClip = g.getWorldClip();
			
			g.translate(oldClip.getX(), oldClip.getY()-20);
			
			g.setWorldClip(bounds);

			super.render(gc, g);
			
			g.setColor(Color.blue);
			g.draw(bounds);
			g.setColor(original);
			
			titleLabel.render(gc, g);
			
			g.translate(-oldClip.getX(), -oldClip.getY()+20);
			
			g.setWorldClip(oldClip);
		}
	}
}
