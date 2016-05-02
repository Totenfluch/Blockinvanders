package me.game.pack;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import me.game.weapons.PlayerWeapon;

public class Player extends Character {

	private double movementSpeed = 6.0;
	private int score;
	public static int activePlayers = 0;
	private int playerNumber;
	private static Frame instance;
	
	public Player(double xPos, int leben, PlayerWeapon waffe){
		super(xPos, 800, leben, waffe);
		if(instance == null)
			instance = Frame.getInstance();
		else if(instance != Frame.getInstance()){
			instance = Frame.getInstance();
			activePlayers = 0;
		}
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
		if(alive){
			gc.fillRect(xPos, yPos, width, heigth);
			if(playerNumber == 0){
				gc.setFont(new Font("Impact", 20));
				gc.setFill(Color.LIGHTGRAY);
				StringBuffer swb = new StringBuffer();
				swb.append(hisWeapon.getWeaponType().toString() + ": ");
				for (int i = 0; i < hisWeapon.getAmmo(); i++)
					swb.append("|");
				swb.append("   ");
				for (int x = 0; x < hisSpecialWeapon.getAmmo(); x++)
					swb.append("<|> ");
				gc.fillText(swb.toString(), 50, 100);
				
				gc.setFont(new Font("Impact", 30));
				gc.setFill(Color.BLUEVIOLET);
				gc.fillText(Frame.getInstance().Player1Name + "> " + score, 50, 50);
				gc.setFill(Color.RED);
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < life; i++)
					sb.append("+ ");
				if(alive)
					gc.fillText(sb.toString(), 45, 75);
				else
					gc.fillText("DEAD", 45, 80);
			}else if(playerNumber == 1){
				gc.setFont(new Font("Impact", 20));
				if (alive) {
					gc.setFill(Color.LIGHTGRAY);
					StringBuffer swb2 = new StringBuffer();
					swb2.append(hisWeapon.getWeaponType().toString() + ": ");
					for (int i = 0; i < hisWeapon.getAmmo(); i++)
						swb2.append("|");
					swb2.append("   ");
					for (int x = 0; x < hisSpecialWeapon.getAmmo(); x++)
						swb2.append("<|> ");
					gc.fillText(swb2.toString(), 50, 875);
				}
				gc.setFont(new Font("Impact", 30));
				gc.setFill(Color.LIME);
				gc.fillText(Frame.getInstance().Player1Name + "> " + score, 50, 825);
				gc.setFill(Color.RED);
				StringBuffer sb2 = new StringBuffer();
				for (int i = 0; i < life; i++)
					sb2.append("+ ");
				if (alive)
					gc.fillText(sb2.toString(), 45, 850);
				else
					gc.fillText("DEAD", 45, 855);
			}
		
		
		}
	}
}
