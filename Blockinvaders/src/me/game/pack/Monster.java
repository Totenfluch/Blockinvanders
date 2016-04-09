package me.game.pack;

import com.sun.javafx.geom.Rectangle;

public class Monster {
	private int Leben;
	private double x;
	private double y;
	private double width;
	private double length;
	private int worth;
	private boolean alive;
	private int InitialHp;
	
	public Monster(int Leben, double x, double y, double width, double length, int worth){
		this.Leben = Leben;
		this.x = x;
		this.y = y;
		this.width = width;
		this.length = length;
		this.worth = worth;
		alive = true;
		InitialHp = Leben;
	}
	
	public int getInitHp(){
		return InitialHp;
	}
	
	public void setInitHp(int hp){
		InitialHp = hp;
	}
	
	public double getPosX(){
		return x;
	}
	
	public double getPosY(){
		return y;
	}
	
	public double getWidth(){
		return width;
	}
	
	public double getLength(){
		return length;
	}
	
	public boolean checkHit(double ox, double oy, double owidth, double olength){
		Rectangle rt1 = new Rectangle((int)ox, (int)oy, (int)owidth, (int)olength);
		Rectangle rt2 = new Rectangle((int)x, (int)y, (int)width, (int)length);
		if(rt1.intersection(rt2) != null)
			return true;
		return false;
	}
	
	public int getLeben(){
		return Leben;
	}
	
	public boolean setLeben(int Leben){
		this.Leben = Leben;
		if(this.Leben <= 0){
			alive = false;
			return false;
		}else{
			alive = true;
			return true;
		}
	}
	
	public boolean subLeben(int amount){
		Leben -= amount;
		if(Leben <= 0){
			alive = false;
			return false;
		}else{
			alive = true;
			return true;
		}
	}
	
	public void moveLeft(){
		x--;
	}
	
	public void moveRight(){
		x++;
	}
	
	public void moveUp(){
		y--;
	}
	
	public void moveDown(){
		y++;
	}
	
	public int getWorth(){
		return worth;
	}
	
	public boolean isAlive(){
		return alive;
	}
}
