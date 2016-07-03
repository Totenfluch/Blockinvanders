package me.game.monsters;

import java.util.Random;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterStandardWeapon;
import me.game.pack.Frame;
import me.game.pack.MonsterWaves;

public class RandomMovementMonster extends Monster{

	private enum moveSequence{UP, RIGHT, DOWN, LEFT};
	private moveSequence whereToMove;
	private int moveTick = 0;
	private final int moveSequenceTime = 50;
	private moveSequence prevSequence = moveSequence.UP;

	public RandomMovementMonster(int level, double xPos, double yPos){
		super(null, 10+level*4, xPos, yPos, 40, 40, 2+level, Color.BURLYWOOD, 0.25, 20);
		hisWeapon = new MonsterStandardWeapon(this);
		game.Monsters.add(this);
		monsterType = MonsterWaves.MonsterType.RANDOMMONSTER;
		whereToMove = moveSequence.values()[new Random().nextInt(4)];
		moveTick = new Random().nextInt(45)+30;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}

	@Override
	public void doMovement(){
		if(moveTick-- == 0){
			moveTick = moveSequenceTime + new Random().nextInt(15)+10;
			whereToMove = moveSequence.values()[new Random().nextInt(4)];
			switch(prevSequence){
			case UP:
				while(whereToMove == moveSequence.DOWN)
					whereToMove = moveSequence.values()[new Random().nextInt(4)];
				break;
			case LEFT:
				while(whereToMove == moveSequence.RIGHT)
					whereToMove = moveSequence.values()[new Random().nextInt(4)];
				break;
			case RIGHT:
				while(whereToMove == moveSequence.LEFT)
					whereToMove = moveSequence.values()[new Random().nextInt(4)];
				break;
			case DOWN:
				while(whereToMove == moveSequence.UP)
					whereToMove = moveSequence.values()[new Random().nextInt(4)];
				break;
			}
			prevSequence = whereToMove;
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
