package me.game.bullets;

import me.game.weapons.Weapon;

public abstract class TimedBullet extends Bullet{
	private int lifetimeticks;
	public TimedBullet(Weapon waffe, double xPos, double yPos, double height, double width, int speed, int damage, double angle, int lifetimeticks) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle);
		this.lifetimeticks = lifetimeticks;
	}

	@Override
	public void refresh() {
		lifetimeticks--;
		if(lifetimeticks <= 0)
			Trigger();	
	}
	
	public abstract void Trigger();

}
