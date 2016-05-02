package me.game.bullets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.game.pack.Frame;
import me.game.pack.Monster;
import me.game.pack.Player;
import me.game.weapons.PlayerWeapon;

public class PlayerBullet extends Bullet {

	public PlayerBullet(PlayerWeapon waffe, double xPos, double yPos, double height, double width, int speed,
			int damage, double angle) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle);
	}
	
	public void refresh(){
		if(yPos <= 0 || yPos >= Frame.GAME_LENGTH || xPos <= 0 || xPos >= Frame.GAME_WIDTH)
			waffe.getKugeln().remove(this);
		
		for (int i = 0; i < waffe.getOwner().game.Monsters.size(); i++) {
			Monster Monti = waffe.getOwner().game.Monsters.elementAt(i);
			if (checkHit(Monti.getX(), Monti.getY(), Monti.getWidth(), Monti.getHeight())) {
				if (Monti.isAlive()) {
					waffe.getKugeln().remove(this);
					if (!Monti.subLeben(damage)){
						Player p = (Player)waffe.getOwner();
						 p.incScore(Monti.getWorth());
					}
				}
			}
		}
		
		xPos += dx;
		yPos -= dy;
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.AQUA);
		gc.fillRect(xPos, yPos, width, height);
	}

}

