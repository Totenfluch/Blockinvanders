package me.game.bullets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.game.pack.Frame;
import me.game.pack.Monster;
import me.game.pack.Player;
import me.game.weapons.MonsterWeapon;
import me.game.weapons.PlayerWeapon;

public class DoubleHelixBullet extends MonsterBullet{

	boolean type;
	double wave=0;
	DoubleHelixBullet Partner;


	public DoubleHelixBullet(MonsterWeapon waffe, double xPos, double yPos, double height, double width, int speed, int damage,
			double angle, boolean type) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle);
		this.type = type;
	}

	public DoubleHelixBullet getPartner(){
		return Partner;
	}

	public void setPartner(DoubleHelixBullet Partner){
		this.Partner = Partner;
	}

	public void refresh(){
		if(yPos <= 0 || yPos >= Frame.GAME_LENGTH || xPos <= 0 || xPos >= Frame.GAME_WIDTH)
			waffe.getKugeln().remove(this);

		for (int x = 0; x < waffe.getOwner().game.Players.length; x++) {
			Player p = waffe.getOwner().game.Players[x];
			if (checkHit(p.getX(), p.getY(), p.getWidth(), p.getHeight())) {
				if (p.isAlive()) {
					waffe.getKugeln().remove(this);
					p.setLife(p.getLife()-waffe.getDamage());
					Monster m = (Monster) waffe.getOwner();
					m.setInitHp(m.getInitHp() + 300);
					m.setLife(m.getInitHp());
					m.setColor(Color.DARKMAGENTA);
					p.giveWeapon(((PlayerWeapon) p.hisWeapon).prevWaffe());
				}
			}
		}

		if(type){
			xPos = xPos+(Math.sin(wave+Math.PI)*3);
		}else{
			xPos = xPos+(Math.cos(wave)*3);
		}
		wave+=0.1;
		yPos += dy;
	}

	@Override
	public void draw(GraphicsContext gc) {
		if(type)
			gc.setFill(Color.INDIANRED);
		else
			gc.setFill(Color.INDIGO);
		gc.fillRect(xPos, yPos, width, height);

		gc.setFill(Color.GREEN);
		if(Partner != null)
			if(Partner.getxPos() > xPos)
				gc.fillRect(xPos+width, yPos+height/2, Partner.getxPos()-xPos-width, Partner.getyPos()-yPos+height/4);
			else
				gc.fillRect(xPos+width, yPos+height/2, xPos-Partner.getxPos()-width, Partner.getyPos()-yPos+height/4);

	}
}
