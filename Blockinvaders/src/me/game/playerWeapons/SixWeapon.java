package me.game.playerWeapons;

import me.game.bullets.PlayerBullet;
import me.game.pack.Player;

public class SixWeapon extends PlayerWeapon{
	double bulletW = 3.0,
			bulletH = 3.0;

	public SixWeapon(Player Besitzer, int damage, int delay, int bulletSpeed) {
		super(Besitzer, 45, 23, 8);
		typ = WeaponType.SIX;
	}

	public SixWeapon(Player Besitzer, int ammo) {
		super(Besitzer, 45, 23, 8);
		typ = WeaponType.SIX;
		this.ammo = ammo;
	}

	@Override
	public void shoot(double playerX, double playerY) {
		if(ammo > 0) {
			if(delay <= 0){
				delay = INIT_DELAY;
				ammo--;
				kugeln.add(new PlayerBullet(this, playerX+5, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+15, 12));
				kugeln.add(new PlayerBullet(this, playerX+3, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+15, 8));
				kugeln.add(new PlayerBullet(this, playerX+1, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+15, 5));
				kugeln.add(new PlayerBullet(this, playerX-1, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+15, -5));
				kugeln.add(new PlayerBullet(this, playerX-3, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+15, -8));
				kugeln.add(new PlayerBullet(this, playerX-5, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+15, -12));
			}
		}else{
			if(delay <= 0){
				delay = INIT_DELAY;
				kugeln.add(new PlayerBullet(this, playerX+5, playerY, bulletH, bulletW, bulletSpeed, damage, 12));
				kugeln.add(new PlayerBullet(this, playerX+3, playerY, bulletH, bulletW, bulletSpeed, damage, 8));
				kugeln.add(new PlayerBullet(this, playerX+1, playerY, bulletH, bulletW, bulletSpeed, damage, 5));
				kugeln.add(new PlayerBullet(this, playerX-1, playerY, bulletH, bulletW, bulletSpeed, damage, -5));
				kugeln.add(new PlayerBullet(this, playerX-3, playerY, bulletH, bulletW, bulletSpeed, damage, -8));
				kugeln.add(new PlayerBullet(this, playerX-5, playerY, bulletH, bulletW, bulletSpeed, damage, -12));
			}
		}
	}
}
