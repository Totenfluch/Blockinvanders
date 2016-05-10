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
	double wave = 0;
	int refreshcounter = 0;
	DoubleHelixBullet Partner;
	int helixcounter = 0;
	int removeInTicks = 40;
	double[][][] helix = new double[3000][2][2];


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
		if(refreshcounter >= 5){
			refreshcounter = 0;
			if(Partner != null){
				helix[helixcounter][0][0] = xPos;
				helix[helixcounter][0][1] = yPos;
				helix[helixcounter][1][0] = Partner.xPos;
				helix[helixcounter][1][1] = Partner.yPos;
			}
			helixcounter++;
		}
		refreshcounter++;
		if(yPos <= 0 || yPos >= Frame.GAME_LENGTH || xPos <= 0 || xPos >= Frame.GAME_WIDTH)
			removeInTicks--;
		
		if(removeInTicks == 0)
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
			xPos = xPos+(Math.sin(wave)*3);
		}else{
			xPos = xPos+(Math.sin(wave+Math.PI)*3);
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
				gc.fillRect(Partner.xPos+width, yPos+height/2, xPos-Partner.getxPos()-width, Partner.getyPos()-yPos+height/4);
		if(Partner != null){
			for(int i=0;i<helixcounter;i++){
				gc.setFill(Color.INDIANRED);
				gc.fillRect(helix[i][0][0], helix[i][0][1], width, height);
				gc.setFill(Color.INDIGO);
				gc.fillRect(helix[i][1][0], helix[i][1][1], width, height);
				if(removeInTicks >= 40)
					gc.setFill(Color.GREEN);
				else
					gc.setFill(Color.RED);
				if(helix[i][1][0] > helix[i][0][0])
					gc.fillRect(helix[i][0][0]+width, helix[i][0][1]+height/2, helix[i][1][0]-helix[i][0][0]-width, helix[i][1][1]-helix[i][0][1]+height/4);
				else
					gc.fillRect(helix[i][1][0]+width, helix[i][0][1]+height/2, helix[i][0][0]-helix[i][1][0]-width, helix[i][1][1]-helix[i][0][1]+height/4);
			}
		}
	}
}
