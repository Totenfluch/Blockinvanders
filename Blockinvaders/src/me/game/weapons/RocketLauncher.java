package me.game.weapons;

import me.game.bullets.ClusterBullet;
import me.game.pack.Player;

public class RocketLauncher extends PlayerWeapon {

	
	public RocketLauncher(Player besitzer) {
		super(besitzer, 100, 150, 4);
		typ = WeaponType.ROCKET;
	}
	
	public RocketLauncher(Player besitzer, int ammo) {
		super(besitzer, 100, 500, 4);
		this.ammo = ammo;
		typ = WeaponType.ROCKET;
	}

	@Override
	public void shoot(double playerX, double playerY) {
		if(ammo >0){
		if(delay <= 0){
			delay = INIT_DELAY;
			ammo--;
			kugeln.add(new ClusterBullet(this, playerX, playerY, 12, 12, bulletSpeed, damage, 0, 100));
		}
		}
		else {
			Owner.giveWeapon(this.prevWaffe());
		}
	}
}
