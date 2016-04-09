package me.game.pack;

import java.util.Random;

public class Drop {
	protected double xPos, yPos;
	protected Drops drop;
	
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
	
	public void assign(Spieler p){
		Random r = new Random();
		
		switch (drop) {
		case ADDLEBEN:
			p.addLeben();
			break;
			
		case ADDAMMO:
			p.waffe.addAmmo(20+r.nextInt(80));
			break;
			
		case NEXTWEAPON:
			p.giveWeapon(new Shotgun(p));
			break;
		case ADDSCORE:
			p.incScore(100+r.nextInt(400));
			break;
		default:
			break;
		}
		
	}
}
