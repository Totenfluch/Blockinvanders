package me.game.pack;

import javafx.scene.shape.Rectangle;

public class MonsterKugel {
	double xPos, yPos;
	double height, width;
	int damage;
	double dx, dy;
	MonsterWaffe waffe;
	
	public MonsterKugel(MonsterWaffe waffe, double xPos, double yPos, double height, double width, int speed, int damage, double angle) {
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
		for(int x =0;x<Frame.Players.size(); x++){
			Spieler p = Frame.Players.elementAt(x);
			if(checkHit(p.getX(), p.getY(), p.getWidth(), p.getHeight())){
				if(!p.isDead()){
					waffe.kugeln.remove(this);
					p.removeLeben();
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
