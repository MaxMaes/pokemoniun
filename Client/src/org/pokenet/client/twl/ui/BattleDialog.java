package org.pokenet.client.twl.ui;

public class BattleDialog
{
	private BattleCanvas canvas;
	private BattleControlFrame control;

	public BattleDialog()
	{
		canvas = new BattleCanvas();
		control = new BattleControlFrame();
	}

	public BattleCanvas getCanvas()
	{
		return canvas;
	}

	public BattleControlFrame getControlFrame()
	{
		return control;
	}
}
