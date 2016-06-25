package me.game.monsters;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.monsterWeapons.MonsterBossWeapon;

public class SwarmMonster extends Monster{

	protected ArrayList<Monster> swarm = new ArrayList<Monster>();
	private int level;
	private final int maxMonsters = 20;
	private final int spawnDelay = 500;
	private int currentSpawnDelay = 0;

	public SwarmMonster(int level, int xPos, int yPos){
		super(null, 1000+level*45, xPos, yPos, 400, 300, level*5, Color.KHAKI, 1.5, 1);
		hisWeapon = new MonsterBossWeapon(this);
		game.Monsters.add(this);
		this.level = level;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
		new SwarmMonsterMinion(level, xPos, yPos-30, this);
	}

	public static void spawnSwarmMonsterWave(int level){
		new SwarmMonster(level, 800, 200);
	}

	@Override
	public void refresh(){
		if(swarm.size() < maxMonsters)
			if(++currentSpawnDelay >= spawnDelay){
				//swarm.add(new SwarmMonsterMinion(level, xPos, yPos-30, this));
				currentSpawnDelay = 0;
			}
		swarm.forEach(Monster::refresh);
	}

	@Override
	public void draw(GraphicsContext gc) {
		if (alive) {
			gc.setFill(getColor());
			gc.fillRect(xPos, yPos, width, heigth);
			if (life != getInitHp()) {
				double fragleben = (double)life / (double) getInitHp();
				Color HpColor = new Color(1.0f * (1 - fragleben), 1.0f * fragleben, 0, 1);
				gc.setFill(HpColor);
				gc.fillRect(xPos + width / 10, yPos + heigth / 2.5,
						width * fragleben * 0.8, heigth - heigth * 0.8);
			}
		}
		for(Monster m : swarm)
			m.draw(gc);
	}
}
