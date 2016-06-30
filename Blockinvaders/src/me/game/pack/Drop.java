package me.game.pack;

import java.util.Random;
import java.util.Vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import me.game.characters.Player;
import me.game.playerWeapons.PlayerWeapon;
import me.game.playerWeapons.PlayerWeapon.WeaponType;
import me.game.playerWeapons.RocketLauncher;

public class Drop implements Drawable{
	final Frame game;
	protected double xPos, yPos;
	public static final int DropSize = 10;
	protected Drops drop;
	public static Vector<Drop> AllDrops = new Vector<Drop>();
	public static final int weaponUpgradeDropLimitPerWave = 1;
	public static int weaponUpgradesDroppedThisWave = 0;

	public enum Drops{
		NEXTWEAPON,
		ADDLIFE,
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
		if(drop == Drops.NEXTWEAPON){
			Drops[] theDrops = Drops.values();
			Random r = new Random();
			if(weaponUpgradesDroppedThisWave < weaponUpgradeDropLimitPerWave ){
				this.drop = Drops.NEXTWEAPON;
			}else{
				this.drop = theDrops[r.nextInt(theDrops.length-1)+1];
			}
		}else{
			this.drop = drop;
		}

		if(this.getDroptype() == Drops.NEXTWEAPON)
			weaponUpgradesDroppedThisWave++;
		System.out.println(weaponUpgradesDroppedThisWave);
	}

	public Drops getDroptype(){
		return drop;
	}

	public void assign(Player p){
		Random r = new Random();

		switch (drop) {
		case ADDLIFE:
			p.setLife(p.getLife()+1);
			break;

		case ADDAMMO:
			p.hisWeapon.addAmmo(20+r.nextInt((game.clearcount+1)*2));
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
		return new Rectangle(ox, oy, owidth, olength).intersects(new Circle(xPos, yPos, DropSize).getBoundsInLocal());
	}

	@Override
	public void draw(GraphicsContext gc) {
		if (drop.equals(Drops.NEXTWEAPON))
			gc.setFill(Color.GOLD);
		else if (drop.equals(Drops.ADDAMMO))
			gc.setFill(Color.AQUAMARINE);
		else if (drop.equals(Drops.ADDLIFE))
			gc.setFill(Color.FIREBRICK);
		else if (drop.equals(Drops.ADDSCORE))
			gc.setFill(Color.BLUE);
		gc.fillOval(xPos, yPos, DropSize, DropSize);
	}
}
