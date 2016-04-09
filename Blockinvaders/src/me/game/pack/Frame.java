package me.game.pack;

import java.util.Random;
import java.util.Vector;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Frame extends Application{
	public static final int GAME_WIDTH = 1600;
	public static final int GAME_LENGTH = 900;

	public static Timeline tf;

	public static Canvas cv;
	public static GraphicsContext gc;

	public static Stage MainStage;
	public static Scene ConnectScene;
	public static Scene GameScene;

	public static Vector<Spieler> Players = new Vector<Spieler>();
	public static boolean inRight = false;
	public static boolean inLeft = false;
	public static boolean inShoot = false;
	public static Monster[] Monsters = new Monster[64];
	public static int Monster_Direction = 0;

	public static int Tick = 0;
	public static int movetick = 0;

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


		tf = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				gc.clearRect(0, 0, GAME_WIDTH, GAME_LENGTH);
				gc.setFill(Color.BLACK);
				gc.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);
				Refresh();
				// Players
				gc.setFill(Color.BLUE);
				for(int i = 0; i<Players.size(); i++){
					Spieler p = Players.elementAt(i);
					if(!p.isDead())
						gc.fillRect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
				}

				gc.setFill(Color.ORANGE);
				for(int i = 0; i<Monsters.length; i++){
					Monster Monti = Monsters[i];
					gc.fillRect(Monti.getPosX(), Monti.getPosY(), Monti.getWidth(), Monti.getLength());
				}


				gc.setFont(new Font("Impact", 30));
				gc.setFill(Color.BLUEVIOLET);
				gc.fillText(""+Players.elementAt(0).getScore(), 50, 50);
				gc.setFill(Color.RED);
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i<Players.elementAt(0).getLeben(); i++)
					sb.append("â�¤ ");
				gc.fillText(sb.toString(), 45, 75);
				
				if(Players.size() > 1){
					gc.setFont(new Font("Impact", 30));
					gc.setFill(Color.LIME);
					gc.fillText(""+Players.elementAt(1).getScore(), 50, 825);
					gc.setFill(Color.RED);
					StringBuffer sb2 = new StringBuffer();
					for(int i = 0; i<Players.elementAt(1).getLeben(); i++)
						sb2.append("â�¤ ");
					gc.fillText(sb2.toString(), 45, 850);
				}
				
				for(int x = 0; x<Waffen.kugeln.size(); x++){
					Kugel p = Waffen.kugeln.elementAt(x);
					gc.fillRect(p.xPos, p.yPos, p.width, p.height);
				}
				

			}
		}));

		GameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.A){
					inLeft = true;
				}else if(event.getCode() == KeyCode.D){
					inRight = true;
				}if(event.getCode() == KeyCode.SPACE){
					inShoot = true;
				}
			}
		});

		GameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.A){
					inLeft = false;
				}else if(event.getCode() == KeyCode.D){
					inRight = false;
				}if(event.getCode() == KeyCode.SPACE){
					inShoot = false;
				}
			}
		});

	}

	public static void switchSceneToGame(){
		Spieler P1 = new Spieler(GAME_WIDTH/2-100, 10, new StandardWaffe());
		
		int x = 0;
		int ix = 0;
		for(int i = 0; i<Monsters.length; i++){
			ix++;
			if(i%16 == 0){
				x++;
				ix = 0;
			}
			int sub = 0;
			if(x%2 == 0)
				sub = 50;

			Monsters[i] = new Monster(1, sub+600+ix*50, x*100, 30, 20, 1);
		}
		Players.add(P1);
		Spieler P2 = new Spieler(GAME_WIDTH/2+100, 10, new StandardWaffe());
		Players.add(P2);
		tf.setCycleCount(Timeline.INDEFINITE);
		tf.play();
		MainStage.setScene(GameScene);
	}

	public static void Refresh(){
		Tick++;
		movetick ++ ;
		if(movetick == 500 && Monster_Direction == 0){
			Monster_Direction = 1;
			movetick = 0;
		}else if(movetick == 500 && Monster_Direction == 1){
			Monster_Direction = 0;
			movetick = 0;
		}

		if(Monster_Direction == 1){
			for(int i = 0; i<Monsters.length; i++){
				Monsters[i].moveRight();
			}
		}else if(Monster_Direction == 0){
			for(int i = 0; i<Monsters.length; i++){
				Monsters[i].moveLeft();
			}
		}

		if(inLeft){
			Players.elementAt(0).moveLeft();
		}else if(inRight){
			Players.elementAt(0).moveRight();
		}
		if(inShoot){
			Players.elementAt(0).waffe.shoot(Players.elementAt(0).getX(), Players.elementAt(0).getY());
		}
		for(int x =0;x<Players.size(); x++){
			Players.elementAt(x).waffe.refresh();
		}
		for(int x =0;x<Waffen.kugeln.size(); x++){
			Kugel r = Waffen.kugeln.elementAt(x);
			if(r.yPos <= 0 || r.yPos >= GAME_LENGTH || r.xPos <= 0 || r.xPos >= GAME_WIDTH)
				Waffen.kugeln.remove(r);
			
			
			r.refresh();
		}
	
	}

}
