package me.game.pack;

import me.game.monsters.BigBossMonster;
import me.game.monsters.FlatMonster;
import me.game.monsters.HelixMonster;
import me.game.monsters.MediumMonster;
import me.game.monsters.SmallMonster;
import me.game.monsters.TinyMonster;

public class MonsterWaves {
	public static void SpawnWave(int which){
		Frame game = Frame.getInstance();
		Drop.weaponUpgradesDroppedThisWave = 0;
		/*int modifier = 1;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled)
			modifier = 2;*/
		// Reset current movement 
		game.movetick = 0;
		game.Monster_Direction = 0;
		switch(which%6){
		case 0: 
			SmallMonster.spawnSmallMonsterWave(which);
			break;

		case 1: 
			FlatMonster.spawnFlatMonsterWave(which);
			break;
		
		case 2: 
			MediumMonster.spawnMediumMonsterWave(which);
			break;
		
		case 3: 
			TinyMonster.spawnTinyMonsterWave(which);
			break;


		case 4:
			BigBossMonster.spawnBigBossMonsterWave(which);
			break;
			
		case 5:
			HelixMonster.spawnHelixMonsterWave(which);
			break;
		}
	}
}

