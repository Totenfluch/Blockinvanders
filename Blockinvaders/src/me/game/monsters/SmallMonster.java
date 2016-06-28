package me.game.monsters;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterStandardWeapon;
import me.game.pack.MonsterWaves;

public class SmallMonster extends Monster{

	public SmallMonster(int level, double xPos, double yPos){
		super(null, 11+level*5, xPos, yPos, 30, 20, 1+level, Color.BROWN, 0.25, 20);
		hisWeapon = new MonsterStandardWeapon(this);
		game.Monsters.add(this);
		monsterType = MonsterWaves.MonsterType.SMALLMONSTER;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}
	
	public static void spawnSmallMonsterWave(int level){
		int x = 0;
		int ix = 0;
		for(int i = 0; i<64; i++){
			ix++;
			if(i%16 == 0){
				x++;
				ix = 0;
			}
			int sub = 0;
			if(x%2 == 0)
				sub = 50;
		
			new SmallMonster(level, sub+600+ix*50, x*100+50);
		}
	}
	
}
