package me.game.playerWeapons;

import java.util.Vector;

import me.game.bullets.PlayerBullet;
import me.game.characters.Player;

public abstract class PlayerWeapon extends Weapon {
	
	public enum WeaponType {STANDARD, DUALS, SHOTGUN, QUAD, FIVE, SIX, MAX };
	
	
	public static Vector<PlayerWeapon> ActiveWeapons = new Vector<PlayerWeapon>();


	public PlayerWeapon(Player owner, int damage,int delay,int bulletSpeed){
		super(owner, damage, delay, bulletSpeed);
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
			return new DualsWeapon((Player)this.Owner, this.ammo);
			
		case DUALS:
			return new Shotgun((Player)this.Owner, this.ammo);
			
		case SHOTGUN:
			return new QuadWeapon((Player)this.Owner, this.ammo);
			
		case QUAD:
			return new FiveWeapon((Player)this.Owner, this.ammo);
			
		case FIVE:
			return new SixWeapon((Player)this.Owner, this.ammo);
			
		case SIX:
			return new MaxWeapon((Player)this.Owner, this.ammo);
			
		default:
			return this;
		}
	}
	
	public PlayerWeapon prevWaffe(){
		switch (typ) {
		case STANDARD:
			return this;
			
		case DUALS:
			return new StandardWeapon((Player)this.Owner, this.ammo);
			
		case SHOTGUN:
			return new DualsWeapon((Player)this.Owner, this.ammo);
			
		case QUAD:
			return new Shotgun((Player)this.Owner, this.ammo);
			
		case FIVE:
			return new QuadWeapon((Player)this.Owner, this.ammo);
			
		case SIX:
			return new FiveWeapon((Player)this.Owner, this.ammo);
			
		case MAX:
			return new SixWeapon((Player)this.Owner, this.ammo);
			
		default:
			return this;
		}
	}
	
	@Override
	public void terminate(){
		ActiveWeapons.remove(this);
	}

}
