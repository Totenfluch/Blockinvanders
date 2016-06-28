package me.game.monsters;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterDoubleHelixWeapon;
import me.game.pack.MonsterWaves;

public class HelixMonster extends Monster{
	public HelixMonster(int level, double xPos, double yPos){
		super(null, 600+level*35, xPos, yPos, 300, 125, 2*level, Color.CORAL, 0.5, 2);
		hisWeapon = new MonsterDoubleHelixWeapon(this);
		game.Monsters.add(this);
		monsterType = MonsterWaves.MonsterType.HELIXMONSTER;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}
	
	public static void spawnHelixMonsterWave(int level){
		new HelixMonster(level, 650, 150);
		new HelixMonster(level, 1000, 150);
		new HelixMonster(level, 650, 300);
		new HelixMonster(level, 1000, 300);
	}
}
