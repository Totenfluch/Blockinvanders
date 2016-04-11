package me.game.pack;


public class FiveWeapon extends PlayerWeapon{
	double bulletW = 3.0,
			bulletH = 3.0;

	public FiveWeapon(Player Besitzer, int damage, int delay, int bulletSpeed) {
		super(Besitzer, 35, 20, 7);
		typ = WeaponType.FIVE;
	}

	public FiveWeapon(Player Besitzer, int ammo) {
		super(Besitzer, 35, 20, 7);
		typ = WeaponType.FIVE;
		this.ammo = ammo;
	}

	@Override
	public void shoot(double playerX, double playerY) {
		if(ammo > 0) {
			if(delay <= 0){
				delay = INIT_DELAY;
				ammo--;
				kugeln.add(new PlayerBullet(this, playerX+2, playerY, bulletH+4, bulletW+4, bulletSpeed, damage+15, 16));
				kugeln.add(new PlayerBullet(this, playerX+1, playerY, bulletH+4, bulletW+4, bulletSpeed, damage+15, 10));
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH+4, bulletW+4, bulletSpeed, damage+15, 0));
				kugeln.add(new PlayerBullet(this, playerX+1, playerY, bulletH+4, bulletW+4, bulletSpeed, damage+15, -10));
				kugeln.add(new PlayerBullet(this, playerX+2, playerY, bulletH+4, bulletW+4, bulletSpeed, damage+15, -16));
			}
		}else{
			if(delay <= 0){
				delay = INIT_DELAY;
				kugeln.add(new PlayerBullet(this, playerX+2, playerY, bulletH, bulletW, bulletSpeed, damage, 16));
				kugeln.add(new PlayerBullet(this, playerX+1, playerY, bulletH, bulletW, bulletSpeed, damage, 10));
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, 0));
				kugeln.add(new PlayerBullet(this, playerX-1, playerY, bulletH, bulletW, bulletSpeed, damage, -10));
				kugeln.add(new PlayerBullet(this, playerX-2, playerY, bulletH, bulletW, bulletSpeed, damage, -16));
			}
		}
	}
}
