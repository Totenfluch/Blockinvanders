package me.game.bullets;

import javafx.scene.paint.Color;
import me.game.pack.Frame;
import me.game.pack.Monster;
import me.game.pack.Player;
import me.game.weapons.MonsterWeapon;
import me.game.weapons.PlayerWeapon;

public class MonsterBullet extends Bullet {

	public MonsterBullet(MonsterWeapon waffe, double xPos, double yPos, double height, double width, int speed, int damage,
			double angle) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle);
	}

	public void refresh(){
		if(yPos <= 0 || yPos >= Frame.GAME_LENGTH || xPos <= 0 || xPos >= Frame.GAME_WIDTH)
			waffe.getKugeln().remove(this);
		
		for (int x = 0; x < Frame.Players.length; x++) {
			Player p = Frame.Players[x];
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
		
		xPos += dx;
		yPos += dy;
	}
}
