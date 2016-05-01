package me.game.pack;

import javafx.application.Application;
import javafx.stage.Stage;

public class Game extends Application {
	static Frame spiel;

	public static void main(String[] args) {
		spiel = Frame.getInstance();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			spiel.start(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}