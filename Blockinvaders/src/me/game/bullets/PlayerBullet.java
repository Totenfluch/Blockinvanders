package me.game.bullets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.game.characters.Monster;
import me.game.characters.Player;
import me.game.pack.Frame;
import me.game.playerWeapons.PlayerWeapon;

public class PlayerBullet extends Bullet {

	public PlayerBullet(PlayerWeapon waffe, double xPos, double yPos, double height, double width, double speed,
			int damage, double angle) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle);
	}

	@Override
	public void refresh(){
		if(yPos <= 0 || yPos >= Frame.GAME_LENGTH || xPos <= 0 || xPos >= Frame.GAME_WIDTH)
			waffe.getKugeln().remove(this);

		for (int i = 0; i < waffe.getOwner().game.Monsters.size(); i++) {
			Monster Monti = waffe.getOwner().game.Monsters.elementAt(i);
			if (checkHit(Monti.getX(), Monti.getY(), Monti.getWidth(), Monti.getHeight())) {
				onHit(Monti, (Player)waffe.getOwner());
			}
		}

		xPos += dx;
		yPos -= dy;
	}

	public void onHit(Monster victim, Player attacker){
		if(victim.isAlive()){
			super.onHit();
			if (!victim.subLeben(damage)){
				attacker.incScore(victim.getWorth());
			}
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.AQUA);
		gc.fillRect(xPos, yPos, width, height);
	}

}

