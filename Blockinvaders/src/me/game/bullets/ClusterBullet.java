package me.game.bullets;

import me.game.weapons.PlayerWeapon;
import me.game.weapons.Weapon;

public class ClusterBullet extends TimedBullet{

	public ClusterBullet(Weapon waffe, double xPos, double yPos, double height, double width, int speed, int damage,
			double angle, int lifetimeticks) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle, lifetimeticks);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Trigger() {
		// TODO Auto-generated method stub
		for(int angle = -180; angle< 180; angle+=10)
			waffe.getKugeln().add(new PlayerBullet((PlayerWeapon) this.waffe, this.xPos, yPos, 7.0, 7.0, waffe.getBulletSpeed(), damage, angle ));
		
			
		waffe.getKugeln().remove(this);
	}

}
