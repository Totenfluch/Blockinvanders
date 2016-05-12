package me.game.characters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import me.game.pack.Frame;
import me.game.playerWeapons.PlayerWeapon;

public class Player extends Character implements Controllable{

	private double movementSpeed = 6.0;
	private int score;
	public static int activePlayers = 0;
	private int playerNumber;
	private Controller controller;
	private static Frame instance;
	private String PlayerName;

	public Player(double xPos, int leben, PlayerWeapon waffe) {
		super(xPos, 800, leben, waffe);
		if (instance == null)
			instance = Frame.getInstance();
		else if (instance != Frame.getInstance()) {
			instance = Frame.getInstance();
			activePlayers = 0;
		}
		heigth = 60;
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

	public void moveLeft() {
		if (xPos - movementSpeed <= 0)
			xPos = 0;
		else
			xPos -= movementSpeed;
	}

	public void moveRight() {
		if (xPos + movementSpeed + width >= Frame.GAME_WIDTH)
			xPos = Frame.GAME_WIDTH - width;
		else
			xPos += movementSpeed;
	}

	public boolean setLife(int leben) {
		if (alive && leben > 0) {
			this.life = leben;
			return true;
		} else {
			alive = false;
			return false;
		}
	}

	public void addLeben() {
		if (alive)
			this.life++;
	}

	public void removeLeben() {
		life--;
		if (life <= 0)
			alive = false;
	}

	public void incScore(int score) {
		if (score > 0)
			this.score += score;
	}

	public void decScore(int score) {
		if (score > 0)
			if (this.score - score <= 0)
				score = 0;
			else
				this.score -= score;
	}

	public int getScore() {
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
		if (alive)
			gc.fillRect(xPos, yPos, width, heigth);

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
	public Controller getController() {
		return controller;
	}

	@Override
	public void setController(Controller controller) {
		this.controller = controller;
		controller.onSetControl(this);
	}
	
	@Override
	public boolean isControlled(){
		if(controller != null)
			return true;
		return false;
	}
}
