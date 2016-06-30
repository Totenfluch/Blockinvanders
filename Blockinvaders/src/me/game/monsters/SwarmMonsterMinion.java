package me.game.monsters;


import javafx.scene.paint.Color;
import me.game.bullets.Bullet;
import me.game.characters.Monster;
import me.game.characters.Player;
import me.game.pack.MonsterWaves;

public class SwarmMonsterMinion extends Monster{
	
	private SwarmMonster host;
	
	public SwarmMonsterMinion(int level, double xPos, double yPos, SwarmMonster host){
		super(null, 11+level*5, xPos, yPos, 30, 20, 1+level, Color.BROWN, 0.25, 20);
		//hisWeapon = new MonsterStandardWeapon(this);
		host.swarm.add(this);
		monsterType = MonsterWaves.MonsterType.SWARMMONSTERMINION;
		this.host = host;
		if(game.Coop_enabled || game.Online_Coop || game.Play_with_bot_enabled){
			setInitHp(getInitHp()*2);
			setLife(getInitHp());
		}
	}
	
	@Override
	public void refresh(){
		if (game.movetick == 500 && game.Monster_Direction == 0) {
			game.Monster_Direction = 1;
			game.movetick = 0;
		} else if (game.movetick == 500 && game.Monster_Direction == 1) {
			game.Monster_Direction = 0;
			game.movetick = 0;
		}

		if (game.Monster_Direction == 1)
			moveRight();
		else if (game.Monster_Direction == 0) 
			moveLeft();
		
		if(yPos <= host.getY()-10-height && xPos < host.getX()+host.getWidth()+10)
			moveRight();
		else if(xPos >= host.getX() && (yPos <= host.getY() || yPos < host.getY()+host.getHeight()+10))
			moveDown();
		else if(yPos > host.getY() && xPos > host.getX()-width-10 && xPos < host.getX()+host.getWidth()+width+10)
			moveLeft();
		else if(yPos > host.getY()-10-height)
			moveUp();
		
		for(Player p : game.Players){
			for(Bullet pb : p.getHisWeapon().getKugeln())
				if(pb.checkHit(xPos, yPos, width, height)){
					triggerDeath();
				}
		}
	}
	
	@Override
	public void triggerDeath(){
		super.triggerDeath();
		host.hisWeapon.shoot(xPos, yPos);
		host.swarm.remove(this);
	}
	
}
