package me.game.pack;

public class Shotgun  extends Waffen {
	
	double bulletW = 3.0,
			bulletH = 3.0;
	

	public Shotgun(Spieler Besitzer) {
		super(Besitzer, 25, 20, 5);
	}

	
	@Override
	public void shoot(double playerX, double playerY) {
		if(ammo > 0) {
			if(delay <= 0){
				delay = INIT_DELAY;
				ammo--;
				kugeln.add(new Kugel(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, 3));
				kugeln.add(new Kugel(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, 0));
				kugeln.add(new Kugel(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, -3));
			}
		}
		else
			super.shoot(playerX, playerY, bulletW, bulletH, 0);
	}
	
}
