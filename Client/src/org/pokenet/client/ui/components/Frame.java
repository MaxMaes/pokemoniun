package org.pokenet.client.ui.components;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.GUIContext;

public class Frame extends Component {
	private ArrayList<Component> components = new ArrayList<Component>();

	public Frame() {

	}

	public Frame(float x, float y) {
		this.setPosition(x, y);
	}

	public Frame(float x, float y, float width, float height) {
		this.setBounds(x, y, width, height);
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

	@Override
	public void render(GUIContext gc, Graphics g) {
		Color original = g.getColor();
		Rectangle oldClip = g.getWorldClip();
		Rectangle oldInOwn = new Rectangle(0, 0, oldClip.getWidth(), oldClip.getHeight());
		Rectangle newClip = Frame.intersect(oldInOwn, getBounds());

		g.translate(oldClip.getX(), oldClip.getY());
		g.setWorldClip(newClip);

		g.setColor(Color.red);
		g.draw(newClip);
		
		g.setColor(Color.green);
		g.draw(getBounds());
		g.setColor(original);

		for (Component c : components)
			c.render(gc, g);

		g.setWorldClip(oldClip);
		g.translate(-oldClip.getX(), -oldClip.getY());
	}

	public static Rectangle intersect(Rectangle src1, Rectangle src2) {
		float right = Math.min(src1.getMaxX(), src2.getMaxX());
		float bottom = Math.min(src1.getMaxY(), src2.getMaxY());
		float left = Math.max(src1.getMinX(), src2.getMinX());
		float top = Math.max(src1.getMinY(), src2.getMinY());
		Rectangle intersecting = new Rectangle(left, top, right - left, bottom - top);
		return intersecting;
	}

}
