package me.game.pack;

import java.util.Vector;

import javafx.application.Application;
import javafx.stage.Stage;
import me.game.monsterWeapons.MonsterWeapon;
import me.game.playerWeapons.PlayerWeapon;

public class Game extends Application {
	static Frame spiel;
	private static Stage primaryStage;
	private static GameSettings settings = null;
	 
	
	public static GameSettings getSettings() {
		return settings;
	}
	public static void setSettings(GameSettings settings) {
		Game.settings = settings;
	}

	
	public static void main(String[] args) {
		spiel = Frame.getInstance();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Game.primaryStage = primaryStage;
		try {
			spiel.start(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void restart(){
		Frame.game = null;
		spiel = Frame.getInstance();
		
		PlayerWeapon.ActiveWeapons = new Vector<PlayerWeapon>();
		MonsterWeapon.ActiveWeapons = new Vector<MonsterWeapon>();
		Drop.AllDrops = new Vector<Drop>();
		
		try {
			spiel.start(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}