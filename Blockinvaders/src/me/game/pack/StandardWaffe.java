package me.game.pack;

public class StandardWaffe extends Waffen {
	
	double bulletW = 5.0,
			bulletH = 5.0;
	

	public StandardWaffe(Spieler Besitzer) {
		super(Besitzer, 10, 12, 4);
	}

	@Override
	public void shoot(double playerX, double playerY) {
		if(ammo > 0) {
			
			if(delay <= 0){
				delay = INIT_DELAY;
				ammo--;
				kugeln.add(new Kugel(this, playerX, playerY, 8.0, 8.0, bulletSpeed, damage*2, 0));
			}
		}
		else
			super.shoot(playerX, playerY, bulletW, bulletH, 0);
	}
	

}
