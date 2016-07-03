package me.game.characters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import me.game.pack.Frame;
import me.game.playerWeapons.PlayerWeapon;

public class Player extends Characters{

	private double movementSpeed = 6.0;
	private int score;
	public static int activePlayers = 0;
	private int playerNumber;
	private static Frame instance;
	private String PlayerName;
	private int hitImmume = 0;
	private int downgradeImmume = 0;

	public Player(double xPos, int leben, PlayerWeapon waffe) {
		super(xPos, 800, leben, waffe);
		if (instance == null)
			instance = Frame.getInstance();
		else if (instance != Frame.getInstance()) {
			instance = Frame.getInstance();
			activePlayers = 0;
		}
		height = 60;
		width = 40;
		playerNumber = activePlayers++;
		PlayerName = "Player-"+playerNumber;
	}
	
	public String getPlayerName(){
		return PlayerName;
	}
	
	public void setPlayerName(String PlayerName){
		this.PlayerName = PlayerName;
	}

	public void setSpeed(double speed) {
		this.movementSpeed = speed;
	}

	@Override
	public void moveLeft() {
		if (xPos - movementSpeed <= 0)
			xPos = 0;
		else
			xPos -= movementSpeed;
	}

	@Override
	public void moveRight() {
		if (xPos + movementSpeed + width >= Frame.GAME_WIDTH)
			xPos = Frame.GAME_WIDTH - width;
		else
			xPos += movementSpeed;
	}

	public void incScore(int amount) {
		setScore(this.score+amount);
	}

	public void decScore(int amount) {
		setScore(this.score-amount);
	}
	
	public void setScore(int score){
		if(this.score + score > 0)
			this.score = score;
		else
			this.score = 0;
		
		if(game.Online_Coop)
			game.client.processMessage("syncScore " + this.score);
	}

	public int getScore() {
		return this.score;
	}
	
	public int getHitImmumeTimeLeft(){
		return hitImmume;
	}
	
	public int getDowngradeImmumeTimeLeft() {
		return downgradeImmume;
	}
	
	public boolean isHitImmume(){
		if(hitImmume > 0)
			return true;
		return false;
	}
	
	public boolean isDowngradeImmume(){
		if(downgradeImmume > 0)
			return true;
		return false;
	}
	
	public void setHitImmume(int hitImmume) {
		this.hitImmume = hitImmume;
	}
	
	public void setDowngradeImmume(int downgradeImmume) {
		this.downgradeImmume = downgradeImmume;
	}
	
