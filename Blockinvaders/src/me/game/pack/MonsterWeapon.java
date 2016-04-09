package me.game.pack;

import java.util.Vector;

public abstract class MonsterWeapon extends Weapon {

	public static Vector<MonsterWeapon> ActiveWeapons = new Vector<MonsterWeapon>();


	public MonsterWeapon(Monster Besitzer, int damage,int delay,int bulletSpeed){
		super(Besitzer, damage, delay, bulletSpeed);
		ActiveWeapons.add(this);
	}


	public void shoot(double playerX, double playerY, double bulletW, double bulletH, double angle) {
		if(delay <= 0){
			delay = INIT_DELAY;
			kugeln.add(new MonsterBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, angle));
		}
	}

	public abstract void shoot(double monsterX, double monsterY);

}
