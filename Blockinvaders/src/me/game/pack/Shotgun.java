package me.game.pack;

public class Shotgun  extends PlayerWeapon {
	
	double bulletW = 3.0,
			bulletH = 3.0;
	
	public Shotgun(Player Besitzer) {
		super(Besitzer, 25, 20, 5);
		typ = WaffenTyp.SHOTGUN;
	}

	
	@Override
	public void shoot(double playerX, double playerY) {
		if(ammo > 0) {
			if(delay <= 0){
				delay = INIT_DELAY;
				ammo--;
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, 3));
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, 0));
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, -3));
			}
		}
		else
			super.shoot(playerX, playerY, bulletW, bulletH, 0);
	}
	
}
