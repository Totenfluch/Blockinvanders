package me.game.pack;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import com.sun.javafx.geom.Rectangle;

import me.game.playerWeapons.PlayerWeapon;
import me.game.playerWeapons.SpecialWeapon;
import me.game.playerWeapons.Weapon;

public abstract class Character implements Drawable{
	public final Frame game;
	protected int life;
	protected boolean alive = true;
	
	public Weapon hisWeapon;
	public SpecialWeapon hisSpecialWeapon;
	
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
	
	public void setX(double xPos){
		this.xPos = xPos;
	}
	
	public void setY(double yPos){
		this.yPos = yPos;
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
		this.game = Frame.getInstance();
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
		if(hisWeapon != waffe && hisWeapon != null)
				hisWeapon.requestRemoval();
		this.hisWeapon = waffe;
	}
	
	public Weapon getHisWeapon(){
		return hisWeapon;
	}
	
	public void giveSpecialWeapon(SpecialWeapon waffe){
		PlayerWeapon.ActiveWeapons.remove(hisSpecialWeapon);
		this.hisSpecialWeapon = waffe;
	}
	
	public SpecialWeapon getHisSpecialWeapon(){
		return hisSpecialWeapon;
	}
	
}