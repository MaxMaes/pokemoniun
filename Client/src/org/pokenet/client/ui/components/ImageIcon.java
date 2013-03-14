package org.pokenet.client.ui.components;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

public class ImageIcon extends Component {
	private Image image;
	
	public ImageIcon(Image img, float x, float y)
	{
		this.setImage(img);
		this.setPosition(x, y);
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	@Override
	public void render(GUIContext gc, Graphics g)
	{
		g.translate(g.getWorldClip().getX(), g.getWorldClip().getY());
		g.drawImage(this.getImage(), this.getX(), this.getY());
		g.translate(-g.getWorldClip().getX(), -g.getWorldClip().getY());
	}
}
