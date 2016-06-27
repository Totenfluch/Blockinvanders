package me.game.monsterWeapons;

import me.game.bullets.WallMonsterBullet;
import me.game.characters.Monster;

public class MonsterWallMonsterWeapon extends MonsterWeapon{

	public MonsterWallMonsterWeapon(Monster Besitzer) {
		super(Besitzer, 10, 20, 0.25);
	}

	@Override
	public void shoot(double monsterX, double monsterY) {
		for(int i = 1; i < 1920; i+=8)
			kugeln.add(new WallMonsterBullet(this, i, 10, 6, 6, bulletSpeed, damage, 0));
	}

}
