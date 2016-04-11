package me.game.pack;

import java.util.Vector;

public abstract class PlayerWeapon extends Weapon {
	
	protected enum WeaponType {STANDARD, DUALS, SHOTGUN, QUAD, FIVE, SIX, MAX };
	
	
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
			return new DualsWeapon((Player)this.Besitzer, this.ammo);
			
		case DUALS:
			return new Shotgun((Player)this.Besitzer, this.ammo);
			
		case SHOTGUN:
			return new QuadWeapon((Player)this.Besitzer, this.ammo);
			
		case QUAD:
			return new FiveWeapon((Player)this.Besitzer, this.ammo);
			
		case FIVE:
			return new SixWeapon((Player)this.Besitzer, this.ammo);
			
		case SIX:
			return new MaxWeapon((Player)this.Besitzer, this.ammo);
			
		default:
			return this;
		}
	}
	
	public PlayerWeapon prevWaffe(){
		switch (typ) {
		case STANDARD:
			return this;
			
		case DUALS:
			return new StandardWaffe((Player)this.Besitzer, this.ammo);
			
		case SHOTGUN:
			return new DualsWeapon((Player)this.Besitzer, this.ammo);
			
		case QUAD:
			return new Shotgun((Player)this.Besitzer, this.ammo);
			
		case FIVE:
			return new QuadWeapon((Player)this.Besitzer, this.ammo);
			
		case SIX:
			return new FiveWeapon((Player)this.Besitzer, this.ammo);
			
		case MAX:
			return new SixWeapon((Player)this.Besitzer, this.ammo);
			
		default:
			return this;
		}
	}

}
