package me.game.monsterWeapons;

import java.util.Vector;

import me.game.bullets.MonsterBullet;
import me.game.characters.Monster;
import me.game.playerWeapons.Weapon;

public abstract class MonsterWeapon extends Weapon {

	public static Vector<MonsterWeapon> ActiveWeapons = new Vector<MonsterWeapon>();


	public MonsterWeapon(Monster Besitzer, int damage,int delay,int bulletSpeed){
		super(Besitzer, damage, delay, bulletSpeed);
		ActiveWeapons.add(this);
	}


	@Override
	public void shoot(double playerX, double playerY, double bulletW, double bulletH, double angle) {
		if(delay <= 0){
			delay = INIT_DELAY;
			kugeln.add(new MonsterBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, angle));
		}
	}

	@Override
	public abstract void shoot(double monsterX, double monsterY);

	@Override
	public void terminate(){
		ActiveWeapons.remove(this);
	}
}
