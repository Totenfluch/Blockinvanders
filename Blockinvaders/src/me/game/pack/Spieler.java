package me.game.pack;

public class Spieler {
	private int leben;
	private boolean dead;
	
	protected Waffen waffe;
	
	private double xPos;
	
	private final double YPos = 800;
	
	private double width, heigth;
	private double movementSpeed = 1.0;
	
	private int score;
	
	public Spieler(){
		xPos = Frame.GAME_WIDTH/2;
		leben = 3;			
	}
	
	public Spieler(double xPos, int leben, Waffen waffe){
		this.xPos = xPos;
		this.leben = leben;
		this.waffe = waffe;
	}
	
	public void moveLeft() {
		if(xPos - movementSpeed <= 0)
			xPos = 0;
		else
			xPos -= movementSpeed;
	}
	
	public void moveRight() {
		if(xPos + movementSpeed >= Frame.GAME_WIDTH )
			xPos = Frame.GAME_WIDTH;
		else
			xPos += movementSpeed;
	}
	
	
	public void setLeben(int leben){
		if(!dead && leben > 0)
			this.leben = leben;
		else if(leben == 0)
			dead = true;
	}
	
	public void addLeben(){
		if(!dead)
			this.leben++;	
	}
	
	public void removeLeben(){
		leben--;
		if(leben <= 0)
			dead = true;
	}
	
	
	public boolean getHit(double x, double y) {
		if(x > xPos && x < xPos + width 
				&& y > YPos && y < YPos + heigth) {
			removeLeben();
			return true;
		}
		return false;
	}
	
	public void giveWeapon(Waffen waffe){
		this.waffe = waffe;
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
	
	
	

}
