package me.game.monsters;

import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterStandardWeapon;

public class SplitMonster extends Monster{
	
	private int seq = 0;
	private int cLevel;
	
	public SplitMonster(int level, double xPos, double yPos, double width, double height, int seq){
		super(null, (3000+(level*25))/(seq*250), xPos, yPos, width, height, 2+level, Color.DARKOLIVEGREEN, 0.125, 32);
		hisWeapon = new MonsterStandardWeapon(this);
		game.Monsters.add(this);
		this.seq = seq;
		this.cLevel = level;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}
	
	public static void spawnSplitMonsterWave(int level){
		new SplitMonster(level, 500, 150, 900, 300, 1);
	}
	
	@Override
	public void triggerDeath(){
		int maxSeq = (cLevel/9)+1;
		maxSeq = maxSeq > 5 ? 5 : (cLevel/9)+1;
		if(seq < maxSeq){
			seq++;
			new SplitMonster(cLevel, xPos, yPos, width/2, height/2, seq);
			new SplitMonster(cLevel, xPos+width/2+10/seq, yPos, width/2-10/seq, height/2, seq);
			new SplitMonster(cLevel, xPos, yPos+height/2+10/seq, width/2, height/2-10/seq, seq);
			new SplitMonster(cLevel, xPos+width/2+10/seq, yPos+height/2+10/seq, width/2-10/seq, height/2-10/seq, seq);
		}
		super.triggerDeath();
	}
}
