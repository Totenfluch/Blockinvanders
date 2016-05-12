package me.game.monsterWeapons;


import me.game.bullets.MonsterBullet;
import me.game.characters.Monster;

public class MonsterBossWeapon extends MonsterWeapon{
	double bulletW = 10.0,
			bulletH = 10.0;
	public MonsterBossWeapon(Monster Besitzer) {
		super(Besitzer, 2, 12, 2);
	}

	@Override
	public void shoot(double monsterX, double monsterY) {
		for(int i = -90; i<90; i+= 10)
			kugeln.add(new MonsterBullet(this, monsterX, monsterY, bulletH, bulletW, bulletSpeed, damage, i));
	}
}
