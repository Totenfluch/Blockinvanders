package me.game.monsterWeapons;

import java.util.Random;

import me.game.characters.Monster;

public class MonsterStandardWeapon extends MonsterWeapon{
	double bulletW = 5.0,
			bulletH = 5.0;
	public MonsterStandardWeapon(Monster Besitzer) {
		super(Besitzer, 1, 12, 4);
	}

	@Override
	public void shoot(double monsterX, double monsterY) {
		super.shoot(monsterX, monsterY, bulletW, bulletH, new Random().nextInt(10)-5);
	}
}
