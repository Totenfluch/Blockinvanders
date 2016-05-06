package me.game.pack;

public interface Controllable extends Controlling{
	public Controllable getController();
	public void setController(Controlling controller);
	public boolean isControlled();
}
