package me.game.pack;

import java.util.Iterator;

import javafx.scene.paint.Color;
import me.game.bullets.Bullet;
import me.game.weapons.MonsterWeapon;

public class BotKI {
	private Player bot;
	private boolean inRight = true;
	
	private Bullet escapeThis = null;
	public BotKI(Player p){
		this.bot = p;
	}

	public void Refresh(){
		boolean escapeLock = false;
		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
			for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
				escapeLock = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x).equals(escapeThis);
				if(escapeLock)
					break;
			}
		}
		
		if(!checkForBullets() && !escapeLock ){
			moveToClosestEnemy();
			bot.getHisWeapon().shoot(bot.getX(), bot.getY());
		}
	}

	public boolean checkForBullets(){
		boolean dogeing = false;
		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
			for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
				Bullet bul = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
				if(bot.getY() - bul.getyPos() < 200){
					if(bot.getX() - bul.getxPos() < 5 && bot.getX() - bul.getxPos() > -bot.getWidth()-5){
						escapeThis = bul;
						if(bot.getX() < Frame.GAME_WIDTH/4)
							inRight = true;
						if(bot.getX() > Frame.GAME_WIDTH/4*3)
							inRight = false;
						if(inRight)
							bot.moveRight();
						else
							bot.moveLeft();
						dogeing = true;
						for(Monster p: Frame.Monsters)
							p.setColor(Color.GREENYELLOW);
					}
				}
			}
		}
		return dogeing;
	}
	
	public void moveToClosestEnemy(){
		double closest = 2000;
		double wheretogo = 0;
		Iterator<Monster> it = Frame.Monsters.iterator();
		while(it.hasNext()){
			Monster monti = it.next();
			if(Math.abs(monti.getX()-bot.getX()) < closest){
				closest = Math.abs(monti.getX()-bot.getX());
				wheretogo = monti.getX();
				for(Monster i: Frame.Monsters)
					i.setColor(Color.BROWN);
				monti.setColor(Color.BLUE);

				double timevalue = (bot.getY()-monti.getY())/bot.getHisWeapon().getBulletSpeed();
				if(Frame.Monster_Direction == 1){
					wheretogo+=timevalue;
				}else{
					wheretogo-=timevalue;
				}
			}
		}
		if(bot.getX() > wheretogo)
			bot.moveLeft();
		else
			bot.moveRight();
	}
}
