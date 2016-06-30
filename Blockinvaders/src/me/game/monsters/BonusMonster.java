package me.game.monsters;

import java.util.Random;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterStandardWeapon;
import me.game.pack.Frame;

public class BonusMonster extends Monster{
	public BonusMonster(int level, double xPos, double yPos){
		super(null, 100+level*10, xPos, yPos, 80-(level/10)*4, 80-(level/10)*4, 8*level, Color.GOLD, 0.0001, 1);
		hisWeapon = new MonsterStandardWeapon(this);
		game.Monsters.add(this);
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}
	
	public static void spawnBonusMonsterWave(int level){
		Random r = new Random();
		new BonusMonster(level, r.nextInt(600)+460, 80);
		new BonusMonster(level, r.nextInt(600)+460, 170);
		new BonusMonster(level, r.nextInt(600)+460, 260);
		new BonusMonster(level, r.nextInt(600)+460, 350);
		new BonusMonster(level, r.nextInt(600)+460, 440);
		new BonusMonster(level, r.nextInt(600)+460, 530);
	}
	
	@Override
	public void doMovement(){
		if(xPos >= Frame.GAME_WIDTH/2)
			xPos++;
		if(xPos < Frame.GAME_WIDTH/2)
			xPos--;
		if(xPos < -width)
			triggerDeath();
		if(xPos > Frame.GAME_WIDTH)
			triggerDeath();
	}
	
	
	
	
}
