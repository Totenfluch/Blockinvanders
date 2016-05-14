package me.game.characters;

import java.util.Iterator;
import java.util.Vector;

import javafx.scene.paint.Color;
import me.game.bullets.Bullet;
import me.game.monsterWeapons.MonsterWeapon;
import me.game.pack.Drop;
import me.game.pack.Drop.Drops;
import me.game.pack.Frame;

public class Bot1 implements BotKI, Controller{
	/*
		Bot to FIGHT FOR HONOR
		By Totenfluch~
	 */
	public final Frame game;
	private Player bot;
	public static boolean Bot_debug = false;
	public Monster Target = null;
	private Controllable controllable;

	private Bullet escapeThis = null;
	public Bot1(Player p){
		this.game = Frame.getInstance();
		this.bot = p;
		p.setController(this);
	}

	public void refresh(){
		if(!bot.isAlive())
			return;
		/*boolean oldEscapeLock = false;
		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
			for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
				oldEscapeLock = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x).equals(escapeThis);
				if(oldEscapeLock)
					break;
			}
		}*/
		
		Vector<Bullet> scont = new Vector<Bullet>(400, 20);
		MonsterWeapon.ActiveWeapons.forEach(weap -> weap.getKugeln().forEach(bul -> scont.add(bul)));
		boolean escapeLock = scont.stream().anyMatch(b -> b.equals(escapeThis));
		
		
		if(!checkForBullets() && !escapeLock ){
			if(!findWeaponUpgrade()){
				moveToClosestEnemy();
				bot.getHisWeapon().shoot(bot.getX(), bot.getY());
			}else{

				boolean clear = scont.stream().noneMatch(b -> b.checkHit(bot.getX()-10, 0, bot.getWidth()+20, Frame.GAME_LENGTH));
				//boolean oldclear = true;
				/*for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
					for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
						Bullet bul = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
						if(bul.checkHit(bot.getX()-10, 0, bot.getWidth()+20, Frame.GAME_LENGTH)){
							clear = false;
						}
					}
				}*/
				//System.out.println(clear + " " + newClear);
				if(bot.getHisWeapon().getAmmo() == 0 || !clear)
					bot.getHisWeapon().shoot(bot.getX(), bot.getY());
			}
		}else{
			bot.getHisWeapon().shoot(bot.getX(), bot.getY());
		}

		if((game.Monsters.size() > 10  || (game.clearcount%5==4 && game.clearcount > 20)) && bot.getHisSpecialWeapon().getAmmo() > 0)
			if((bot.getX() > 600 && bot.getX() < 1000))
				bot.getHisSpecialWeapon().shoot(bot.getX(), bot.getY());

	}

	public boolean checkForBullets(){
		boolean dogeing = false;
		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
			for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
				Bullet bul = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
				if(bot.getY() - bul.getyPos() < 200){
					if(bul.checkHit(bot.getX()-10, bot.getY()-bot.getHeight(), bot.getWidth()+20, bot.heigth*2)){
						escapeThis = bul;

						if(!tryMoveRight())
							tryMoveLeft();

						dogeing = true;
						if(Bot_debug)
							for(Monster p: game.Monsters)
								p.setColor(Color.GREENYELLOW);
					}
				}
			}
		}
		return dogeing;
	}

	public Monster findTarget(){
		Monster monti2 = null;
		double closest = 5000;
		Iterator<Monster> it = game.Monsters.iterator();
		while(it.hasNext()){
			Monster monti = it.next();
			double distanceToMonti = Math.abs(monti.getX()-bot.getX());
			if( distanceToMonti < closest){
				closest = distanceToMonti;
				if(Bot_debug)
					for(Monster i: game.Monsters)
						i.setColor(Color.BROWN);
				if(Bot_debug)
					monti.setColor(Color.BLUE);
				monti2 = monti;
			}
		}
		return monti2;
	}

	public void moveToClosestEnemy(){
		Monster monti2 = null;
		double wheretogo = 0;
		if(Target == null){
			monti2 = findTarget();
			Target = monti2;
		}else if(!Target.isAlive()){
			monti2 = findTarget();
			Target = monti2;
		}else{
			monti2 = Target;
		}
		if(monti2 == null)
			return;
		wheretogo = monti2.getX();
		if(!monti2.isAlive())
			return;
		double timevalue = (bot.getY()-monti2.getY())/bot.getHisWeapon().getBulletSpeed();
		if(game.Monster_Direction == 1){
			wheretogo+=timevalue;
		}else{
			wheretogo-=timevalue;
		}

		if(bot.getX() > wheretogo)
			tryMoveLeft();
		else
			tryMoveRight();
	}

	public boolean findWeaponUpgrade(){
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
		if(Bot_debug)
			for(Monster ppp: game.Monsters)
				ppp.setColor(Color.YELLOW);
		return true;
	}

	private boolean tryMoveRight(){
		boolean clear = true;
		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
			for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
				Bullet bul = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
				if(bul.checkHit(bot.getX()+bot.width, bot.getY()-bot.getHeight(), bul.getWidth()+10, bot.heigth*2)){
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
				if(bul.checkHit(bot.getX(), bot.getY()-bot.getHeight(), -bul.getWidth()-10, bot.heigth*2)){
					clear = false;
				}
			}
		}
		if(clear)
			bot.moveLeft();
		return clear;
	}
	
	@Override
	public void onSetControl(Controllable controllable) {
		this.controllable = controllable;
	}
}
