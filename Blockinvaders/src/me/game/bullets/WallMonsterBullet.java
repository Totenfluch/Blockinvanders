package me.game.bullets;

import me.game.monsterWeapons.MonsterWeapon;

public class WallMonsterBullet extends MonsterBullet{

	
	public WallMonsterBullet(MonsterWeapon waffe, double xPos, double yPos, double height, double width, double speed,
			int damage, double angle) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle);
		super.xPos = xPos;
	}

}
