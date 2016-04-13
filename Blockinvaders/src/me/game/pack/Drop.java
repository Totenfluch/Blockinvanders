package me.game.pack;

import java.util.Random;
import java.util.Vector;

import javafx.scene.shape.Rectangle;
import me.game.weapons.PlayerWeapon;

public class Drop {
	protected double xPos, yPos;
	public static final int DropSizeX = 10;
	public static final int DropSizeY = 10;
	protected Drops drop;
	public static Vector<Drop> AllDrops = new Vector<Drop>();
	
	protected enum Drops{
		ADDLEBEN,
		ADDAMMO,
		NEXTWEAPON,
		ADDSCORE;
		}
	
	public Drop(double xPos, double yPos, Drops drop){
		this.xPos = xPos;
		this.yPos = yPos;
		this.drop = drop;
	}
	
	public Drops getDroptype(){
		return drop;
	}
	
	public void assign(Player p){
		Random r = new Random();
		
		switch (drop) {
		case ADDLEBEN:
			p.addLeben();
			break;
			
		case ADDAMMO:
			p.hisWeapon.addAmmo(20+r.nextInt(50));
			break;
			
		case NEXTWEAPON:
			p.giveWeapon(((PlayerWeapon) p.hisWeapon).nextWaffe());
			break;
		case ADDSCORE:
			p.incScore(1+r.nextInt(20));
			break;
		default:
			break;
		}
		
	}
	
	public void refresh(){
		yPos++;
		if(yPos > Frame.GAME_WIDTH){
			AllDrops.remove(this);
			return;
		}
		
		for(int i = 0; i<Frame.Players.length; i++){
			Player Peter = Frame.Players[i];
			if(!Peter.isAlive())
				continue;
			if(checkHit(Peter.getX(), Peter.getY(), Peter.getWidth(), Peter.getHeight())){
				assign(Peter);
				AllDrops.remove(this);
			}
		}
	}
	
	public boolean checkHit(double ox, double oy, double owidth, double olength){
		Rectangle rt1 = new Rectangle((int)ox, (int)oy, (int)owidth, (int)olength);
		Rectangle rt2 = new Rectangle((int)xPos, (int)yPos, DropSizeX, DropSizeY);
		if(rt1.intersects(rt2.getBoundsInLocal()))
			return true;
		return false;
	}
}
