package me.game.playerWeapons;

import java.util.Vector;

import me.game.bullets.Bullet;
import me.game.pack.Character;
import me.game.playerWeapons.PlayerWeapon.WeaponType;

public abstract class Weapon {
	protected final int INIT_DELAY;
	protected int damage;
	protected int ammo = 20;
	protected int bulletSpeed;
	protected int delay;
	protected Character Owner;
	protected WeaponType typ;
	
	protected Vector<Bullet> kugeln = new Vector<Bullet>(10000, 500);


	public Weapon(Character Besitzer, int damage,int delay, int bulletSpeed){
		this.damage = damage;
		this.INIT_DELAY = delay;
		this.bulletSpeed = bulletSpeed;
		this.Owner = Besitzer;
	}
	
	public Character getOwner(){
		return Owner;
	}
	
	public Vector<Bullet> getKugeln(){
		return kugeln;
	}
	
	public WeaponType getWeaponType(){
		return typ;
	}


	public abstract void shoot(double playerX, double playerY, double bulletW, double bulletH, double angle);

	public abstract void shoot(double playerX, double playerY);


	public int getInitDelay() {
		return INIT_DELAY;
	}


	public int getDamage() {
		return damage;
	}

	public int getBulletSpeed() {
		return bulletSpeed;
	}



	public int getDelay() {
		return delay;
	}


	public void setDelay(int delay) {
		this.delay = delay;
	}


	public void addAmmo(int ammo) {
		if (ammo > 0)
			this.ammo += ammo;
	}

	public void removeAmmo(int ammo) {
		if (ammo > 0)
			if (this.ammo - ammo <= 0)
				this.ammo = 0;
			else
				this.ammo -= ammo;
	}

	public void setAmmo(int ammo){
		if(ammo >= 0)
			this.ammo = ammo;		
	}

	public int getAmmo() {
		return this.ammo;
	}

	public void refresh() {
		for(int i = 0; i<kugeln.size(); i++)
			kugeln.elementAt(i).refresh();
		
		if(delay > 0)
			delay--;			
	}
	

}
