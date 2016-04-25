package me.game.pack;

import java.util.Iterator;

import javafx.scene.paint.Color;
import me.game.bullets.Bullet;
import me.game.pack.Drop.Drops;
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
		
		bot.getHisWeapon().shoot(bot.getX(), bot.getY());
		if(!checkForBullets() && !escapeLock ){
			if(!findWeaponUpgrade()){
				moveToClosestEnemy();
			}
		}
		if(Frame.Monsters.size() > 10 && bot.getHisSpecialWeapon().getAmmo() > 0)
			bot.getHisSpecialWeapon().shoot(bot.getX(), bot.getY());
	}

	public boolean checkForBullets(){
		// CDT boxes
		/*Frame.gc.beginPath();
		Frame.gc.setLineWidth(2.0);
		Frame.gc.setStroke(Color.RED);
		Frame.gc.setFill(Color.RED);
		Frame.gc.rect(bot.getX()-20, bot.getY()-200, bot.getWidth()+20, 400);
		Frame.gc.stroke();
		Frame.gc.closePath();*/
		
		boolean dogeing = false;
		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
			for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
				Bullet bul = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
				if(bot.getY() - bul.getyPos() < 200){
					if(bot.getX() - bul.getxPos() < 20 && bot.getX() - bul.getxPos() > -bot.getWidth()-20){
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
		Monster monti2 = null;
		while(it.hasNext()){
			Monster monti = it.next();
			if(Math.abs(monti.getX()-bot.getX()) < closest){
				closest = Math.abs(monti.getX()-bot.getX());
				wheretogo = monti.getX();
				for(Monster i: Frame.Monsters)
					i.setColor(Color.BROWN);
				monti.setColor(Color.BLUE);
				monti2 = monti;
			}
		}
		if(monti2 == null)
			return;
		double timevalue = (bot.getY()-monti2.getY())/bot.getHisWeapon().getBulletSpeed();
		if(Frame.Monster_Direction == 1){
			wheretogo+=timevalue;
		}else{
			wheretogo-=timevalue;
		}
		
		if(bot.getX() > wheretogo)
			bot.moveLeft();
		else
			bot.moveRight();
	}
	
	public boolean findWeaponUpgrade(){
		Drop p = null;
		for(int i = 0; i<Drop.AllDrops.size(); i++){
			if(Drop.AllDrops.elementAt(i).getDroptype() == Drops.NEXTWEAPON)
				p = Drop.AllDrops.elementAt(i);
		}
		if(p == null)
			return false;
		else if(p.getyPos() >= bot.getY())
			return false;
		double wheretogo = p.xPos;
		if(bot.getX() > wheretogo)
			bot.moveLeft();
		else
			bot.moveRight();
		for(Monster ppp: Frame.Monsters)
			ppp.setColor(Color.YELLOW);
		return true;
	}
}
