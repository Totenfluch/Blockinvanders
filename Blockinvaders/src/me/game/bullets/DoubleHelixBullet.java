package me.game.bullets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import me.game.characters.Monster;
import me.game.characters.Player;
import me.game.monsterWeapons.MonsterWeapon;
import me.game.pack.Frame;
import me.game.playerWeapons.PlayerWeapon;

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
			/*boolean hit = false;
			if(helixcounter != 0)
				if(overlaps(new Rectangle(helix[0][0][0]-width, helix[0][0][1], 60+width*2, helix[helixcounter-1][0][1]-helix[0][0][1]), p))
					hit = true;*/
			if (checkHit(p.getX(), p.getY(), p.getWidth(), p.getHeight()) /*|| hit*/) {
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
			gc.setFill(Color.BLUE);
			//if(helixcounter != 0)
			//	gc.fillRect(helix[0][0][0]-width, helix[0][0][1], 60+width*2, helix[helixcounter-1][0][1]-helix[0][0][1]);
		}
	}

	private boolean overlaps (Rectangle r, Player p) {
		return p.getX() < r.getX() + r.getWidth() && p.getX() + p.getWidth() > r.getX() && p.getY() < r.getY() + r.getHeight() && p.getY() + p.getHeight() > r.getY();
	}

	public boolean checkHit(double x, double y, double width, double height){
		boolean isHit =  xPos < x + width && xPos + this.width > x && yPos < y + height && yPos + this.height > y;
		boolean isHit2 = helix[0][0][0]-this.width < x + width && helix[0][0][0]-this.width + 60+this.width*2 > x && helix[0][0][1] < y + height && helix[0][0][1] + helix[helixcounter-1][0][1]-helix[0][0][1] > y;
		return isHit || isHit2;
	}
}
