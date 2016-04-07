package me.game.pack;

import java.util.Vector;

public abstract class Waffen {
	private final int INIT_DELAY;
	private int damage;
	private int ammo;
	private int bulletSpeed;
	private int delay;
	public static Vector<Kugel> kugeln = new Vector<Kugel>(10, 5);


	public Waffen(int damage,int delay,int bulletSpeed){
		this.damage = damage;
		this.INIT_DELAY = delay;
		this.bulletSpeed = bulletSpeed;
	}


	public void shoot(double playerX, double playerY, double bulletW, double bulletH) {
		if(delay <= 0){
			delay = INIT_DELAY;
			kugeln.add(new Kugel(playerX, playerY, bulletH, bulletW, bulletSpeed));
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


	public void addAmmunition(int ammo) {
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
		if(delay > 0)
			delay--;

		for(int p=0; p<kugeln.size(); p++){
			Kugel r = kugeln.elementAt(p);
			if(r.yPos <= 0)
				kugeln.remove(r);
			r.refresh();
		}			
	}

}
