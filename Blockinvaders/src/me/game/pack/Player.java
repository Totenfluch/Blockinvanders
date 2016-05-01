package me.game.pack;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.game.weapons.PlayerWeapon;

public class Player extends Character {

	private double movementSpeed = 6.0;
	private int score;
	public static int activePlayers = 0;
	private int playerNumber;
	
	public Player(double xPos, int leben, PlayerWeapon waffe){
		super(xPos, 800, leben, waffe);
		heigth = 60;
		width = 40;
		playerNumber = activePlayers++;
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
		if (playerNumber == 0)
			gc.setFill(Color.PURPLE);
		else if (playerNumber == 1)
			gc.setFill(Color.LIME);
		else if (playerNumber == 2)
			gc.setFill(Color.BLUE);
		else if (playerNumber == 3)
			gc.setFill(Color.CRIMSON);
		if(alive)
			gc.fillRect(xPos, yPos, width, heigth);
	}
}
