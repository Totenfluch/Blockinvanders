package me.game.playerWeapons;

import me.game.bullets.PlayerBullet;
import me.game.pack.Player;

public class Shotgun  extends PlayerWeapon {
	
	double bulletW = 3.0,
			bulletH = 3.0;
	
	public Shotgun(Player Besitzer) {
		super(Besitzer, 25, 16, 5);
		typ = WeaponType.SHOTGUN;
	}
	
	public Shotgun(Player Besitzer, int ammo) {
		super(Besitzer, 25, 16, 5);
		typ = WeaponType.SHOTGUN;
		this.ammo = ammo;
	}

	
	@Override
	public void shoot(double playerX, double playerY) {
		if(ammo > 0) {
			if(delay <= 0){
				delay = INIT_DELAY;
				ammo--;
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+5, 3));
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+5, 0));
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH+3, bulletW+3, bulletSpeed, damage+5, -3));
			}
		}
		else{
			if(delay <= 0){
				delay = INIT_DELAY;
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, 3));
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, 0));
				kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, -3));
			}
		}
	}
	
}
