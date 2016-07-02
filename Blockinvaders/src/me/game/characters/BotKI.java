package me.game.characters;

public interface BotKI {
	public void refresh();
	
	
	/* How to write a Bot
	 * Create a variable to store a Player object - let's call it bot
	 * Create a variable to store a Frame object - let's call it game
	 * 
	 * You can move your bot with
	 *  - bot.moveLeft();
	 *  - bot.moveRight();
	 *  -> You are only allowed to use one of the above per refresh!!
	 */
	
	/* Detect Colissions
	 * ... Iterate through the Bullets
	 *  for (int i = 0; i < MonsterWeapon.ActiveWeapons.size(); i++){
	 *		for(int x = 0; x < MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().size(); x++){
	 *      Bullet bul = MonsterWeapon.ActiveWeapons.elementAt(i).getKugeln().elementAt(x);
	 *      // Do Stuff with the Bullet
	 *      for example: bul.checkHit(....) to use the build in function - You should do that!!
	 *      }
	 *  }
	 */
	
	/* Shoot bullets
	 * Normal ones:
	 * - player.getHisWeapon().shoot(player.getX(), player.getY());
	 * Special 'Rockets'
	 * - player.getHisSpecialWeapon().shoot(player.getX(), player.getY());
	 */
	
	/* Detect Drops
	 *  for(int i = 0; i<Drop.AllDrops.size(); i++){
	 *       // Drops is an enum of all Drops - look it up to find more Types
	 *		if(Drop.AllDrops.elementAt(i).getDroptype() == Drops.NEXTWEAPON){
	 *			p = Drop.AllDrops.elementAt(i);
	 *          // Do Stuff with the Drop
	 *			break;
	 *		}
	 *	}
	 */
	
	/* Detect Monster Type
	 * if(game.Monsters.size > 0)
	 * 	MonsterType mt = game.Monsters.get(0).getMonsterType();
	 * // Do stuff
	 * MonsterType is an Enum - look it up!
	 */
	
}
