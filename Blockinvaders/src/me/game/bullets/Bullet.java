package me.game.bullets;

import me.game.pack.Drawable;
import me.game.playerWeapons.Weapon;


public abstract class Bullet implements Drawable{

	double xPos, yPos;
	double height, width;
	int damage;
	double dx, dy;
	Weapon waffe;
	
	public Bullet(Weapon waffe, double xPos, double yPos, double height, double width, int speed, int damage, double angle) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.height = height;
		this.width = width;
		this.damage = damage;
		this.waffe = waffe;
		this.xPos += waffe.getOwner().getWidth()/2/2;
		this.xPos += width/2;
		
		if(angle >= -180.0 && angle <= 180){
			dx = Math.sin(Math.toRadians(angle))*speed;
			dy = Math.cos(Math.toRadians(angle))*speed;
		}
		else
			dy = speed;
	}
	
	
	public double getxPos() {
		return xPos;
	}
	public double getyPos() {
		return yPos;
	}
	
	public double getHeight() {
		return height;
	}
	public double getWidth() {
		return width;
	}

	public int getDamage() {
		return damage;
	}

	public abstract void refresh();
	
	public void onHit(){
		waffe.getKugeln().remove(this);
	}
	
	public boolean checkHit(double ox, double oy, double owidth, double oheight){
		return ox < xPos + width && ox + owidth > xPos && oy < yPos + height && oy + oheight > yPos;
	}
}