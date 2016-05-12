package me.game.characters;

public interface Controllable{
	public Controller getController();
	public void setController(Controller controller);
	public boolean isControlled();
}
