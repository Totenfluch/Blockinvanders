package me.game.monsters;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterStandardWeapon;

public class TinyMonster extends Monster{
	public TinyMonster(int level, double xPos, double yPos){
		super(null, (5+level)*3, xPos, yPos, 20, 40, 1+level, Color.CRIMSON, 0.125, 32);
		hisWeapon = new MonsterStandardWeapon(this);
		game.Monsters.add(this);
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}

	public static void spawnTinyMonsterWave(int level){
		int x = 0;
		int ix = 0;
		for(int i = 0; i<128; i++){
			ix++;
			if(i%32 == 0){
				x++;
				ix = 0;
			}

			int sub = 0;
			if(x%2 == 0)
				sub = 50;

			new TinyMonster(level, 530+ix*30+sub, x*100+50);
		}
	}
}
