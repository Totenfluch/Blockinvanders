package me.game.bullets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.characters.Player;
import me.game.monsterWeapons.MonsterWeapon;
import me.game.pack.Frame;
import me.game.playerWeapons.PlayerWeapon;

public class MonsterBullet extends Bullet {

	public MonsterBullet(MonsterWeapon waffe, double xPos, double yPos, double height, double width, int speed, int damage,
			double angle) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle);
	}

	@Override
	public void refresh(){
		if(yPos <= 0 || yPos >= Frame.GAME_LENGTH || xPos <= 0 || xPos >= Frame.GAME_WIDTH)
			waffe.getKugeln().remove(this);

		for (int x = 0; x < waffe.getOwner().game.Players.length; x++) {
			Player p = waffe.getOwner().game.Players[x];
			if (checkHit(p.getX(), p.getY(), p.getWidth(), p.getHeight())) {
				onHit((Monster)waffe.getOwner(), p);
			}
		}

		xPos += dx;
		yPos += dy;
	}

	public void onHit(Monster attacker, Player victim){
		if(victim.isAlive()){
			super.onHit();
			attacker.setInitHp(attacker.getInitHp() + (Frame.getInstance().clearcount+1)*10);
			attacker.setLife(attacker.getInitHp());
			attacker.setColor(Color.DARKMAGENTA);
			if(!victim.isHitImmume()){
				victim.setLife(victim.getLife()-waffe.getDamage());
				victim.setHitImmume(625);
			}
			if(!victim.isDowngradeImmume()){
				victim.giveWeapon(((PlayerWeapon) victim.hisWeapon).prevWaffe());
				victim.setDowngradeImmume(1000);
			}
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.RED);
		gc.fillRect(xPos, yPos, width, height);
	}
}
