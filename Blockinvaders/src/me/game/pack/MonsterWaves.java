package me.game.pack;

import javafx.scene.paint.Color;
import me.game.weapons.MonsterStandardWaffe;
import me.game.weapons.Tier2MonsterWeapon;

public class MonsterWaves {
	public static void SpawnWave(int which){
		// Reset current movement 
		Frame.movetick = 0;
		Frame.Monster_Direction = 0;
		switch(which%2){
		case 0: {
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

				//if(!Frame.Coop_enabled)
				//	Frame.Monster_HP /= 2;
				Monster tempi = new Monster(null, 10+which*5, sub+600+ix*50, x*100+50, 30, 20, 1+which, Color.BROWN);
				Frame.Monsters.add(tempi);
				tempi.giveWeapon(new MonsterStandardWaffe(tempi));
			}
			break;
		}

		case 1: {
			int p  = 0;
			int ip = 0;
			for(int i = 0; i<12; i++){
				ip++;
				if(i%4 == 0){
					p++;
					ip = 0;
				}
				
				//if(!Frame.Coop_enabled)
				//	Frame.Monster_HP /= 2;
				Monster tempi = new Monster(null, 60+which*20, 600+ip*250, p*100+50, 200, 40, 3*which, Color.DARKSLATEGRAY);
				Frame.Monsters.add(tempi);
				tempi.giveWeapon(new Tier2MonsterWeapon(tempi));
			}
			break;
		}
		
		}
	}
}

