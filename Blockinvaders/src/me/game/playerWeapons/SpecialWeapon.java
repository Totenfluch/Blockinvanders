package me.game.playerWeapons;

import me.game.pack.Player;

public abstract class SpecialWeapon extends PlayerWeapon{
	public SpecialWeapon(Player Besitzer, int damage, int delay, int bulletSpeed) {
		super(Besitzer, damage, delay, bulletSpeed);
	}
}
