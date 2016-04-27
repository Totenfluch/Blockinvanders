package me.game.pack;

import java.util.Random;

import javafx.scene.paint.Color;
import me.game.pack.Drop.Drops;
import me.game.weapons.Weapon;

public class Monster extends Character{

	private int worth;

	private int InitialHp;
	private Color color;
	private double shootRate;
	private int dropRate;

	public Monster(Weapon waffe, int Leben, double x, double y, double width, double length, int worth, Color color, double shootRate, int dropRate){
		super(x, y, Leben, waffe);
		this.width = width;
		this.heigth = length;
		this.worth = worth;
		alive = true;
		InitialHp = Leben;
		this.color = color;
		this.shootRate = shootRate;
		this.dropRate = dropRate;
	}
	
	public double getShootRate(){
		return shootRate;
	}
	
	public int getDropRate(){
		return dropRate;
	}
	
	public void setColor(Color c){
		this.color = c;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setWorth(int amount){
		worth = amount;
	}

	public int getInitHp(){
		return InitialHp;
	}

	public void setInitHp(int hp){
		InitialHp = hp;
	}


	public boolean setLife(int Leben){
		this.life = Leben;
		if(this.life <= 0){
			alive = false;
			TriggerDeath();
			return false;
		}else{
			alive = true;
			return true;
		}
	}

	public boolean subLeben(int amount){
		life -= amount;
		if(life <= 0){
			alive = false;
			TriggerDeath();
			return false;
		}else{
			alive = true;
			return true;
		}
	}


	public void TriggerDeath(){
		Random r = new Random();
		Drops[] theDrops = Drops.values();
		if(r.nextInt(dropRate) == 1){
			Drop.AllDrops.add(new Drop(xPos, yPos, theDrops[r.nextInt(theDrops.length)]));
		}else if(color.equals(Color.DARKMAGENTA)){
			Drop.AllDrops.add(new Drop(xPos, yPos, theDrops[r.nextInt(theDrops.length)]));
		}
		Frame.Monsters.remove(this);
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void moveLeft(){
		xPos--;
	}

	public void moveRight(){
		xPos++;
	}

	public void moveUp(){
		yPos--;
	}

	public void moveDown(){
		yPos++;
	}

	public int getWorth(){
		return worth;
	}

}
