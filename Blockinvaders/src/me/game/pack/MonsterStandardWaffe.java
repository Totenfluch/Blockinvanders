package me.game.pack;

public class MonsterStandardWaffe extends MonsterWaffe{
	double bulletW = 5.0,
			bulletH = 5.0;
	public MonsterStandardWaffe(Monster Besitzer) {
		super(Besitzer, 10, 12, 4);
	}

	@Override
	public void shoot(double monsterX, double monsterY) {
			super.shoot(monsterX, monsterY, bulletW, bulletH, 0);
	}
}