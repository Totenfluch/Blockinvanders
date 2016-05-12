package me.game.playerWeapons;

import me.game.bullets.PlayerBullet;
import me.game.characters.Player;

public class QuadWeapon extends PlayerWeapon{

	double bulletW = 3.0,
			bulletH = 3.0;

	public QuadWeapon(Player Besitzer, int damage, int delay, int bulletSpeed) {
		super(Besitzer, 30, 18, 6);
		typ = WeaponType.QUAD;
	}

	public QuadWeapon(Player Besitzer, int ammo) {
		super(Besitzer, 30, 18, 6);
		typ = WeaponType.QUAD;
		this.ammo = ammo;
	}

	@Override
	public void shoot(double playerX, double playerY) {
		if(ammo > 0) {
			if(delay <= 0){
				delay = INIT_DELAY;
				ammo--;
				kugeln.add(new PlayerBullet(this, playerX-12, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+10, 0));
				kugeln.add(new PlayerBullet(this, playerX+12, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+10, 0));
				kugeln.add(new PlayerBullet(this, playerX-7, playerY-6, bulletH+3, bulletW+3, bulletSpeed, damage+10, 0));
				kugeln.add(new PlayerBullet(this, playerX+7, playerY-6, bulletH+3, bulletW+3, bulletSpeed, damage+10, 0));
			}
		}
		else{
			if(delay <= 0){
				delay = INIT_DELAY;
				kugeln.add(new PlayerBullet(this, playerX-12, playerY, bulletH, bulletW, bulletSpeed, damage, 0));
				kugeln.add(new PlayerBullet(this, playerX+12, playerY, bulletH, bulletW, bulletSpeed, damage, 0));
				kugeln.add(new PlayerBullet(this, playerX-7, playerY-6, bulletH, bulletW, bulletSpeed, damage, 0));
				kugeln.add(new PlayerBullet(this, playerX+7, playerY-6, bulletH, bulletW, bulletSpeed, damage, 0));
			}
		}
	}

}
