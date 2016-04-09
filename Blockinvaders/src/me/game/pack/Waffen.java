package me.game.pack;

import java.util.Vector;

public abstract class Waffen {
	protected final int INIT_DELAY;
	protected int damage;
	protected int ammo = 20;
	protected int bulletSpeed;
	protected int delay;
	protected Spieler Besitzer;
	
	Vector<Kugel> kugeln = new Vector<Kugel>(10, 5);
	public static Vector<Waffen> ActiveWeapons = new Vector<Waffen>();


	public Waffen(Spieler Besitzer, int damage,int delay,int bulletSpeed){
		this.damage = damage;
		this.INIT_DELAY = delay;
		this.bulletSpeed = bulletSpeed;
		this.Besitzer = Besitzer;
		ActiveWeapons.add(this);
	}


	public void shoot(double playerX, double playerY, double bulletW, double bulletH, double angle) {
		if(delay <= 0){
			delay = INIT_DELAY;
			kugeln.add(new Kugel(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, angle));
		}
	}

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
