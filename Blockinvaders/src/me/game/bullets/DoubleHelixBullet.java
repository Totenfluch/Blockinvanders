package me.game.bullets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.characters.Player;
import me.game.monsterWeapons.MonsterWeapon;
import me.game.pack.Frame;

public class DoubleHelixBullet extends MonsterBullet{

	DoubleHelixBullet partnerNullet;
	boolean type;
	double wave = 0;
	int refreshcounter = 0;
	int helixcounter = 0;
	int removeInTicks = 40;
	double[][][] helix = new double[3000][2][2];


	public DoubleHelixBullet(MonsterWeapon waffe, double xPos, double yPos, double height, double width, int speed, int damage,
			double angle, boolean type) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle);
		this.type = type;
	}

	public DoubleHelixBullet getPartner(){
		return partnerNullet;
	}

	public void setPartner(DoubleHelixBullet Partner){
		this.partnerNullet = Partner;
	}

	@Override
	public void refresh(){
		if(refreshcounter >= 5){
			refreshcounter = 0;
			if(partnerNullet != null){
				helix[helixcounter][0][0] = xPos;
				helix[helixcounter][0][1] = yPos;
				helix[helixcounter][1][0] = partnerNullet.xPos;
				helix[helixcounter][1][1] = partnerNullet.yPos;
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
				onHit((Monster)waffe.getOwner(), p);
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
		if(partnerNullet != null)
			if(partnerNullet.getxPos() > xPos)
				gc.fillRect(xPos+width, yPos+height/2, partnerNullet.getxPos()-xPos-width, partnerNullet.getyPos()-yPos+height/4);
			else
				gc.fillRect(partnerNullet.xPos+width, yPos+height/2, xPos-partnerNullet.getxPos()-width, partnerNullet.getyPos()-yPos+height/4);
		if(partnerNullet != null){
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
		}
	}

	@Override
	public boolean checkHit(double x, double y, double width, double height){
		boolean isHit =  xPos < x + width && xPos + this.width > x && yPos < y + height && yPos + this.height > y;
		boolean isHit2 = false;
		if(helixcounter != 0)
			isHit2 = helix[0][0][0]-this.width < x + width && helix[0][0][0]-this.width + 60+this.width*2 > x && helix[0][0][1] < y + height && helix[0][0][1] + helix[helixcounter-1][0][1]-helix[0][0][1] > y;
		return isHit || isHit2;
	}
}
