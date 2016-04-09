package me.game.pack;

public class PlayerBullet extends Bullet {

	public PlayerBullet(PlayerWeapon waffe, double xPos, double yPos, double height, double width, int speed,
			int damage, double angle) {
		super(waffe, xPos, yPos, height, width, speed, damage, angle);
	}
	
	public void refresh(){
		if(yPos <= 0 || yPos >= Frame.GAME_LENGTH || xPos <= 0 || xPos >= Frame.GAME_WIDTH)
			waffe.kugeln.remove(this);
		
		for (int i = 0; i < Frame.Monsters.length; i++) {
			Monster Monti = Frame.Monsters[i];
			if (checkHit(Monti.getX(), Monti.getY(), Monti.getWidth(), Monti.getHeight())) {
				if (Monti.isAlive()) {
					waffe.kugeln.remove(this);
					if (!Monti.subLeben(damage)){
						Player p = (Player)waffe.Besitzer;
						 p.incScore(Monti.getWorth());
					}
				}
			}
		}
		
		xPos += dx;
		yPos -= dy;
	}

}

