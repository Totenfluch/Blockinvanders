package me.game.monsters;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterStandardWeapon;

public class MediumMonster extends Monster{
	public MediumMonster(int level, double xPos, double yPos){
		super(null, 90+level*10, xPos, yPos, 50, 20, 1+2*level, Color.CORAL, 0.5, 8);
		hisWeapon = new MonsterStandardWeapon(this);
		game.Monsters.add(this);
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}
	
	public static void spawnMediumMonsterWave(int level){
		int x = 0;
		int ix = 0;
		for(int i = 0; i<32; i++){
			ix++;
			if(i%8 == 0){
				x++;
				ix = 0;
			}

			new MediumMonster(level, 800+ix*70, x*100+50);
		}
	}
}
