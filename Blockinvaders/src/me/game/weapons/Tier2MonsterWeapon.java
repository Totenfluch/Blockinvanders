package me.game.weapons;

import java.util.Random;

import me.game.bullets.MonsterBullet;
import me.game.pack.Monster;

public class Tier2MonsterWeapon extends MonsterWeapon{
	double bulletW = 5.0,
			bulletH = 5.0;
	public Tier2MonsterWeapon(Monster Besitzer) {
		super(Besitzer, 2, 12, 4);
	}

	@Override
	public void shoot(double monsterX, double monsterY) {
		kugeln.add(new MonsterBullet(this, monsterX+20, monsterY, bulletH+2, bulletW+8, bulletSpeed, damage, new Random().nextInt(20)-10));
		kugeln.add(new MonsterBullet(this, monsterX, monsterY, bulletH+2, bulletW+8, bulletSpeed, damage, new Random().nextInt(20)-10));
		kugeln.add(new MonsterBullet(this, monsterX-20, monsterY, bulletH+2, bulletW+8, bulletSpeed, damage, new Random().nextInt(20)-10));
	}
}
