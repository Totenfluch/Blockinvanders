package me.game.pack;

import me.game.bullets.Bullet;
import me.game.weapons.MonsterWeapon;

public class BotKI {
	private Player bot;
	public BotKI(Player p){
		this.bot = p;
	}

	public void Refresh(){
		checkForBullets();
		//bot.getHisWeapon().shoot(bot.getX(), bot.getY());
	}

	public void checkForBullets(){
		for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++)
			for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
				Bullet bul = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
				if(bot.getY() - bul.getyPos() < 100){
					if(bot.getX() - bul.getxPos() < 0 && bot.getX() - bul.getxPos() < -bot.getWidth()){
						if(bot.getX() < Frame.GAME_WIDTH/2)
							bot.moveRight();
						else
							bot.moveLeft();
					}
				}
			}
	}
}
