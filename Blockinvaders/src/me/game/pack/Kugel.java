package me.game.pack;

public class Kugel{
	double xPos, yPos;
	double height, width;
	int damage;
	double dx, dy;
	Waffen waffe;
	
	public Kugel(Waffen waffe, double xPos, double yPos, double height, double width, int speed, int damage, double angle) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.height = height;
		this.width = width;
		this.damage = damage;
		this.waffe = waffe;
		
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

	public void refresh(){
		xPos += dx;
		yPos -= dy;
	}
	
	
	
	
}