	public void refresh(){
		if(hitImmume > 0)
			hitImmume--;
		if(downgradeImmume > 0)
			downgradeImmume--;
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		if(alive && hitImmume > 0){
			gc.setFill(Color.AQUAMARINE);
			gc.fillRect(xPos-2, yPos-2, width+4, height+4);
		}else if(alive && downgradeImmume > 0){
			gc.setFill(Color.ORANGE);
			gc.fillRect(xPos-2, yPos-2, width+4, height+4);
		}
		
		if (playerNumber == 0)
			gc.setFill(Color.PURPLE);
		else if (playerNumber == 1)
			gc.setFill(Color.LIME);
		else if (playerNumber == 2)
			gc.setFill(Color.BLUE);
		else if (playerNumber == 3)
			gc.setFill(Color.CRIMSON);
		else
			gc.setFill(Color.WHITE);

		if (alive)
			gc.fillRect(xPos, yPos, width, height);

		if (playerNumber == 0) {
			if (alive) {
				gc.setFont(new Font("Impact", 20));
				gc.setFill(Color.LIGHTGRAY);
				StringBuffer swb = new StringBuffer();
				swb.append(hisWeapon.getWeaponType().toString() + ":        ");
				swb.append(hisSpecialWeapon.getAmmo());
				swb.append("x    <|>                ");
				swb.append(hisWeapon.getAmmo() - hisWeapon.getAmmo() % 50);
				swb.append("    ");
				for (int i = 0; i < hisWeapon.getAmmo() % 50; i++)
					swb.append("|");

				gc.fillText(swb.toString(), 50, 100);
			}

			gc.setFont(new Font("Impact", 30));
			gc.setFill(Color.BLUEVIOLET);
			gc.fillText(Frame.getInstance().Player1Name + "> " + score, 50, 50);
			gc.setFill(Color.RED);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < life; i++)
				sb.append("+ ");
			if (alive)
				gc.fillText(sb.toString(), 45, 75);
			else
				gc.fillText("DEAD", 45, 80);

		} else if (playerNumber == 1) {
			gc.setFont(new Font("Impact", 20));
			if (alive) {
				gc.setFill(Color.LIGHTGRAY);
				StringBuffer swb2 = new StringBuffer();
				swb2.append(hisWeapon.getWeaponType().toString() + ":        ");
				swb2.append(hisSpecialWeapon.getAmmo());
				swb2.append("x    <|>                ");
				swb2.append(hisWeapon.getAmmo() - hisWeapon.getAmmo() % 50);
				swb2.append("    ");
				for (int i = 0; i < hisWeapon.getAmmo() % 50; i++)
					swb2.append("|");

				gc.fillText(swb2.toString(), 50, 875);
			}
			gc.setFont(new Font("Impact", 30));
			gc.setFill(Color.LIME);
			gc.fillText(Frame.getInstance().Player2Name + "> " + score, 50, 825);
			gc.setFill(Color.RED);
			StringBuffer sb2 = new StringBuffer();
			for (int i = 0; i < life; i++)
				sb2.append("+ ");
			if (alive)
				gc.fillText(sb2.toString(), 45, 850);
			else
				gc.fillText("DEAD", 45, 855);
		} else if (playerNumber == 2) {
			gc.setFont(new Font("Impact", 20));
			if (alive) {
				gc.setFill(Color.LIGHTGRAY);
				StringBuffer swb2 = new StringBuffer();
				for (int i = 0; i < hisWeapon.getAmmo() % 50; i++)
					swb2.append("|");
				swb2.append(" " + (hisWeapon.getAmmo() - hisWeapon.getAmmo() % 50));
				swb2.append("x    <|>                ");
				swb2.append(hisSpecialWeapon.getAmmo());
				swb2.append("        " + hisWeapon.getWeaponType().toString());
				swb2.append("    ");
				gc.fillText(swb2.toString(), Frame.GAME_WIDTH-swb2.length()*6, 100);
			}

			gc.setFont(new Font("Impact", 30));
			gc.setFill(Color.BLUEVIOLET);
			String toDraw = score  + "< " + Frame.getInstance().Player1Name;
			gc.fillText(toDraw, Frame.GAME_WIDTH-toDraw.length()*13.5, 50);
			gc.setFill(Color.RED);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < life; i++)
				sb.append("+ ");
			if (alive){
				gc.fillText(sb.toString(), Frame.GAME_WIDTH-sb.toString().length()*12, 75);
			}else{
				gc.fillText("DEAD", Frame.GAME_WIDTH-245, 80);
			}

		} else if (playerNumber == 3) {
			gc.setFont(new Font("Impact", 20));
			if (alive) {
				gc.setFill(Color.LIGHTGRAY);
				StringBuffer swb2 = new StringBuffer();
				for (int i = 0; i < hisWeapon.getAmmo() % 50; i++)
					swb2.append("|");
				swb2.append(" " + (hisWeapon.getAmmo() - hisWeapon.getAmmo() % 50));
				swb2.append("x    <|>                ");
				swb2.append(hisSpecialWeapon.getAmmo());
				swb2.append("        " + hisWeapon.getWeaponType().toString());
				swb2.append("    ");
				gc.fillText(swb2.toString(), Frame.GAME_WIDTH-swb2.length()*6, 875);
			}
			gc.setFont(new Font("Impact", 30));
			gc.setFill(Color.CRIMSON);
			String toDraw = score  + "< " + Frame.getInstance().Player2Name;
			gc.fillText(toDraw, Frame.GAME_WIDTH-toDraw.length()*13.5, 825);
			gc.setFill(Color.RED);
			StringBuffer sb2 = new StringBuffer();
			for (int i = 0; i < life; i++)
				sb2.append("+ ");
			if (alive)
				gc.fillText(sb2.toString(), Frame.GAME_WIDTH-sb2.toString().length()*12, 850);
			else
				gc.fillText("DEAD", Frame.GAME_WIDTH-245, 855);
		}

	}

	@Override
	public void triggerDeath() {
		if(game.Online_Coop)
			game.client.processMessage("playerDeath");
	}
}
