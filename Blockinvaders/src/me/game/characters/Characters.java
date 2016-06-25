package me.game.characters;

import me.game.pack.Drawable;
import me.game.pack.Frame;
import me.game.playerWeapons.PlayerWeapon;
import me.game.playerWeapons.SpecialWeapon;
import me.game.playerWeapons.Weapon;

public abstract class Characters implements Drawable{
	public final Frame game;
	protected int life;
	protected boolean alive = true;
	
	public Weapon hisWeapon;
	public SpecialWeapon hisSpecialWeapon;
	
	protected double xPos;
	protected double yPos;
	
	protected double width;
	protected double height;

	
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
		return height;
	}
	
	public boolean isAlive(){
		return alive;
	}
	public int getLife(){
		return life;
	}
	
	public Characters(double xPos, double yPos,int leben, Weapon waffe){
		this.game = Frame.getInstance();
		this.xPos = xPos;
		this.yPos = yPos;
		this.life = leben;
		this.hisWeapon = waffe;
	}
	
	public abstract void moveLeft();
	
	public abstract void moveRight();
	
	
	public abstract boolean setLife(int leben);
	
	public abstract void triggerDeath();
	

	public boolean checkHit(double ox, double oy, double owidth, double oheight){
		return ox < xPos + width && ox + owidth > xPos && oy < yPos + height && oy + oheight > yPos;
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