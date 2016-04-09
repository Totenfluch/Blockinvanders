package me.game.pack;

import java.util.Random;
import java.util.Vector;

import javafx.scene.shape.Rectangle;

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
	
	public void refresh(){
		yPos++;
		if(yPos > Frame.GAME_WIDTH){
			AllDrops.remove(this);
			return;
		}
		for(int i = 0; i<Frame.Players.size(); i++){
			Spieler Peter = Frame.Players.elementAt(i);
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