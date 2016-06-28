package me.game.pack;

import me.game.monsters.BigBossMonster;
import me.game.monsters.BonusMonster;
import me.game.monsters.FlatMonster;
import me.game.monsters.HelixMonster;
import me.game.monsters.MediumMonster;
import me.game.monsters.RandomMovementMonster;
import me.game.monsters.SmallMonster;
import me.game.monsters.SplitMonster;
import me.game.monsters.SwarmMonster;
import me.game.monsters.TinyMonster;
import me.game.monsters.WallMonster;

public class MonsterWaves {
	
	public enum MonsterType{DEFAULT, SMALLMONSTER, FLATMONSTER, MEDIUMMONSTER, TINYMONSTER, BIGBOSSMONSTER, HELIXMONSTER, SWARMMONSTER, SWARMMONSTERMINION, RANDOMMONSTER, WALLMONSTER, SPLITMONSTER, BONUSMONSTER};
	
	public static void SpawnWave(int which){
		Frame game = Frame.getInstance();
		Drop.weaponUpgradesDroppedThisWave = 0;
		/*int modifier = 1;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled)
			modifier = 2;*/
		// Reset current movement 
		game.movetick = 0;
		game.Monster_Direction = 0;
		switch(which%11){
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
			
		case 6:
			SwarmMonster.spawnSwarmMonsterWave(which);
			break;
			
		case 7:
			RandomMovementMonster.spawnRandomMovementMonsterWave(which);
			break;
			
		case 8:
			WallMonster.spawnWallMonsterWave(which);
			break;
		
		case 9:
			SplitMonster.spawnSplitMonsterWave(which);
			break;
			
		case 10:
			BonusMonster.spawnBonusMonsterWave(which);
			break;
		}
	}
}

