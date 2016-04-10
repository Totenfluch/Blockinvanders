package me.game.pack;


public class MaxWeapon extends PlayerWeapon{
	double bulletW = 3.0,
			bulletH = 3.0;

	public MaxWeapon(Player Besitzer, int damage, int delay, int bulletSpeed) {
		super(Besitzer, 60, 25, 9);
		typ = WaffenTyp.MAX;
	}

	public MaxWeapon(Player Besitzer, int ammo) {
		super(Besitzer, 60, 25, 9);
		typ = WaffenTyp.MAX;
		this.ammo = ammo;
	}

	@Override
	public void shoot(double playerX, double playerY) {
		if(ammo > 0) {
			if(delay <= 0){
				delay = INIT_DELAY;
				ammo--;
				kugeln.add(new PlayerBullet(this, playerX+12, playerY+6, bulletH+3, bulletW+3, bulletSpeed, damage+15, 40));
				kugeln.add(new PlayerBullet(this, playerX+8, playerY+4, bulletH+3, bulletW+3, bulletSpeed, damage+15, 15));
				kugeln.add(new PlayerBullet(this, playerX+4, playerY+2, bulletH+3, bulletW+3, bulletSpeed, damage+15, 10));
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+15, 0));
				kugeln.add(new PlayerBullet(this, playerX-4, playerY+2, bulletH+3, bulletW+3, bulletSpeed, damage+15, -10));
				kugeln.add(new PlayerBullet(this, playerX-8, playerY+4, bulletH+3, bulletW+3, bulletSpeed, damage+15, -15));
				kugeln.add(new PlayerBullet(this, playerX-12, playerY+6, bulletH+3, bulletW+3, bulletSpeed, damage+15, -40));
			}
		}else{
			if(delay <= 0){
				delay = INIT_DELAY;
				kugeln.add(new PlayerBullet(this, playerX+12, playerY+6, bulletH, bulletW, bulletSpeed, damage, 40));
				kugeln.add(new PlayerBullet(this, playerX+8, playerY+4, bulletH, bulletW, bulletSpeed, damage, 15));
				kugeln.add(new PlayerBullet(this, playerX+4, playerY+2, bulletH, bulletW, bulletSpeed, damage, 10));
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, 0));
				kugeln.add(new PlayerBullet(this, playerX-4, playerY+2, bulletH, bulletW, bulletSpeed, damage, -10));
				kugeln.add(new PlayerBullet(this, playerX-8, playerY+4, bulletH, bulletW, bulletSpeed, damage, -15));
				kugeln.add(new PlayerBullet(this, playerX-12, playerY+6, bulletH, bulletW, bulletSpeed, damage, -40));
			}
		}
	}
}
