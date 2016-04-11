package me.game.pack;

import com.sun.javafx.geom.Rectangle;

public abstract class Character {
	protected int life;
	protected boolean alive = true;
	
	protected Weapon hisWeapon;
	
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
	public int getLife(){
		return life;
	}
	
	public Character(double xPos, double yPos,int leben, Weapon waffe){
		this.xPos = xPos;
		this.yPos = yPos;
		this.life = leben;
		this.hisWeapon = waffe;
	}
	
	public abstract void moveLeft();
	
	public abstract void moveRight();
	
	
	public abstract boolean setLife(int leben);
	

	public boolean checkHit(double ox, double oy, double owidth, double olength){
		Rectangle rt1 = new Rectangle((int)ox, (int)oy, (int)owidth, (int)olength);
		Rectangle rt2 = new Rectangle((int)xPos, (int)yPos, (int)width, (int)heigth);
		if(rt1.intersection(rt2) != null)
			return true;
		return false;
	}
	
	public void giveWeapon(Weapon waffe){
		this.hisWeapon = waffe;
	}
	
	public Weapon getHisWeapon(){
		return hisWeapon;
	}
	
}