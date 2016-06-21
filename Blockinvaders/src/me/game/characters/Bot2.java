package me.game.characters;

import me.game.bullets.Bullet;
import me.game.monsterWeapons.MonsterWeapon;
import me.game.pack.Drop;
import me.game.pack.Frame;

public class Bot2 implements BotKI {

	public final Frame game;
	private Player player;
	private double destX = 800;
	private boolean lockLeft, lockRight;
	int bal;

	private Bullet dodge;
	private Monster locked = null;

	public Bot2(Player player) {
		this.game = Frame.getInstance();
		this.player = player;
	}

	public void refresh() {
		if(!player.isAlive())
			return;
		bal = 0;
		if (locked == null || !locked.isAlive())
			findMonster();

		watchBullets();
		getDrops();
		destination();
		specialShoot();

		move();

		if (player.getHisWeapon().getAmmo() == 0 ||
				(Math.abs(player.getX()-destX) <= 10 && game.Monsters.size() > 0))
			player.getHisWeapon().shoot(player.getX(), player.getY());
	}

	private void findMonster() {
		double minDistance = 1000;
		
		for (int i = 0; i < game.Monsters.size(); i++) {
			Monster m = game.Monsters.elementAt(i);
			if (m.isAlive()) {
				double offset = (game.Monster_Direction == 0 ? 1 : -1)*((m.getY() - player.getY()) / player.getHisWeapon().getBulletSpeed());
				double distance = Math.abs(player.getX() + player.getWidth() / 2 - m.getX() - m.getWidth() / 2 +offset);
				if (distance < minDistance) {
					minDistance = distance;
					locked = m;
				}
			}
		}
	}

	private double destination() {
		if (game.Monster_Direction == 1)
			destX = locked.getX() + locked.getWidth() / 2 - player.getWidth()/2
					- ((locked.getY() - player.getY()) / player.getHisWeapon().getBulletSpeed());
		else
			destX = locked.getX() + locked.getWidth() / 2 - player.getWidth()/2
					+ ((locked.getY() - player.getY()) / player.getHisWeapon().getBulletSpeed());

		return destX;
	}

	private void getDrops() {
		Drop nextDrop = null;
		double minDistance = 400;
		for (Drop d : Drop.AllDrops) {
			if (d.getyPos() <= player.getY() + player.getHeight() && d.getxPos() + Drop.DropSize >= player.getX() - 800
					&& d.getxPos() <= player.getX() + player.getWidth() + 800) {
				double distance = player.getY() - d.getyPos() - Drop.DropSize;
				if (distance < minDistance) {
					minDistance = distance;
					nextDrop = d;
				}
			}
		}
		if (nextDrop != null) {
			findMonster();
			double dir = player.getX() + player.getWidth() / 2 - nextDrop.getxPos() - Drop.DropSize / 2;
			if (player.getY() - nextDrop.getyPos() - Drop.DropSize <= 100) {
				if (!lockRight && dir < -5)
					bal += 100;
				else if (!lockLeft && dir > 5)
					bal -= 100;
			} else {
				if (!lockRight && dir < -100)
					bal += 100;
				else if (!lockLeft && dir > 100)
					bal -= 100;
			}
		}

	}

	private void move() {
		if (dodge != null)
			dodgeBullet();

		if (!lockRight && destX > player.getX() + 5)
			bal += 20;
		else if (!lockLeft && destX < player.getX() - 5)
			bal -= 20;
		
		if (bal > 0)
			player.moveRight();
		else if (bal < 0)
			player.moveLeft();
		
		lockLeft = false;
		lockRight = false;
	}

	private void watchBullets() {
		double minDistance = 150;
		dodge = null;
		for (MonsterWeapon mw : MonsterWeapon.ActiveWeapons) {
			for (Bullet b : mw.getKugeln()) {
				if (b.getxPos() + b.getWidth() >= player.getX() - 20
						&& b.getxPos() <= player.getX() + player.getWidth() + 20 && b.getyPos() < player.getY()) {
					double distance = player.getY() - b.getyPos() - b.getHeight();
					if (distance < minDistance) {
						minDistance = distance;
						dodge = b;
					}
				}
				int move = 0;
				if (b.getyPos() >= player.getY() - 30 && b.getyPos() <= player.getY() + player.getHeight()
						&& player.getX() - 20 <= b.getxPos() + b.getWidth()
						&& player.getX() >= b.getxPos() + b.getWidth()) {
					lockLeft = true;
					if (player.getX() - 10 <= b.getxPos() + b.getWidth())
						move++;
				} else if (b.getyPos() >= player.getY() - 30 && b.getyPos() <= player.getY() + player.getHeight()
						&& player.getX() + player.getWidth() + 20 >= b.getxPos()
						&& player.getX() + player.getWidth() <= b.getxPos()) {
					lockRight = true;
					if (player.getX() + player.getWidth() + 10 >= b.getxPos())
						move--;
				}
				if (move > 0)
					bal += 500;
				else if (move < 0)
					bal -= 500;
			}
		}
	}

	private void dodgeBullet() {
		double dir = player.getX() + player.getWidth() / 2 - dodge.getxPos() - dodge.getWidth() / 2;
		if (!lockRight && dir >= 0)
			bal += 1000;
		else if (!lockLeft && dir < 0)
			bal -= 1000;
		else if(!lockRight)
			bal += 1000;
		else if(!lockLeft)
			bal -= 1000;

	}

	private void specialShoot() {
		if (player.hisSpecialWeapon.getAmmo() > 0) {
			int alive = 0;
			for (Monster m : game.Monsters)
				if (m.isAlive())
					alive++;

			int initMonsters = 0;
			switch (game.clearcount % 6) {
			case 0:
				initMonsters = 64;
				break;
			case 1:
				initMonsters = 12;
				break;
			case 2:
				initMonsters = 32;
				break;
			case 3:
				initMonsters = 128;
				break;
			case 4:
				initMonsters = 1;
				break;
			case 5:
				initMonsters = 4;
				break;
			}

			if ((game.clearcount >= 20 && (double) alive / initMonsters >= 0.8)
					|| (game.clearcount >= 30 && (double) alive / initMonsters >= 0.6)
					|| (game.clearcount >= 40 && (double) alive / initMonsters >= 0.4)) {
				if (player.getX() + player.getWidth() / 2 >= Frame.GAME_WIDTH / 3
						&& player.getX() + player.getWidth() / 2 <= (Frame.GAME_WIDTH / 3) * 2) {
					player.hisSpecialWeapon.shoot(player.getX(), player.getY());
				} else
					destX = Frame.GAME_WIDTH / 2;
			}
		}
	}
}
