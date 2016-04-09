package me.game.pack;

import javafx.scene.shape.Rectangle;


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
		this.xPos += waffe.Besitzer.getWidth()/2/2;
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

	public void refresh(){
		for(int i = 0; i<Frame.Monsters.length; i++){
			Monster Monti = Frame.Monsters[i];
			if(checkHit(Monti.getPosX(), Monti.getPosY(), Monti.getWidth(), Monti.getLength())){
				if(Monti.isAlive()){
					waffe.kugeln.remove(this);
					if(!Monti.subLeben(damage))
						waffe.Besitzer.incScore(Monti.getWorth());
				}
			}
		}
		
		xPos += dx;
		yPos -= dy;
	}
	
	public boolean checkHit(double ox, double oy, double owidth, double olength){
		Rectangle rt1 = new Rectangle((int)ox, (int)oy, (int)owidth, (int)olength);
		Rectangle rt2 = new Rectangle((int)xPos, (int)yPos, (int)width, (int)height);
		if(rt1.intersects(rt2.getBoundsInLocal()))
			return true;
		return false;
	}
	
	
}

