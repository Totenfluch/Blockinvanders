package me.game.monsterWeapons;

import java.util.Random;

import me.game.pack.Monster;
import me.game.playerWeapons.MonsterWeapon;

public class MonsterStandardWaffe extends MonsterWeapon{
	double bulletW = 5.0,
			bulletH = 5.0;
	public MonsterStandardWaffe(Monster Besitzer) {
		super(Besitzer, 1, 12, 4);
	}

	@Override
	public void shoot(double monsterX, double monsterY) {
		super.shoot(monsterX, monsterY, bulletW, bulletH, new Random().nextInt(10)-5);
	}
}
