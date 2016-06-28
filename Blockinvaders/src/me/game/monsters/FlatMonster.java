package me.game.monsters;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterTier2Weapon;
import me.game.pack.MonsterWaves;

public class FlatMonster extends Monster{
	public FlatMonster(int level, double xPos, double yPos){
		super(null, 250+level*10, xPos, yPos, 200, 40, 3*level, Color.DARKSLATEGRAY, 1, 4);
		hisWeapon = new MonsterTier2Weapon(this);
		game.Monsters.add(this);
		monsterType = MonsterWaves.MonsterType.FLATMONSTER;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}
	
	public static void spawnFlatMonsterWave(int level){
		int p  = 0;
		int ip = 0;
		for(int i = 0; i<12; i++){
			ip++;
			if(i%4 == 0){
				p++;
				ip = 0;
			}

			new FlatMonster(level, 600+ip*250, p*100+50);
		}
	}
}
