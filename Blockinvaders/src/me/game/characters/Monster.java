package me.game.characters;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import me.game.monsterWeapons.MonsterWeapon;
import me.game.pack.Drop;
import me.game.pack.Drop.Drops;
import me.game.pack.Frame;
import me.game.playerWeapons.Weapon;

public class Monster extends Characters {

	private int worth;

	private int InitialHp;
	private Color color;
	private double shootRate;
	private int dropRate;

	public Monster(Weapon waffe, int Leben, double x, double y, double width, double length, int worth, Color color, double shootRate, int dropRate){
		super(x, y, Leben, waffe);
		this.width = width;
		this.height = length;
		this.worth = worth;
		alive = true;
		InitialHp = Leben;
		this.color = color;
		this.shootRate = shootRate;
		this.dropRate = dropRate;
	}

	// Format: ;x|y|width|length|life|initialHp|color:red:green:blue:alpha|weapon|shootRate|dropRate|worth;
	public Monster(String createString){
		super(0, 0, 0, null);
		if(!createString.startsWith(";"))
			return;
		if(!createString.endsWith(";"))
			return;
		String parts[];
		if((parts = createString.split("|")).length != 11)
			return;
		try{
			double x = Double.valueOf(parts[0]);
			double y = Double.valueOf(parts[1]);
			double width = Double.valueOf(parts[2]);
			double height = Double.valueOf(parts[3]);
			int life = Integer.valueOf(parts[4]);
			int initialHp = Integer.valueOf(parts[5]);
			String colorSplit[] = parts[6].split(":");
			Color color = new Color(Double.valueOf(colorSplit[1]), Double.valueOf(colorSplit[2]), Double.valueOf(colorSplit[3]), Double.valueOf(colorSplit[4]));
			MonsterWeapon weapon = null;
			double shootRate = Double.valueOf(parts[8]);
			int dropRate = Integer.valueOf(parts[9]);
			int worth = Integer.valueOf(parts[10]);

			setX(x);
			setY(y);
			setLife(life);
			giveWeapon(weapon);
			this.width = width;
			this.height = height;
			this.worth = worth;
			alive = true;
			InitialHp = initialHp;
			this.color = color;
			this.shootRate = shootRate;
			this.dropRate = dropRate;
		}catch(Exception e){
			return;
		}
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
		if (game.Online_Coop)
			game.client.processMessage("hitMonster " + hashCode() + " " + life);
		this.life = Leben;
		if(this.life <= 0){
			alive = false;
			triggerDeath();
			return false;
		}else{
			alive = true;
			return true;
		}
	}

	public boolean setLife(int Life, boolean online){
		this.life = Life;
		if(this.life <= 0){
			alive = false;
			triggerDeath();
			return false;
		}else{
			alive = true;
			return true;
		}
	}

	public boolean subLeben(int amount){
		if (game.Online_Coop)
			game.client.processMessage("hitMonster " + hashCode() + " " + (life-amount));
		life -= amount;
		if(life <= 0){
			alive = false;
			triggerDeath();
			return false;
		}else{
			alive = true;
			return true;
		}
	}


	public void triggerDeath(){
		Random r = new Random();
		Drops[] theDrops = Drops.values();
		if(r.nextInt(dropRate) == 1){
			Drop.AllDrops.add(new Drop(xPos, yPos, theDrops[r.nextInt(theDrops.length)]));
		}else if(color.equals(Color.DARKMAGENTA)){
			Drop.AllDrops.add(new Drop(xPos, yPos, theDrops[r.nextInt(theDrops.length)]));
		}

		hisWeapon.requestRemoval();
		game.Monsters.remove(this);
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void moveLeft(){
		if(xPos >= 0)
			xPos--;
	}

	public void moveRight(){
		if(xPos <= Frame.GAME_WIDTH-width)
			xPos++;
	}

	public void moveUp(){
		if(yPos >= 0)
			yPos--;
	}

	public void moveDown(){
		if(yPos <= Frame.GAME_LENGTH-300)
			yPos++;
	}

	public int getWorth(){
		return worth;
	}

	public void refresh(){}

	public void doMovement(){
		if (game.movetick == 500 && game.Monster_Direction == 0) {
			game.Monster_Direction = 1;
			game.movetick = 0;
		} else if (game.movetick == 500 && game.Monster_Direction == 1) {
			game.Monster_Direction = 0;
			game.movetick = 0;
		}

		if (game.Monster_Direction == 1)
			moveRight();
		else if (game.Monster_Direction == 0) 
			moveLeft();
	}

	@Override
	public void draw(GraphicsContext gc) {
		if (alive) {
			gc.setFill(color);
			gc.fillRect(xPos, yPos, width, height);
			if (life != InitialHp) {
				double fragleben = (double)life / (double) InitialHp;
				Color HpColor = new Color(1.0f * (1 - fragleben), 1.0f * fragleben, 0, 1);
				gc.setFill(HpColor);
				gc.fillRect(xPos + width / 10, yPos + height / 2.5,
						width * fragleben * 0.8, height - height * 0.8);
			}
		}
	}

	@Override
	public int hashCode(){
		int hc = 11;
		int hashMultiplier = 47;
		hc = hc * hashMultiplier + life;
		hc = hc * hashMultiplier + InitialHp;
		hc = hc * hashMultiplier + worth;
		double colorMesh = color.getBlue()*color.getRed()*color.getGreen();
		if(colorMesh != 0)
			// convert double to long and shift bytes then convert to Int (Rule for hashing)
			hc = hc * hashMultiplier + (int)(Double.doubleToLongBits(colorMesh)>>>32);
		double position = xPos*yPos;
		if(position != 0)
			hc = hc * hashMultiplier + (int)(Double.doubleToLongBits(position)>>>32);
		double size = width*height;
		if(size != 0)
			hc = hc * hashMultiplier + (int)(Double.doubleToLongBits(size)>>>32);
		hc = hc * hashMultiplier + dropRate;
		hc = hc * hashMultiplier + (int)(Double.doubleToLongBits(shootRate)>>>32);
		if(hisWeapon != null)
			hc = hc * hashMultiplier + hisWeapon.hashCode();
		if(hisSpecialWeapon != null)
			hc = hc * hashMultiplier + hisSpecialWeapon.hashCode();

		return hc;
	}

	@Override
	public boolean equals(Object other){
		if(other == null)
			return false;
		if(this.getClass() != other.getClass())
			return false;
		if(!super.equals(other))
			return false;

		Monster monti = (Monster)other;
		if(hashCode() != monti.hashCode())
			return false;
		if(life != monti.life)
			return false;
		if(hisWeapon != monti.hisWeapon)
			return false;
		if(hisSpecialWeapon != monti.hisSpecialWeapon)
			return false;
		if(InitialHp != monti.InitialHp)
			return false;
		if(worth != monti.worth)
			return false;
		if(shootRate != monti.shootRate)
			return false;
		if(dropRate != monti.dropRate)
			return false;
		if(color != monti.getColor())
			return false;
		if(width != monti.width)
			return false;
		if(height != monti.height)
			return false;

		return true;
	}
}
