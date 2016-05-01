package me.game.pack;

import javafx.application.Application;
import javafx.stage.Stage;

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
		try {
			spiel.start(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}