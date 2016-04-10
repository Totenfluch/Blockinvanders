package me.game.pack;

import javafx.scene.paint.Color;

public class MonsterBullet extends Bullet {

	public MonsterBullet(MonsterWeapon waffe, double xPos, double yPos, double height, double width, int speed, int damage,
			double angle) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle);
	}

	public void refresh(){
		if(yPos <= 0 || yPos >= Frame.GAME_LENGTH || xPos <= 0 || xPos >= Frame.GAME_WIDTH)
			waffe.kugeln.remove(this);
		
		for (int x = 0; x < Frame.Players.size(); x++) {
			Player p = Frame.Players.elementAt(x);
			if (checkHit(p.getX(), p.getY(), p.getWidth(), p.getHeight())) {
				if (p.isAlive()) {
					waffe.kugeln.remove(this);
					p.removeLeben();
					Monster m = (Monster) waffe.Besitzer;
					m.setInitHp(m.getInitHp() + 300);
					m.setLeben(m.getInitHp());
					m.setColor(Color.DARKMAGENTA);
					p.giveWeapon(((PlayerWeapon) p.waffe).prevWaffe());
				}
			}
		}
		
		xPos += dx;
		yPos += dy;
	}
}
