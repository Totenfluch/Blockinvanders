package me.game.pack;

import java.util.Random;
import java.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import me.game.playerWeapons.PlayerWeapon;
import me.game.playerWeapons.RocketLauncher;
import me.game.playerWeapons.PlayerWeapon.WeaponType;

public class Drop implements Drawable{
	final Frame game;
	protected double xPos, yPos;
	public static final int DropSizeX = 10;
	public static final int DropSizeY = 10;
	protected Drops drop;
	public static Vector<Drop> AllDrops = new Vector<Drop>();
	public static final int weaponUpgradeDropLimitPerWave = 2;
	public static int weaponUpgradesDroppedThisWave = 0;

	protected enum Drops{
		NEXTWEAPON,
		ADDLEBEN,
		ADDAMMO,
		ADDSCORE;
	}

	public double getxPos() {
		return xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public Drop(double xPos, double yPos, Drops drop){
		this.game = Frame.getInstance();
		this.xPos = xPos;
		this.yPos = yPos;
		if(this.getDroptype() == Drops.NEXTWEAPON){
			Drops[] theDrops = Drops.values();
			Random r = new Random();
			if(weaponUpgradesDroppedThisWave < weaponUpgradeDropLimitPerWave ){
				this.drop = drop;
			}else{
				this.drop = theDrops[r.nextInt(theDrops.length-1)+1];
			}
		}else{
			this.drop = drop;
		}

		if(this.getDroptype() == Drops.NEXTWEAPON)
			weaponUpgradesDroppedThisWave++;
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
			if(p.getHisWeapon().getWeaponType() == WeaponType.MAX)
				if(p.getHisSpecialWeapon() != null)
					p.giveSpecialWeapon(new RocketLauncher(p, p.getHisSpecialWeapon().getAmmo()+3));
				else
					p.giveSpecialWeapon(new RocketLauncher(p, 3));
			else
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

		for(int i = 0; i<game.Players.length; i++){
			Player Peter = game.Players[i];
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

	@Override
	public void draw(GraphicsContext gc) {
		if (drop.equals(Drops.NEXTWEAPON))
			gc.setFill(Color.GOLD);
		else if (drop.equals(Drops.ADDAMMO))
			gc.setFill(Color.AQUAMARINE);
		else if (drop.equals(Drops.ADDLEBEN))
			gc.setFill(Color.FIREBRICK);
		else if (drop.equals(Drops.ADDSCORE))
			gc.setFill(Color.BLUE);
		gc.fillOval(xPos, yPos, DropSizeX, DropSizeY);
	}
}
