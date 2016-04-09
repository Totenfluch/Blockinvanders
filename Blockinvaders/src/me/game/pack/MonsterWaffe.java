package me.game.pack;

import java.util.Vector;

public abstract class MonsterWaffe {
	
	protected final int INIT_DELAY;
	protected int damage;
	protected int bulletSpeed;
	protected int delay;
	protected Monster Besitzer;
	
	Vector<MonsterKugel> kugeln = new Vector<MonsterKugel>(10, 5);
	public static Vector<MonsterWaffe> ActiveWeapons = new Vector<MonsterWaffe>();


	public MonsterWaffe(Monster Besitzer, int damage,int delay,int bulletSpeed){
		this.damage = damage;
		this.INIT_DELAY = delay;
		this.bulletSpeed = bulletSpeed;
		this.Besitzer = Besitzer;
		ActiveWeapons.add(this);
	}


	public void shoot(double playerX, double playerY, double bulletW, double bulletH, double angle) {
		if(delay <= 0){
			delay = INIT_DELAY;
			kugeln.add(new MonsterKugel(this, playerX, playerY, bulletH, bulletW, bulletSpeed, damage, angle));
		}
	}

	public abstract void shoot(double monsterX, double monsterY);


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

	
	public void refresh() {
		for(int i = 0; i<kugeln.size(); i++)
			kugeln.elementAt(i).refresh();
		
		if(delay > 0)
			delay--;			
	}
}
