package me.game.pack;

import java.util.Vector;

public abstract class PlayerWeapon extends Weapon {
	
	protected enum WaffenTyp {STANDARD, SHOTGUN };
	protected WaffenTyp typ;
	
	public static Vector<PlayerWeapon> ActiveWeapons = new Vector<PlayerWeapon>();


	public PlayerWeapon(Player Besitzer, int damage,int delay,int bulletSpeed){
		super(Besitzer, damage, delay, bulletSpeed);
		ActiveWeapons.add(this);
	}

	
	public void shoot(double playerX, double playerY, double bulletW, double bulletH, double angle) {
		if(delay <= 0){
			delay = INIT_DELAY;
			kugeln.add(new PlayerBullet(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, angle));
		}
	}
	
	public abstract void shoot(double playerX, double playerY);


	public int getWert(){
		return typ.ordinal();
	}
	
	public PlayerWeapon nextWaffe() {
		
		switch (typ) {
		case STANDARD:
			return new Shotgun((Player)this.Besitzer, this.ammo);
			
		default:
			return this;
		}
	}

}
