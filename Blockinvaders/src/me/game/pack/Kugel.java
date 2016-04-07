package me.game.pack;

public class Kugel{
	double xPos, yPos;
	double height ,width;
	int speed;
	
	public Kugel(double xPos, double yPos, double height, double width, int speed) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.height = height;
		this.width = width;
		this.speed = speed;
	}
	
	public void refresh(){
		yPos -= speed;		
	}
	
	
	
	
}

