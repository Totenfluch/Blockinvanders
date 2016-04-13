package me.game.pack;

import javafx.scene.paint.Color;
import me.game.weapons.MonsterStandardWaffe;

public class MonsterWaves {
	public static void SpawnWave(int which){
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

				if(!Frame.Coop_enabled)
					Frame.Monster_HP /= 2;
				Monster tempi = new Monster(null, Frame.Monster_HP, sub+600+ix*50, x*100+50, 30, 20, 1, Color.BROWN);
				Frame.Monsters.add(tempi);
				tempi.giveWeapon(new MonsterStandardWaffe(tempi));
			}
		}

		case 1: {
			int p  = 0;
			int ip = 0;
			for(int i = 0; i<32; i++){
				ip++;
				if(i%8 == 0){
					p++;
					ip = 0;
				}
				
				int sub = 0;
				if(i%2 == 0)
					sub = 100;

				if(!Frame.Coop_enabled)
					Frame.Monster_HP /= 2;
				Monster tempi = new Monster(null, Frame.Monster_HP, sub+200+ip*150, p*100+50, 60, 40, 3, Color.BROWN);
				Frame.Monsters.add(tempi);
				tempi.giveWeapon(new MonsterStandardWaffe(tempi));
			}
		}
		}
	}
}

