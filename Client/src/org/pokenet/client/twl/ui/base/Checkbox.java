package org.pokenet.client.twl.ui.base;

import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.Widget;

public class Checkbox extends Widget {
	private ToggleButton checkbox;
	private Label text;
	
	public Checkbox(String txt) {
		checkbox = new ToggleButton();
		//checkbox.setTheme("checkbox");
		checkbox.setSize(14, 14);
		checkbox.setPosition(0,0);
		text = new Label(txt);
		add(checkbox);
		add(text);
	}
	
	public void setText(String txt) {
		text.setText(txt);
		invalidateLayout();
	}
	
	@Override
	public void layout() {
		text.setPosition(checkbox.getX() + 20, checkbox.getY() + 7);
	}
	
	public void setActive(boolean toSet) {
		checkbox.setActive(toSet);
	}
	
	public boolean isActive() {
		return checkbox.isActive();
	}
}
