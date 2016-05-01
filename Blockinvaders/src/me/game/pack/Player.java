package me.game.pack;

import javafx.scene.canvas.GraphicsContext;
import me.game.weapons.PlayerWeapon;

public class Player extends Character {

	private double movementSpeed = 6.0;
	private int score;
	
	public Player(double xPos, int leben, PlayerWeapon waffe){
		super(xPos, 800, leben, waffe);
		heigth = 60;
		width = 40;
	}
	
	public void setSpeed(double speed){
		this.movementSpeed = speed;
	}
		
	public void moveLeft() {
		if(xPos - movementSpeed <= 0)
			xPos = 0;
		else
			xPos -= movementSpeed;
	}
	
	public void moveRight() {
		if(xPos + movementSpeed + width >= Frame.GAME_WIDTH )
			xPos = Frame.GAME_WIDTH-width;
		else
			xPos += movementSpeed;
	}
	
	
	public boolean setLife(int leben){
		if(alive && leben > 0) {
			this.life = leben;
			return true;
		}
		else{
			alive = false;
			return false;
		}
	}
	
	public void addLeben(){
		if(alive)
			this.life++;	
	}
	
	public void removeLeben(){
		life--;
		if(life <= 0)
			alive = false;
	}
		
	
	public void incScore(int score){
		if(score > 0)
			this.score += score;
	}
	
	public void decScore(int score){
		if(score > 0)
			if(this.score - score <= 0)
				score = 0;
			else
				this.score -= score;
	}
	
	public int getScore(){
		return this.score;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// TODO
	}
}
