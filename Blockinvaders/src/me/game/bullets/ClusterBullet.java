package me.game.bullets;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import me.game.playerWeapons.PlayerWeapon;
import me.game.playerWeapons.Weapon;

public class ClusterBullet extends TimedBullet{
	private int count = 0;
	public ClusterBullet(Weapon waffe, double xPos, double yPos, double height, double width, int speed, int damage,
			double angle, int lifetimeticks) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle, lifetimeticks);
	}

	@Override
	public void Trigger() {
		for(int angle = -180; angle< 180; angle+=10)
			waffe.getKugeln().add(new PlayerBullet((PlayerWeapon) this.waffe, this.xPos, yPos, 7.0, 7.0, waffe.getBulletSpeed()+4, damage, angle ));
		
		Timeline tf = new Timeline(new KeyFrame(Duration.millis(150), ae->{
			for(int angle = -180-count*90; angle< 180+count*90; angle+=10+count*2.5)
				waffe.getKugeln().add(new PlayerBullet((PlayerWeapon) this.waffe, this.xPos, yPos, 7.0, 7.0, waffe.getBulletSpeed()+4, damage, angle ));
		}));
		tf.setCycleCount(3);
		tf.play();
			
		waffe.getKugeln().remove(this);
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.AQUA);
		gc.fillRect(xPos, yPos, width, height);
	}

}
