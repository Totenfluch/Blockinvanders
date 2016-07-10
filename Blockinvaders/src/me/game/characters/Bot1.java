package me.game.characters;

import java.util.Iterator;
import java.util.Vector;

import javafx.scene.paint.Color;
import me.game.bullets.Bullet;
import me.game.monsterWeapons.MonsterWeapon;
import me.game.pack.Drop;
import me.game.pack.Drop.Drops;
import me.game.pack.Frame;
import me.game.pack.MonsterWaves.MonsterType;

public class Bot1 implements BotKI{
	/*
		Bot to FIGHT FOR HONOR
		By Totenfluch~
	 */
	private Frame game;
	private Player bot;
	private static boolean Bot_debug = false;
	private Monster Target = null;
	private String status = "4711";

	private Bullet escapeThis = null;
	public Bot1(Player p){
		this.game = Frame.getInstance();
		this.bot = p;
	}

	@Override
	public void refresh(){
		status = "";
		if(!bot.isAlive())
			return;
		
		Vector<Bullet> scont = new Vector<Bullet>(400, 20);
		MonsterWeapon.ActiveWeapons.forEach(weap -> weap.getKugeln().forEach(bul -> scont.add(bul)));
		boolean escapeLock = scont.stream().anyMatch(b -> b.equals(escapeThis));
		
		if((!checkForBullets() && !escapeLock) || bot.isHitImmume() || (bot.isDowngradeImmume() && bot.getHisWeapon().getWeaponType().ordinal() < 5)){
			if(!findWeaponUpgrade()){
				moveToClosestEnemy();
				bot.getHisWeapon().shoot(bot.getX(), bot.getY());
			}else{
				boolean clear = game.Monsters.stream().noneMatch(b -> b.checkHit(bot.getX()-250, 0, bot.getWidth()+500, Frame.GAME_LENGTH));

				if(bot.getHisWeapon().getAmmo() == 0 || !clear)
					bot.getHisWeapon().shoot(bot.getX(), bot.getY());
			}
		}else{
			bot.getHisWeapon().shoot(bot.getX(), bot.getY());
		}
		
		// Rocketlauncher
		MonsterType waveType = null;
		if(game.Monsters.size() > 0)
			waveType = game.Monsters.elementAt(0).getMonsterType();
		else
			return;
		if(bot.getLife() < 10 || bot.getHisWeapon().getWeaponType().ordinal() < 5 || (game.Monsters.size() >= waveType.getSpawnAmount()*0.8  || 
				((waveType == MonsterType.BIGBOSSMONSTER || waveType == MonsterType.SWARMMONSTER || waveType == MonsterType.FLATMONSTER) && game.clearcount > 20)) 
				&& bot.getHisSpecialWeapon().getAmmo() > 0)
			if((bot.getX() > 600 && bot.getX() < 1000) || bot.getLife() < 10)
				bot.getHisSpecialWeapon().shoot(bot.getX(), bot.getY());

	}

	private boolean checkForBullets(){
		status = "checking for Bullets";
		boolean dogeing = false;
		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
			for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
				Bullet bul = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
				if(bot.getY() - bul.getyPos() < 200){
					if(bul.checkHit(bot.getX()-10, bot.getY()-bot.getHeight(), bot.getWidth()+20, bot.height*2)){
						escapeThis = bul;

						if(!tryMoveRight())
							tryMoveLeft();

						dogeing = true;
						if(isBot_debug())
							for(Monster p: game.Monsters)
								p.setColor(Color.GREENYELLOW);
					}
				}
			}
		}
		return dogeing;
	}

	private Monster findTarget(){
		status = "finding target";
		Monster monti2 = null;
		double closest = 5000;
		Iterator<Monster> it = game.Monsters.iterator();
		while(it.hasNext()){
			Monster monti = it.next();
			double distanceToMonti = Math.abs(monti.getX()-bot.getX());
			if( distanceToMonti < closest){
				closest = distanceToMonti;
				if(isBot_debug())
					for(Monster i: game.Monsters)
						i.setColor(Color.BROWN);
				if(isBot_debug())
					monti.setColor(Color.BLUE);
				monti2 = monti;
			}
		}
		return monti2;
	}

	private void moveToClosestEnemy(){
		status = "moving to closest Enemy";
		Monster monti2 = null;
		double wheretogo = 0;
		
		if(Target == null){
			monti2 = findTarget();
			Target = monti2;
		}else if(!Target.isAlive() || !Frame.getInstance().Monsters.contains(monti2)){
			monti2 = findTarget();
			Target = monti2;
		}else
			monti2 = Target;
		
		
		if(monti2 == null || Target == null)
			return;
		if(!monti2.isAlive() || !Target.isAlive())
			return;
		
		wheretogo = monti2.getX();
		double timevalue = (bot.getY()-monti2.getY())/bot.getHisWeapon().getBulletSpeed();
		if(game.Monster_Direction == 1)
			wheretogo+=timevalue;
		else
			wheretogo-=timevalue;
		

		if(bot.getX() > wheretogo)
			tryMoveLeft();
		else
			tryMoveRight();
	}

	private boolean findWeaponUpgrade(){
		status = "finding Weapon Upgrade";
		Drop p = null;
		for(int i = 0; i<Drop.AllDrops.size(); i++){
			if(Drop.AllDrops.elementAt(i).getDroptype() == Drops.NEXTWEAPON){
				p = Drop.AllDrops.elementAt(i);
				break;
			}
		}
		if(p == null){
			int closest=5000;
			Drop closestDrop = null;
			for(int i = 0; i<Drop.AllDrops.size(); i++){
				Drop n = Drop.AllDrops.elementAt(i);
				int dist = (int)bot.getX()-(int)n.getxPos();
				if(i==0){
					closest = dist;
					closestDrop = n;
				}else
					if(dist < closest){
						closestDrop = n;
						closest = dist;
					}
			}
			if(closest < 200 && bot.getY()-closestDrop.getyPos() < 400)
				p = closestDrop;
		}
		if(p == null)
			return false;
		else if(p.getyPos() >= bot.getY())
			return false;
		else if(bot.getY()-p.getyPos() > 500)
			return false;
		double wheretogo = p.getxPos();
		if(bot.getX() > wheretogo)
			tryMoveLeft();
		else
			tryMoveRight();
		if(isBot_debug())
			for(Monster ppp: game.Monsters)
				ppp.setColor(Color.YELLOW);
		return true;
	}

	private boolean tryMoveRight(){
		boolean clear = true;
		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
			for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
				Bullet bul = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
				if(bul.checkHit(bot.getX()+bot.width, bot.getY()-bot.getHeight(), bul.getWidth()+10, bot.height*2)){
					clear = false;
				}
			}
		}
		if(clear)
			bot.moveRight();
		return clear;
	}

	private boolean tryMoveLeft(){
		boolean clear = true;
		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
			for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
				Bullet bul = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
				if(bul.checkHit(bot.getX(), bot.getY()-bot.getHeight(), -bul.getWidth()-10, bot.height*2)){
					clear = false;
				}
			}
		}
		if(clear)
			bot.moveLeft();
		return clear;
	}
	
	@Override
	public String toString(){
		return status;
	}

	public boolean isBot_debug() {
		return Bot_debug;
	}

	public static void setBot_debug(boolean bot_debug) {
		Bot_debug = bot_debug;
	}
}
