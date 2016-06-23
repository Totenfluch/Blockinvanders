package me.game.monsters;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterBossWeapon;

public class BigBossMonster extends Monster{
	public BigBossMonster(int level, double xPos, double yPos){
		super(null, 2000+level*25, xPos, yPos, 700, 300, 5*level, Color.HOTPINK, 2.0, 1);
		hisWeapon = new MonsterBossWeapon(this);
		game.Monsters.add(this);
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}
	
	public static void spawnBigBossMonsterWave(int level){
		new BigBossMonster(level, 700, 150);
	}

}
