package me.game.pack;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterBossWeapon;
import me.game.monsterWeapons.MonsterDoubleHelixWeapon;
import me.game.monsterWeapons.MonsterStandardWeapon;
import me.game.monsterWeapons.MonsterTier2Weapon;
import me.game.monsters.SmallMonster;

public class MonsterWaves {
	public static void SpawnWave(int which){
		Frame game = Frame.getInstance();
		Drop.weaponUpgradesDroppedThisWave = 0;
		int modifier = 1;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled)
			modifier = 2;
		// Reset current movement 
		game.movetick = 0;
		game.Monster_Direction = 0;
		switch(which%6){
		case 0: 
			SmallMonster.spawnSmallMonsterWave(which);
			break;

		case 1: {
			int p  = 0;
			int ip = 0;
			for(int i = 0; i<12; i++){
				ip++;
				if(i%4 == 0){
					p++;
					ip = 0;
				}

				//if(!game.Coop_enabled)
				//	game.Monster_HP /= 2;
				Monster tempi = new Monster(null, 80+which*20*modifier, 600+ip*250, p*100+50, 200, 40, 3*which, Color.DARKSLATEGRAY, 1, 4);
				game.Monsters.add(tempi);
				tempi.giveWeapon(new MonsterTier2Weapon(tempi));
			}
			break;
		}

		case 2: {
			int x = 0;
			int ix = 0;
			for(int i = 0; i<32; i++){
				ix++;
				if(i%8 == 0){
					x++;
					ix = 0;
				}

				//if(!game.Coop_enabled)
				//	game.Monster_HP /= 2;
				Monster tempi = new Monster(null, 30+which*10*modifier, 800+ix*70, x*100+50, 50, 20, 1+2*which, Color.CORAL, 0.5, 8);
				game.Monsters.add(tempi);
				tempi.giveWeapon(new MonsterStandardWeapon(tempi));
			}
			break;
		}

		case 3: {
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


				//if(!game.Coop_enabled)
				//	game.Monster_HP /= 2;
				Monster tempi = new Monster(null, 5+which*3*modifier, 530+ix*30+sub, x*100+50, 20, 40, 1+which, Color.CRIMSON, 0.125, 32);
				game.Monsters.add(tempi);
				tempi.giveWeapon(new MonsterStandardWeapon(tempi));
			}
			break;
		}

		case 4:{
			Monster tempi = new Monster(null, 2500+which*200*modifier, 700, 150, 700, 300, 3*which, Color.HOTPINK, 2.0, 1);
			game.Monsters.add(tempi);
			tempi.giveWeapon(new MonsterBossWeapon(tempi));
			break;
		}

		case 5:{
			Monster temp = new Monster(null, 500+which*50*modifier, 650, 150, 300, 125, 2*which, Color.CORAL, 0.5, 2);
			game.Monsters.add(temp);
			temp.giveWeapon(new MonsterDoubleHelixWeapon(temp));
			Monster temp1 = new Monster(null, 500+which*50*modifier, 1000, 150, 300, 125, 2*which, Color.CORAL, 0.5, 2);
			game.Monsters.add(temp1);
			temp1.giveWeapon(new MonsterDoubleHelixWeapon(temp1));
			Monster temp2 = new Monster(null, 500+which*50*modifier, 650, 300, 300, 125, 2*which, Color.CORAL, 0.5, 2);
			game.Monsters.add(temp2);
			temp2.giveWeapon(new MonsterDoubleHelixWeapon(temp2));
			Monster temp3 = new Monster(null, 500+which*50*modifier, 1000, 300, 300, 125, 2*which, Color.CORAL, 0.5, 6);
			game.Monsters.add(temp3);
			temp3.giveWeapon(new MonsterDoubleHelixWeapon(temp3));

			break;
		}

		}
	}
}

