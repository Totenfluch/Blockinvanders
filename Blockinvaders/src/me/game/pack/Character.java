package me.game.pack;

import com.sun.javafx.geom.Rectangle;

public abstract class Character {
	protected int leben;
	protected boolean alive = true;
	
	protected Weapon waffe;
	
	protected double xPos;
	protected double yPos;
	
	protected double width;
	protected double heigth;

	
	public double getX(){
		return xPos;
	}
	
	public double getY(){
		return yPos;
	}
	
	public double getWidth(){
		return width;
	}
	
	public double getHeight(){
		return heigth;
	}
	
	public boolean isAlive(){
		return alive;
	}
	public int getLeben(){
		return leben;
	}
	
	public Character(double xPos, double yPos,int leben, Weapon waffe){
		this.xPos = xPos;
		this.yPos = yPos;
		this.leben = leben;
		this.waffe = waffe;
	}
	
	public abstract void moveLeft();
	
	public abstract void moveRight();
	
	
	public abstract boolean setLeben(int leben);
	

	public boolean checkHit(double ox, double oy, double owidth, double olength){
		Rectangle rt1 = new Rectangle((int)ox, (int)oy, (int)owidth, (int)olength);
		Rectangle rt2 = new Rectangle((int)xPos, (int)yPos, (int)width, (int)heigth);
		if(rt1.intersection(rt2) != null)
			return true;
		return false;
	}
	
	public void giveWeapon(Weapon waffe){
		this.waffe = waffe;
	}
	
	public Weapon getWaffe(){
		return waffe;
	}
	
}