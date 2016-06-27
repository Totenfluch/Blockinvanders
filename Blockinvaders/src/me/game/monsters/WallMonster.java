package me.game.monsters;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterWallMonsterWeapon;

public class WallMonster extends Monster{
	public WallMonster(int level, double xPos, double yPos){
		super(null, 3000+level*70, xPos, yPos, 1200, 400, 20+level*2, Color.HOTPINK, 0.25, 20);
		hisWeapon = new MonsterWallMonsterWeapon(this);
		game.Monsters.add(this);
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
		hisWeapon.shoot(0, 0);
	}
	
	public static void spawnWallMonsterWave(int level){
		new WallMonster(level, 250, 200);
	}
	
	@Override
	public void doMovement(){};
	
	@Override
	public void triggerDeath(){
		super.triggerDeath();
		hisWeapon.getKugeln().clear();
	}

}
