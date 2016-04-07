package me.game.pack;

import java.util.Random;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Frame extends Application{
	public static final int GAME_WIDTH = 1600;
	public static final int GAME_LENGTH = 900;
	
	public static Canvas cv;
	public static GraphicsContext gc;
	
	public static Stage MainStage;
	public static Scene ConnectScene;
	public static Scene GameScene;
	
	
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainStage = primaryStage;
		MainStage.setTitle("BlockInvaders");
		// Connect Scene
		BorderPane connect_Bp = new BorderPane();
		VBox connect_MiddlePart = new VBox();
		connect_MiddlePart.setPadding(new Insets(200, 500, 500, 500)); 
		connect_MiddlePart.setSpacing(20);
		
		
		connect_Bp.setCenter(connect_MiddlePart);
		//BorderPane.setAlignment(connect_MiddlePart, Pos.CENTER);
		

		
		Random r = new Random();
		int id = r.nextInt(20);
		TextField Username = new TextField("Username#" + id);
		connect_MiddlePart.getChildren().add(Username);
		
		TextField Host = new TextField("Host:Port");
		connect_MiddlePart.getChildren().add(Host);
		
		
		Button Confirm = new Button("Connect");
		connect_MiddlePart.getChildren().add(Confirm);
		connect_MiddlePart.setAlignment(Pos.TOP_CENTER);
		Confirm.setAlignment(Pos.BOTTOM_CENTER);
		Confirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				switchSceneToGame();
			}
		});
		
		ConnectScene = new Scene(connect_Bp, GAME_WIDTH, GAME_LENGTH);
		MainStage.setScene(ConnectScene);
		
		// Connect Scene fin
		
		
		// Game Scene
		HBox cvcontainer = new HBox();
		cv = new Canvas(GAME_WIDTH, GAME_LENGTH);
		gc = cv.getGraphicsContext2D();
		cvcontainer.getChildren().add(cv);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);
		GameScene = new Scene(cvcontainer, GAME_WIDTH, GAME_LENGTH);
		
		
		MainStage.show();
	}
	
	public void switchSceneToGame(){
		MainStage.setScene(GameScene);
	}
}
