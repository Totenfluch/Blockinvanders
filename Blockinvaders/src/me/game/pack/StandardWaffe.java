package me.game.pack;

public class StandardWaffe extends Waffen {
	
	double bulletW, bulletH;
	

	public StandardWaffe() {
		super(10, 7, 4);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void shoot(double playerX, double playerY) {
		super.shoot(playerX, playerY, bulletW, bulletH);
		
	}
	

}
