package me.game.weapons;


import me.game.bullets.DoubleHelixBullet;
import me.game.pack.Monster;

public class MonsterDoubleHelixWeapon extends MonsterWeapon{
	double bulletW = 5.0,
			bulletH = 5.0;
	public MonsterDoubleHelixWeapon(Monster Besitzer) {
		super(Besitzer, 1, 12, 1);
	}

	@Override
	public void shoot(double monsterX, double monsterY) {
		kugeln.add(new DoubleHelixBullet(this, monsterX, monsterY, bulletH+2, bulletW+2, bulletSpeed, damage, 0, true));
		kugeln.add(new DoubleHelixBullet(this, monsterX, monsterY, bulletH+2, bulletW+2, bulletSpeed, damage, 0, false));
	}
}
