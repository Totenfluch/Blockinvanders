package me.game.weapons;

import me.game.bullets.PlayerBullet;
import me.game.pack.Player;

public class DualsWeapon extends PlayerWeapon{

	double bulletW = 3.0,
			bulletH = 3.0;

	public DualsWeapon(Player Besitzer, int damage, int delay, int bulletSpeed) {
		super(Besitzer, 25, 14, 5);
		typ = WeaponType.DUALS;
	}

	public DualsWeapon(Player Besitzer, int ammo) {
		super(Besitzer, 25, 14, 5);
		typ = WeaponType.DUALS;
		this.ammo = ammo;
	}

	@Override
	public void shoot(double playerX, double playerY) {
		if(ammo > 0) {
			if(delay <= 0){
				delay = INIT_DELAY;
				ammo--;
				kugeln.add(new PlayerBullet(this, playerX-6, playerY, bulletH+4, bulletW+4, bulletSpeed, damage+10, 0));
				kugeln.add(new PlayerBullet(this, playerX+6, playerY, bulletH+4, bulletW+4, bulletSpeed, damage+10, 0));
			}
		}else{
			if(delay <= 0){
				delay = INIT_DELAY;
				kugeln.add(new PlayerBullet(this, playerX-6, playerY, bulletH, bulletW, bulletSpeed, damage, 0));
				kugeln.add(new PlayerBullet(this, playerX+6, playerY, bulletH, bulletW, bulletSpeed, damage, 0));
			}
		}
	}

}
