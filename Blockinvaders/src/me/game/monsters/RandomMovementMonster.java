package me.game.monsters;

import java.util.Random;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterStandardWeapon;
import me.game.pack.Frame;

public class RandomMovementMonster extends Monster{
	
	private enum moveSequence{UP, RIGHT, DOWN, LEFT};
	private moveSequence whereToMove;
	private int moveTick = 0;
	
	public RandomMovementMonster(int level, double xPos, double yPos){
		super(null, 10+level*4, xPos, yPos, 40, 40, 2+level, Color.BURLYWOOD, 0.25, 20);
		hisWeapon = new MonsterStandardWeapon(this);
		game.Monsters.add(this);
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}
	
	@Override
	public void doMovement(){
		if(moveTick-- == 0){
			moveTick = 50;
			whereToMove = moveSequence.values()[new Random().nextInt(4)];
		}
		switch(whereToMove){
		case UP:
			moveUp();
			break;
		case RIGHT:
			moveRight();
			break;
		case DOWN:
			moveDown();
			break;
		case LEFT:
			moveLeft();
		}
	}
	
	public static void spawnRandomMovementMonsterWave(int level){
		Random r = new Random();
		for(int i = 0; i<16; i++)
			new RandomMovementMonster(level, r.nextInt(Frame.GAME_WIDTH-40), r.nextInt(Frame.GAME_LENGTH-340));
	}
}
